package com.project.library_management_system.serviceImplementation;

import com.project.library_management_system.entity.FineEntity;
import com.project.library_management_system.entity.LendingEntity;
import com.project.library_management_system.entity.userEntity;
import com.project.library_management_system.repository.FineRepository;
import com.project.library_management_system.repository.LendingRepository;
import com.project.library_management_system.repository.userRepository;
import com.project.library_management_system.services.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FineServiceImplementation implements FineService {
    @Autowired
    private LendingRepository lendingRepository;

    @Autowired
    private FineRepository fineRepository;

    @Autowired
    private userRepository userrepository;
    @Override
    public void updateFinesDaily() {
        List<LendingEntity> lendings = lendingRepository.findAll();
        int FINE_PER_DAY= 5;
        for(LendingEntity lending: lendings){
            if(lending.isCollected()) {
                if (lending.getDueDate().isBefore(LocalDate.now()) && !lending.getStatus().equals("OVERDUE")) {
                    lending.setStatus("OVERDUE");
                    lendingRepository.save(lending);
                }
                if (lending.getStatus().equals("OVERDUE")) {
                    FineEntity fineEntity = fineRepository.findByLendingId(lending.getId())
                            .orElse(null);
                    if (fineEntity == null) {
                        fineEntity = new FineEntity();
                        fineEntity.setUser(lending.getUser());
                        fineEntity.setLending(lending);
                        int overdueDays = Period.between( lending.getDueDate(),LocalDate.now()).getDays();
                        fineEntity.setFineAmount(overdueDays * FINE_PER_DAY);
                        fineEntity.setPaid(false);
                        fineEntity.setFineDate(null);
//                    fineEntity.setFineDate(LocalDate.of( 0,0,0));
                    } else {
                        int overdueDays = Period.between(lending.getDueDate(),LocalDate.now()).getDays();
                        if(overdueDays>0) {
                            fineEntity.setFineAmount(overdueDays * FINE_PER_DAY);
                        }
                    }
                    fineRepository.save(fineEntity);
                    System.out.println("FINES UPDATED");
                }
            }
        }
    }
    @Override
    public String getLoggedInUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails){
            return ((UserDetails)principal).getUsername();
        }
        else{
            return principal.toString();
        }
    }
    @Override
    public ResponseEntity<Map<String, Object>> payFine() {
        Map<String, Object> response = new HashMap<>();
        String username = getLoggedInUsername();
        userEntity userentity = userrepository.findByUsername(username).get();
        List<FineEntity> fineEntities = fineRepository.findByUserId(userentity.getId());
        List<LendingEntity> lendingEntities = lendingRepository.findByUser(userentity);
        if(fineEntities.isEmpty()) {
            response.put("status", "success");
            response.put("message", "No fines");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        boolean allFinesPaid = fineEntities.stream().allMatch(FineEntity::isPaid);
        if(allFinesPaid){
            response.put("status","success");
            response.put("message", "All fines are cleared");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        boolean paymentStatus = true;
        if(paymentStatus) {
            for (FineEntity fineEntitiy : fineEntities) {
                fineEntitiy.setPaid(true);
                fineEntitiy.setFineDate(LocalDate.now());
                fineRepository.save(fineEntitiy);
            }
            response.put("status","success");
            response.put("message", "payment successful");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        response.put("status","failed");
        response.put("message", "payment unsuccessful");
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(response);
    }

    @Override
    public ResponseEntity<Map<String,Object>> totalFine() {

        String username = getLoggedInUsername();

        Optional<userEntity> optionalUser = userrepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found"));
        }

        userEntity user = optionalUser.get();
        int totalFine = (int)fineRepository.findByUser(user)
                .stream()
                .filter(fine -> !fine.isPaid()) // Consider only unpaid fines
                .mapToDouble(FineEntity::getFineAmount)
                .sum();

        return ResponseEntity.ok(Map.of("totalFine", totalFine));
    }
}
