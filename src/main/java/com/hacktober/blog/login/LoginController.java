package com.hacktober.blog.login;

import com.hacktober.blog.login.dto.LoginDTO;
import com.hacktober.blog.login.entity.UserLoginEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/login")
@CrossOrigin("*")
@Tag(name = "Authentication", description = "Endpoints for authenticating users")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public String login(@RequestBody LoginDTO loginDTO)
            throws InterruptedException, ExecutionException {

        String token = loginService.login(loginDTO.getUsername(), loginDTO.getPassword());
        return String.format("{\"token\":\"%s\"}",token);
    }
}
