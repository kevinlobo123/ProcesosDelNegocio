package com.proyecto.app.controllers;

import com.proyecto.app.entity.User;
import com.proyecto.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "api/users/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@PathVariable Long id){
        Optional<User> foundUser = userRepository.findById(id);
        if(foundUser.isPresent()){
            return ResponseEntity.ok(foundUser.get());
        }
        Map<String,String> errorResponse = new LinkedHashMap<>();
        errorResponse.put("error","Not found");
        errorResponse.put("message", "User not found");
        errorResponse.put("status", HttpStatus.NOT_FOUND.toString());
        return new ResponseEntity(errorResponse, HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "api/users", method = RequestMethod.POST)
    public ResponseEntity<User> createUser(@RequestBody User user){
        Map<String,String> response = new LinkedHashMap<>();
        try{
            userRepository.save(user);
            response.put("success","registered user!");
            response.put("message","registered user success!");
            response.put("status", HttpStatus.OK.toString());
            return new ResponseEntity(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error","Error");
            response.put("message", "An error occurred while registering the user!");
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.toString());
            return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "api/users", method = RequestMethod.GET)
    public List<User> listUsers(){
        return userRepository.findAll();
    }

    @RequestMapping(value = "api/users/{id}", method = RequestMethod.PUT)
    public ResponseEntity<User> editUser(@RequestBody User newUser, @PathVariable Long id){
        Map<String, String> response = new HashMap<>();
        try {
            User user = userRepository.findById(id).get();
            user.setFirstName(newUser.getFirstName());
            user.setLastName(newUser.getLastName());
            user.setEmail(newUser.getEmail());
            user.setPassword(newUser.getPassword());
            response.put("success","user edit!");
            response.put("message","user edit success!");
            response.put("status", HttpStatus.OK.toString());
            userRepository.save(user);
            return new ResponseEntity(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error","Not found");
            response.put("message", "User not found!");
            response.put("status", HttpStatus.NOT_FOUND.toString());
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "api/users/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<User> deleteUser(@PathVariable Long id){
        Map<String, String> response = new HashMap<>();
        try {
            User user = userRepository.findById(id).get();
            userRepository.delete(user);
            response.put("success","user delete!");
            response.put("message","user delete success!");
            response.put("status", HttpStatus.OK.toString());
            return new ResponseEntity(response, HttpStatus.OK);
        }catch (Exception e){
            response.put("error","Not found");
            response.put("message", "User not found!");
            response.put("status", HttpStatus.NOT_FOUND.toString());
            //response.put("exception", e.toString());
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }


    }
}











