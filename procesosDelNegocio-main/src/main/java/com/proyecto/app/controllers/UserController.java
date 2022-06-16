package com.proyecto.app.controllers;

import com.proyecto.app.entity.User;
import com.proyecto.app.repository.UserRepository;
import com.proyecto.app.util.JWTUtil;
import com.proyecto.app.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    private Message message = new Message();
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtil jwtUtil;

    private boolean validarToken(String token){
        String id = jwtUtil.getKey(token);
        return id != null;
    }

    @RequestMapping(value = "api/users/{id}", method = RequestMethod.GET)
    public Optional<User> getUser(@PathVariable Long id, @RequestHeader(value = "Authorization") String token){
        if(validarToken(token) == false){ return null;}

        Optional<User> foundUser = userRepository.findById(id);
        if(foundUser.isPresent()){
            return foundUser;
        }
        return null;
    }

    @RequestMapping(value = "api/users", method = RequestMethod.POST)
    public ResponseEntity createUser(@RequestBody User user){
        Map<String,String> response = new LinkedHashMap<>();
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return message.viewMessage(HttpStatus.OK,"success","registered user success!");
        }catch (Exception e){
            return message.viewMessage(HttpStatus.INTERNAL_SERVER_ERROR,"error","An error occurred while registering the user!");
        }

    }

    @RequestMapping(value = "api/users", method = RequestMethod.GET)
    public List<User> listUsers(@RequestHeader(value = "Authorization") String token){
        if(validarToken(token) == false){ return null;}
        return userRepository.findAll();
    }

    @RequestMapping(value = "api/users/{id}", method = RequestMethod.PUT)
    public ResponseEntity editUser(@RequestBody User newUser, @PathVariable Long id, @RequestHeader(value = "Authorization") String token){
        if(validarToken(token) == false){ return null;}
        Map<String, String> response = new HashMap<>();
        try {
            User user = userRepository.findById(id).get();
            user.setFirstName(newUser.getFirstName());
            user.setLastName(newUser.getLastName());
            user.setEmail(newUser.getEmail());
            user.setPassword(passwordEncoder.encode(newUser.getPassword()));
            userRepository.save(user);

            return message.viewMessage(HttpStatus.OK,"success","user edit success!!");
        }catch (Exception e){
            return message.viewMessage(HttpStatus.NOT_FOUND,"error","User not found!");
        }
    }

    @RequestMapping(value = "api/users/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable Long id, @RequestHeader(value = "Authorization") String token){
        if(validarToken(token) == false){ return null;}
        Map<String, String> response = new HashMap<>();
        try {
            User user = userRepository.findById(id).get();
            userRepository.delete(user);
            return message.viewMessage(HttpStatus.OK,"success","user delete success!!");
        }catch (Exception e){
            return message.viewMessage(HttpStatus.NOT_FOUND,"error","User not found!");
        }


    }
}











