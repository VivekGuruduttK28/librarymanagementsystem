package com.project.library_management_system.services;

import com.project.library_management_system.entity.LendingEntity;
import com.project.library_management_system.repository.LendingRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private LendingRepository lendingRepository;

    @Scheduled(cron = "0 16 22 * * ?")
    public void sendDueDateRemainder(){
        LocalDate remainderDate = LocalDate.now().plusDays(2);
        List<LendingEntity> lendings = lendingRepository.findByDueDate(remainderDate);

        for(LendingEntity lending: lendings){
            sendRemainderEmail(lending.getUser().getEmail(), lending.getBook().getTitle(),lending.getDueDate());
        }
    }
    public void sendRemainderEmail(String userEmail, String bookName, LocalDate dueDate){
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(userEmail);
            helper.setSubject("Library Book Due Remainder");
            helper.setText("Dear User, \n\n Your book \"" + bookName +
                    "\" is due on " + dueDate + ". Please return it on time to avoid fines.\n Thank you!");

            javaMailSender.send(message);
            System.out.println("Email sent");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
