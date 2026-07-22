package com.linklite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.linklite.dto.LoginRequest;
import com.linklite.dto.RegisterRequest;
import com.linklite.entity.User;
import com.linklite.repository.UserRepository;

@Service
public class AuthService{

    @Autowired
    private UserRepository userRepository;

    public String register(RegisterRequest request){

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        userRepository.save(user);

        return "User Registered Successfully";
    }
    
    @RestController
    @RequestMapping("/api/auth")
    @CrossOrigin(origins = "http://localhost:5173")
    public class AuthController {

        @Autowired
        private AuthService authService;

        @PostMapping("/register")
        public String register(@RequestBody RegisterRequest request){

            return authService.register(request);

        }

        @PostMapping("/login")
        public String login(@RequestBody LoginRequest request){

            return authService.login(request);

        }

    }

    public String login(LoginRequest request){

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        if(!user.getPassword().equals(request.getPassword())){
            throw new RuntimeException("Invalid Password");
        }

        return "Login Successful";
    }

}