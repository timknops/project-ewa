package nl.solar.app.security;

import io.jsonwebtoken.*;
import lombok.Data;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Data
public class JWToken {
    //TODO add comments, reformat code and remove unnecessary imports

    private static final String JWT_CALLNAME_CLAIM = "sub";
    private static final String JWT_USERID_CLAIM = "id";
    private static final String JWT_ROLE_CLAIM = "role";

    private String callName;
    private Long userId;
    private String role;

    public JWToken(String callName, Long userId, String role) {
        this.callName = callName;
        this.userId = userId;
        this.role = role;
    }

    public String encode(String issuer, String passphrase, int expiration){
        Key key = getKey(passphrase);

        return Jwts.builder().claim(JWT_CALLNAME_CLAIM, this.callName).claim(JWT_USERID_CLAIM, this.userId)
                .setIssuer(issuer).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    private static Key getKey(String pass){
        byte[] hmacKey = pass.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(hmacKey, SignatureAlgorithm.HS512.getJcaName());
    }

    private static JWToken decode(String token, String pass){
        Key key = getKey(pass);
        Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        Claims claims = jws.getBody();

        return new JWToken(claims.get(JWT_CALLNAME_CLAIM).toString(),
                Long.valueOf(claims.get(JWT_USERID_CLAIM).toString()),
                claims.get(JWT_ROLE_CLAIM).toString());
    }
}
