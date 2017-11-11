package in.techbeat.palapa.controller;

import in.techbeat.palapa.exception.InvalidInputException;
import in.techbeat.palapa.model.db.User;
import in.techbeat.palapa.model.request.CreateUserRequest;
import in.techbeat.palapa.model.request.LoginRequest;
import in.techbeat.palapa.model.response.LoginResponse;
import in.techbeat.palapa.model.response.UserResponse;
import in.techbeat.palapa.service.UserService;
import in.techbeat.palapa.validator.UserValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@Slf4j
public class ApiController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @RequestMapping(method = GET, value = "/ping")
    public ResponseEntity<String> handlePing() {
        log.debug("Responding to ping");
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @RequestMapping(method = POST, value = "/register")
    public UserResponse createUser(@RequestBody final CreateUserRequest input, final BindingResult bindingResult) {
        log.debug("Got registration request for name {} and email {}", input.getUsername(), input.getEmail());
        userValidator.validate(input, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new InvalidInputException(StringUtils.join(bindingResult.getAllErrors().toArray(), ";"));
        }
        final User user = userService.createUser(input);
        log.debug(user.toString());
        return user.toUserResponse();
    }

    @RequestMapping(method = GET, value = "/user/name/{username}")
    public UserResponse findByUsername(@PathVariable("username") final String username) {
        return userService.findByUsername(username).toUserResponse();
    }

    @RequestMapping(method = POST, value = "/login")
    public LoginResponse login(@RequestBody LoginRequest input) {
        log.debug("Got request for login {}", input.getUsername());
        final LoginResponse loginResponse = LoginResponse.builder().token(userService.login(input)).build();
        return loginResponse;
    }

}
