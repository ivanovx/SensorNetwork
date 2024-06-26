package org.sensornetwork.auth.user;

import jakarta.validation.Valid;
import org.sensornetwork.auth.request.SignInRequest;
import org.sensornetwork.auth.request.SignUpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/signin")
    public String signIn(Model model) {
        model.addAttribute("user", new SignInRequest());

        return "signin";
    }

    @GetMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute("user", new SignUpRequest());

        return "signup";
    }

    @PostMapping("/signup")
    public String signUp(@Valid @ModelAttribute("user") SignUpRequest request, Model model) {
        if (userRepository.existsByEmail(request.getEmail())) {
            //errors.rejectValue("email", "email.exists");

            model.addAttribute("error", "This email address is already in use.");

            return "signup";
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            model.addAttribute("error", "This username is already in use.");

            return "signup";
        }

        User user = new User();

        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setCreated(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        return "redirect:/";
    }
}
