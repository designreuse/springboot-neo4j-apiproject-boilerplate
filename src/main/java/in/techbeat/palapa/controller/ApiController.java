package in.techbeat.palapa.controller;

import in.techbeat.palapa.exception.EntityAlreadyExistsException;
import in.techbeat.palapa.exception.NotFoundException;
import in.techbeat.palapa.model.db.Role;
import in.techbeat.palapa.model.db.User;
import in.techbeat.palapa.model.request.RegisterRequest;
import in.techbeat.palapa.repository.RoleRepository;
import in.techbeat.palapa.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @RequestMapping(method = GET, value = "/ping")
    public ResponseEntity<String> handlePing() {
        log.debug("Responding to ping");
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @RequestMapping(method = POST, value = "/register")
    public User createUser(@RequestBody RegisterRequest input) {
        log.debug("Got registration request for name {} and email {}", input.getUsername(), input.getEmail());
        if (userRepository.findByUsername(input.getUsername()) != null) {
            log.warn("A user with name {} already exists in the DB", input.getUsername());
            throw new EntityAlreadyExistsException("Username already exists");
        }
        if (userRepository.findByEmail(input.getEmail()) != null) {
            log.warn("A user with email {} already exists in the DB", input.getEmail());
            throw new EntityAlreadyExistsException("Email ID already exists");
        }
        final User user = User.builder()
                .username(input.getUsername().trim())
                .email(input.getEmail().trim())
                .passwordHash(DigestUtils.sha1Hex(input.getPassword().trim()))
                .build();

        // TODO This is probably not at the right place
        Role userRole = roleRepository.findByName("USER");
        if (userRole == null) {
            userRole = Role.builder().name("USER").build();
            roleRepository.save(userRole);
        }
        user.hasRole(userRole);
        userRepository.save(user);
        log.debug(user.toString());
        return user;
    }
    @RequestMapping(method = GET, value = "/user/name/{username}")
    public User findByUsername(@PathVariable("username") String username) {
        log.debug("Got request for getUserByName for {}", username);
        final User user = userRepository.findByUsername(username.trim());
        if (user == null) {
            log.warn("No user with name {} exists in the DB", username);
            throw new NotFoundException("Requested user was not found");
        }
        return user;
    }

}
