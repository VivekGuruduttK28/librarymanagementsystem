package com.project.library_management_system.serviceImplementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.project.library_management_system.model.UserPrincipal;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.library_management_system.entity.userEntity;
import com.project.library_management_system.model.User;
import com.project.library_management_system.repository.userRepository;
import com.project.library_management_system.services.userService;


@Service
public class userServiceImplementation implements userService {

    @Autowired
    userRepository userrepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<Map<String, Object>> createUser(User user) {
        Map<String,Object> map = new HashMap<>();
        user.setRole("USER");
        System.out.println(user);
        if(userrepository.findByUsername(user.getUsername()).isPresent()){
            map.put("status","error");
            map.put("message","user already exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
            // return "User already exists";
        }
        userEntity userentity = new userEntity();
        BeanUtils.copyProperties(user, userentity);
        userentity.setPassword(passwordEncoder.encode(user.getPassword()));
        userrepository.save(userentity);
        map.put("status", "success");
        map.put("message","Successfully created");
        return ResponseEntity.status(HttpStatus.CREATED).body(map);
    }

    @Override
    public List<User> readUser() {
        
        List<userEntity> userList = userrepository.findAll();
        List<User> users = new ArrayList<>();
        for(userEntity u: userList){
            User user = new User();
            BeanUtils.copyProperties(u, user);
            users.add(user);
        }
        return users;
    }

    @Override
    public ResponseEntity<Map<String, Object>> updateUser(Long id, User updatedUser) {
        Map<String, Object> response = new HashMap<>();
        return userrepository.findById(id)
                .map(userEntity -> {
                    if(!userEntity.getUsername().equals(updatedUser.getUsername())){
                        response.put("status","error");
                        response.put("message","Username cannot be changed");
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                    }
                    userEntity.setPhone(updatedUser.getPhone());
                    userEntity.setAddress(updatedUser.getAddress());
                    userEntity.setEmail(updatedUser.getEmail());
                    userEntity.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                    userrepository.save(userEntity);
                    response.put("status","success");
                    response.put("message","User update successful");
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                })
                .orElseGet(()->{
                    response.put("status","error");
                    response.put("message","User not successful");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @Override
    public ResponseEntity<Map<String, Object>> deleteUser(Long id) {
        Map<String, Object> response = new HashMap<>();
        if(userrepository.existsById(id)) {
            userrepository.deleteById(id);
            response.put("status", "success");
            response.put("message", "delete successful");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        else {
            response.put("status", "failure");
            response.put("message", "delete unsuccessful as userId not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        userEntity user = userrepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not found"));

        return new UserPrincipal(user);
    }
}
