package com.project.library_management_system.serviceImplementation;

import com.project.library_management_system.dto.LendingDto;
import com.project.library_management_system.entity.FineEntity;
import com.project.library_management_system.entity.LendingEntity;
import com.project.library_management_system.entity.userEntity;
import com.project.library_management_system.repository.FineRepository;
import com.project.library_management_system.repository.LendingRepository;
import com.project.library_management_system.repository.userRepository;
import com.project.library_management_system.services.LendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LendingServiceImplementation implements LendingService {

    @Autowired
    private LendingRepository lendingRepository;

    @Autowired
    private FineRepository fineRepository;

    @Autowired
    private userRepository userrepository;

    @Override
    public List<LendingEntity> getAllLendings() {
        return lendingRepository.findAll();
    }

    @Override
    public ResponseEntity<Map<String, Object>> markAsCollected(Long lendingId) {
        Map<String, Object> response = new HashMap<>();
        LendingEntity lendingEntity = lendingRepository.findById(lendingId)
                .orElseThrow(() -> new RuntimeException("Lending Id not found"));
        lendingEntity.setCollected(true);
        lendingRepository.save(lendingEntity);
        response.put("status","success");
        response.put("message", "Marked as collected");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    @Override
    public ResponseEntity<Map<String, Object>> markAsReturned(Long lendingId) {
        Map<String,Object> response =  new HashMap<>();
        Optional<LendingEntity> lendingEntity = lendingRepository.findById(lendingId);
        Optional<FineEntity> fineEntity = fineRepository.findByLendingId(lendingId);

        if(fineEntity.isPresent()){
            if(!fineEntity.get().isPaid()){
                response.put("status", "error");
                response.put("message","Please clear the dues before returning");
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            else{
                if(lendingEntity.get().isCollected()) {
                    Optional<LendingEntity> lending = lendingRepository.findById(lendingId);
                    lending.get().setStatus("RETURNED");
                    lending.get().getBook().setCopies_available(lending.get().getBook().getCopies_available() + 1);
                    lending.get().setReturnDate(LocalDate.now());
                    lendingRepository.save(lending.get());
                    response.put("status", "success");
                    response.put("message", "Return Successful");
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
                else{
                    response.put("status", "error");
                    response.put("message", "Return unsuccessful as books are not collected yet");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
            }
        }
        else {
            if (lendingEntity.isPresent() && lendingEntity.get().isCollected()) {
                Optional<LendingEntity> lending = lendingRepository.findById(lendingId);
                lending.get().setStatus("RETURNED");
                lending.get().getBook().setCopies_available(lending.get().getBook().getCopies_available() + 1);
                lending.get().setReturnDate(LocalDate.now());
                lendingRepository.save(lending.get());
                response.put("status", "success");
                response.put("message", "Return Successful");
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else if(lendingEntity.isPresent() && !lendingEntity.get().isCollected()){
                response.put("status", "error");
                response.put("message", "Collect before return");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

        }


        response.put("status", "error");
        response.put("message", "lending id not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @Override
    public List<LendingDto> displayLendingsDetails() {
        return lendingRepository.getAllLendings();
    }

    @Override
    public int getBorrowedBooks() {
        return lendingRepository.getAllLendings().size();
    }

    @Override
    public long getTotalOverdueBooks(){
        return lendingRepository.countOverdueBooks();
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
    public ResponseEntity<List<LendingDto>> getHistory() {
        String username = getLoggedInUsername();

        Optional<userEntity> optionalUser = userrepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }

        userEntity user = optionalUser.get();
        List<LendingEntity> lendings = lendingRepository.findByUser(user);

        List<LendingDto> lendingDtos = lendings.stream()
                .map(lending -> new LendingDto(
                        lending.getId(),
                        lending.getBook().getTitle(),
                        lending.getBook().getImage_url(),
                        lending.getBorrowDate(),
                        lending.getDueDate(),
                        lending.getStatus())
                ).collect(Collectors.toList());

        return ResponseEntity.ok(lendingDtos);
    }

    @Override
    public ResponseEntity<Integer> getHistoryCount() {
        String username =getLoggedInUsername();
        List<LendingEntity> lendings = lendingRepository.findByUser(userrepository.findByUsername(username).get());
        return ResponseEntity.ok(lendings.size());
    }

    @Override
    public int getOverdueBooks() {
        String username = getLoggedInUsername();
        return lendingRepository.findByUserAndStatus(userrepository.findByUsername(username).get(), "OVERDUE").size();
    }

}
