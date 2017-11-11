package in.techbeat.palapa.service;

import in.techbeat.palapa.exception.EntityAlreadyExistsException;
import in.techbeat.palapa.exception.NotFoundException;
import in.techbeat.palapa.model.db.Role;
import in.techbeat.palapa.model.db.User;
import in.techbeat.palapa.model.request.CreateUserRequest;
import in.techbeat.palapa.model.request.LoginRequest;
import in.techbeat.palapa.repository.RoleRepository;
import in.techbeat.palapa.repository.UserRepository;
import in.techbeat.palapa.util.JWTokenUtil;
import in.techbeat.palapa.validator.UserValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTokenUtil jwTokenUtil;

    public User createUser(final CreateUserRequest input) {
        if (userRepository.findByUsername(input.getUsername()) != null) {
            log.error("A user with name {} already exists in the DB", input.getUsername());
            throw new EntityAlreadyExistsException("Username already exists");
        }
        if (userRepository.findByEmail(input.getEmail()) != null) {
            log.error("A user with email {} already exists in the DB", input.getEmail());
            throw new EntityAlreadyExistsException("Email ID already exists");
        }
        final User user = User.fromUserRequest(input);
        // TODO Default role creation is probably not at the right place
        Role userRole = roleRepository.findByName("USER");
        if (userRole == null) {
            userRole = Role.builder().name("USER").build();
            roleRepository.save(userRole);
        }
        user.hasRole(userRole);
        userRepository.save(user);
        return user;
    }
    public User findByUsername(final String username) {
        log.debug("Got request for getUserByName for {}", username);
        final User user = userRepository.findByUsername(username.trim());
        if (user == null) {
            log.error("No user with name {} exists in the DB", username);
            throw new NotFoundException("Requested user was not found");
        }
        return user;
    }

    public String login(final LoginRequest input) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.getUsername(), input.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String jwToken = jwTokenUtil.generateToken((String) authentication.getPrincipal());
        log.debug("Generated JsonWebToken {} for session", jwToken);
        return jwToken;
    }
}
