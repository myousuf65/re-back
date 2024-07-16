//For RestAPI Auth
package com.hkmci.csdkms.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.stereotype.Component;
import java.util.Date;

import org.springframework.security.ldap.userdetails.LdapUserDetails;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {

/*Before Add CustomUserDetails for 2 factor auth    	
    	LdapUserDetailsImpl userPrincipal = (LdapUserDetailsImpl) authentication.getPrincipal();  
*/
    	LdapUserDetails userPrincipal = (LdapUserDetails) authentication.getPrincipal();

    	return this.generateJWT(userPrincipal.getUsername());
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
//
//        return Jwts.builder()
//                .setSubject(userPrincipal.getUsername())
//                .setIssuedAt(new Date())
//                .setExpiration(expiryDate)
//                .signWith(SignatureAlgorithm.HS512, jwtSecret)
//                .compact();
    }

    public String generateTokenDP(String username) {

    	return this.generateJWT(username);
    }
    
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

       // public boolean validateToken(String authToken) {
        public Integer validateToken(String authToken) {
            logger.info("Start JWT parse....");
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            // return true;
            return 0;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
            return HttpStatus.UNAUTHORIZED.value();
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
            return HttpStatus.UNAUTHORIZED.value();
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
            return 440;
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
            return HttpStatus.UNAUTHORIZED.value();
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
            return HttpStatus.UNAUTHORIZED.value();
        }
        // return false;
    }
    // public boolean validateToken(String authToken) {
    //     logger.info("Start JWT parse....");
    //     try {
    //         Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
    //         return true;
    //     } catch (SignatureException ex) {
    //         logger.error("Invalid JWT signature");
    //     } catch (MalformedJwtException ex) {
    //         logger.error("Invalid JWT token");
    //     } catch (ExpiredJwtException ex) {
    //         logger.error("Expired JWT token");
    //     } catch (UnsupportedJwtException ex) {
    //         logger.error("Unsupported JWT token");
    //     } catch (IllegalArgumentException ex) {
    //         logger.error("JWT claims string is empty.");
    //     }
    //     return false;
    // }
    
    private String generateJWT(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    	
    }
}