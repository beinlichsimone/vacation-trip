package io.github.beinlichsimone.vacationtrip.controller;

import io.github.beinlichsimone.vacationtrip.dto.LoginForm;
import io.github.beinlichsimone.vacationtrip.dto.TokenDTO;
import io.github.beinlichsimone.vacationtrip.dto.RegisterForm;
import io.github.beinlichsimone.vacationtrip.config.security.TokenService;
import io.github.beinlichsimone.vacationtrip.model.User;
import io.github.beinlichsimone.vacationtrip.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Profile("prod")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<TokenDTO> autenticar(@RequestBody @Valid LoginForm form){

        UsernamePasswordAuthenticationToken dadosLogin = form.converter();

        try {
            Authentication authentication = authenticationManager.authenticate(dadosLogin);
            String token = tokenService.gerarToken(authentication);
            return ResponseEntity.ok(new TokenDTO(token, "Bearer"));

        }catch (AuthenticationException e){
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(@RequestBody @Valid RegisterForm form) {
        Optional<User> existente = userRepository.findByEmail(form.getEmail());
        if (existente.isPresent()) {
            return ResponseEntity.status(409).body("E-mail já cadastrado");
        }

        User novo = new User();
        novo.setNome(form.getNome());
        novo.setEmail(form.getEmail());
        novo.setPassword(passwordEncoder.encode(form.getPassword()));
        novo.setEnabled(true);
        userRepository.save(novo);

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(form.getEmail(), form.getPassword())
            );
            String token = tokenService.gerarToken(auth);
            return ResponseEntity.created(URI.create("/auth/register")).body(new TokenDTO(token, "Bearer"));
        } catch (AuthenticationException e) {
            return ResponseEntity.ok().build();
        }
    }
}
