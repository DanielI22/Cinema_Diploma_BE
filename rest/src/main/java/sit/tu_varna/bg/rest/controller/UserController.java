package sit.tu_varna.bg.rest.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sit.tu_varna.bg.api.operation.user.archive.ArchiveUserOperation;
import sit.tu_varna.bg.api.operation.user.archive.ArchiveUserRequest;
import sit.tu_varna.bg.api.operation.user.archive.ArchiveUserResponse;
import sit.tu_varna.bg.api.operation.user.login.LoginOperation;
import sit.tu_varna.bg.api.operation.user.login.LoginRequest;
import sit.tu_varna.bg.api.operation.user.login.LoginResponse;
import sit.tu_varna.bg.api.operation.user.password.PasswordChangeOperation;
import sit.tu_varna.bg.api.operation.user.password.PasswordChangeRequest;
import sit.tu_varna.bg.api.operation.user.password.PasswordChangeResponse;
import sit.tu_varna.bg.api.operation.user.register.RegisterOperation;
import sit.tu_varna.bg.api.operation.user.register.RegisterRequest;
import sit.tu_varna.bg.api.operation.user.register.RegisterResponse;
import sit.tu_varna.bg.core.service.user.LogoutService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/user")
public class UserController {
    private final LoginOperation login;
    private final RegisterOperation register;
    private final LogoutService logout;
    private final PasswordChangeOperation changePassword;
    private final ArchiveUserOperation archive;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return new ResponseEntity<>(login.process(request), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {
        return new ResponseEntity<>(register.process(request), HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<Integer> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logout.logout(request, response, authentication);
        return ResponseEntity.status(200).build();
    }

    @PatchMapping("/change")
    public ResponseEntity<PasswordChangeResponse> changePassword(@RequestBody @Valid PasswordChangeRequest request) {
        return new ResponseEntity<>(changePassword.process(request), HttpStatus.OK);
    }

    @DeleteMapping("/archive")
    public ResponseEntity<ArchiveUserResponse> archive(@RequestBody @Valid ArchiveUserRequest request) {
        return new ResponseEntity<>(archive.process(request), HttpStatus.OK);
    }
}
