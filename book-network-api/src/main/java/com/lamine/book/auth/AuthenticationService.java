package com.lamine.book.auth;

import com.lamine.book.email.EmailService;
import com.lamine.book.email.EmailTemplateName;
import com.lamine.book.role.RoleRepository;
import com.lamine.book.security.JwtService;
import com.lamine.book.user.Token;
import com.lamine.book.user.TokenRepository;
import com.lamine.book.user.User;
import com.lamine.book.user.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;


    public void register(RegistrationRequest request) throws MessagingException {

        var userRole=roleRepository.findByName("USER")
                .orElseThrow(()->new IllegalStateException("Role USER was not found"));

        new User();
        var user= User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();

        userRepository.save(user);
        sendValidationEmail(user);




    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken=generateAndSaveActivationToken(user);


        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Activation Account"

        );
    }

    private String generateAndSaveActivationToken(User user) {
        // generate token
        String generatedToken = generateActivationCode(6);

      //  new Token();
        var token=Token.builder()
                .token(generatedToken)
                .user(user)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .build();
        tokenRepository.save(token);

        return generatedToken;
    }

    private String generateActivationCode(int length) {

        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var claims=new HashMap<String, Object>();
        var user=((User)authentication.getPrincipal());
        claims.put("fullName",user.fullName());
        var jwtToken=jwtService.generateToken(claims,user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    //@Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken= tokenRepository.findByToken(token)
                // todo Exception has to be defined
                .orElseThrow(()-> new RuntimeException("Token not found"));

        if(LocalDateTime.now().isAfter(savedToken.getExpiresAt())){
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token is expired.A new token has been sent to the same email address.");
        }

        var user=userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(()->new UsernameNotFoundException("User not found"));

        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);

    }
}
