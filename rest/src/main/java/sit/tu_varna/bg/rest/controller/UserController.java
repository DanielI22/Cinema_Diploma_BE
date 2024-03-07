package sit.tu_varna.bg.rest.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sit.tu_varna.bg.api.operation.user.login.LoginOperation;
import sit.tu_varna.bg.api.operation.user.login.LoginRequest;
import sit.tu_varna.bg.api.operation.user.login.LoginResponse;
import sit.tu_varna.bg.api.operation.user.register.RegisterOperation;
import sit.tu_varna.bg.api.operation.user.register.RegisterRequest;
import sit.tu_varna.bg.api.operation.user.register.RegisterResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/user")
public class UserController {
    private final LoginOperation login;
    private final RegisterOperation register;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return new ResponseEntity<>(login.process(request), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request) {
        return new ResponseEntity<>(register.process(request), HttpStatus.OK);
    }
}
