package in.techbeat.palapa.configuration.filter;

import in.techbeat.palapa.exception.ForbiddenException;
import in.techbeat.palapa.model.db.User;
import in.techbeat.palapa.repository.UserRepository;
import in.techbeat.palapa.util.JWTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JWTokenUtil jwTokenUtil;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Get the user's token
        final String tokenHeaderValue = request.getHeader(tokenHeader);
        final String token;
        if (tokenHeaderValue != null && tokenHeaderValue.startsWith(JWTokenUtil.JWT_TOKEN_HEADER_PREFIX)) {
            token = tokenHeaderValue.substring(JWTokenUtil.JWT_TOKEN_HEADER_PREFIX.length());
            logger.debug("Extracted token " + token);
            try {
                final String username = jwTokenUtil.getUsernameFromToken(token);
                logger.info("Successfully decrypted JWT for user " + username);
                final User user = userRepository.findByUsername(username);
                final List<GrantedAuthority> authorities = user.getRoles().stream().map(
                        u -> new SimpleGrantedAuthority(u.getName())).collect(Collectors.toList());
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username,
                        user.getPasswordHash(), authorities));
            } catch (IllegalArgumentException iae) {
                throw new ForbiddenException("Invalid authentication token");
            }
        } else {
            // Don't throw up here because we also have some APIs that should be accessible unauthenticated
            logger.warn("Couldn't extract the JWT from Authorization header");
        }
        filterChain.doFilter(request, response);
    }
}