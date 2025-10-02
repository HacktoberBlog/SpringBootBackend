package com.hacktober.blog.login;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hacktober.blog.config.JwtUtils;
import com.hacktober.blog.exceptions.UnauthorizedException;
import com.hacktober.blog.utils.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/login")
@CrossOrigin("*")
@Tag(name = "Authentication", description = "Endpoints for authenticating users")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private LoginService loginService;

    @PostMapping
    @Operation(summary = "Authenticate user", description = "Validate a username and password and return JWT token.")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(@RequestParam String username,
            @RequestParam String password)
            throws InterruptedException, ExecutionException {

        boolean success = loginService.login(username, password);
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("login", success);

        if (success) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            data.put("token", jwt);
            data.put("tokenType", "Bearer");
            data.put("message", "Login successful");
            return ResponseEntity.ok(ApiResponse.success(data, "Login successful"));
        } else {
            throw new UnauthorizedException("Invalid username or password");
        }
    }
}
