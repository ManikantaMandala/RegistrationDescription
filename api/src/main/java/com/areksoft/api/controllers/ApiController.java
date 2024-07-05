package com.areksoft.api.controllers;

import com.areksoft.api.models.request.AddUserRequest;
import com.areksoft.api.models.request.OTPVerifyRequest;
import com.areksoft.api.models.User;
import com.areksoft.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApiController {

    @Autowired
    UserService service;

    public ApiController(UserService service){
        this.service = service;
    }

    @GetMapping("/")
    private String indexPage(){
        return "Index page";
    }

    @PostMapping(name = "signUp", path = "/api/v1/signup")
    public ResponseEntity<String> SignUpRequest(@RequestBody User user){
        /*  register the user
         *   Fields:
         *       - firstName
         *       - secondName
         *       - MobileNumber:
         *           - country-code
         *           - number
         */
        /* send to Message server */
        String m = service.SignUp(user);
        System.out.println(user.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(m);
    }

    @PostMapping(name="verifyOTP", path = "/api/v1/verifyOTP")
    public ResponseEntity<String> VerifyOTPRequest(@RequestBody OTPVerifyRequest request){
        /* get OTP from Redis client */
        System.out.println(request);
        String m = service.VerifyOTP(request.getUser(), request.getOtp());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(m);
    }

    @GetMapping(name="getUsers", path = "/api/v1/getUsers")
    public ResponseEntity<List<User>> getUserRequest(){
        List<User> users = service.getUsers();
        System.out.println(users);
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    /*
        Only super admin should be able to add users
     */
    @GetMapping(name="addUser", path = "/api/v1/addUser")
    public ResponseEntity<String> addUser(@RequestBody AddUserRequest request){
        System.out.println(request);
        return ResponseEntity.status(HttpStatus.OK).body("Hello this is from addUsers");
    }


}
