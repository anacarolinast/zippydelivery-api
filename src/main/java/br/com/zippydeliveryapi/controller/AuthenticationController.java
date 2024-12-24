package br.com.zippydeliveryapi.controller;

import br.com.zippydeliveryapi.config.JwtUtil;
import br.com.zippydeliveryapi.model.Usuario;
import br.com.zippydeliveryapi.service.UsuarioService;
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

    @Autowired
    private final UsuarioService usuarioService;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );

        User user = (User) authentication.getPrincipal();
        String token = this.jwtUtil.generateToken(user.getUsername());
        Long id = this.usuarioService.findByUsername(user.getUsername()).getId();

        return ResponseEntity.ok(new AuthResponse(token, id));
    }


    record AuthenticationRequest(String username, String password) {}
    record AuthResponse(String token, Long id) {}

}
