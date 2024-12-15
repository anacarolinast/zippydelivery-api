package br.com.zippydeliveryapi.controller;

import br.com.zippydeliveryapi.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final JwtUtil jwtUtil;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );

        User user = (User) authentication.getPrincipal();
        String token = jwtUtil.generateToken(user.getUsername());

        return ResponseEntity.ok(new AuthResponse(token));
    }


    record AuthenticationRequest(String username, String password) {}
    record AuthResponse(String token) {}

}
