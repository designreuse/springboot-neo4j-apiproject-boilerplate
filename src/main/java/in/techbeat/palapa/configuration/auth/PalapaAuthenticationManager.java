package in.techbeat.palapa.configuration.auth;

import in.techbeat.palapa.exception.ForbiddenException;
import in.techbeat.palapa.model.db.User;
import in.techbeat.palapa.repository.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PalapaAuthenticationManager implements AuthenticationManager {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = ((String) authentication.getPrincipal()).trim();
        final String password = ((String) authentication.getCredentials()).trim();
        final User user = userRepository.findByUsernameAndPasswordHash(username, DigestUtils.sha1Hex(password));
        if (user != null) {
            final List<GrantedAuthority> authorities = user.getRoles().stream().map(u -> new SimpleGrantedAuthority(u.getName())).collect(Collectors.toList());
            return new UsernamePasswordAuthenticationToken(username, user.getPasswordHash(), authorities);
        } else {
            throw new ForbiddenException("Invalid login credentials");
        }
    }
}