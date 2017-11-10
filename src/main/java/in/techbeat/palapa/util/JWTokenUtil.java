package in.techbeat.palapa.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class JWTokenUtil {

    public static final String JWT_TOKEN_HEADER_PREFIX = "Bearer ";

    @Value("${jwt.key}")
    private String jwtSigningKey;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String generateToken(String username) {
        final String compactJws = Jwts.builder().
                setSubject(username).
                signWith(SignatureAlgorithm.HS512, jwtSigningKey).
                compact();
        return compactJws;
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSigningKey)
                .parseClaimsJws(token)
                .getBody();
    }
}