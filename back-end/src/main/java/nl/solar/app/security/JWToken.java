package nl.solar.app.security;

import io.jsonwebtoken.*;
import lombok.Data;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Data
public class JWToken {

    private static final String JWT_CALLNAME_CLAIM = "sub";
    private static final String JWT_USERID_CLAIM = "id";
    private static final String JWT_TYPE_CLAIM = "type";
    private static final String JWT_ISSUER_CLAIM = "iss";
    public static final String JWT_ATTRIBUTE_NAME = "JWTokenInfo";

    private String callName;
    private Long userId;
    private String type;

    public JWToken(String callName, Long userId, String type) {
        this.callName = callName;
        this.userId = userId;
        this.type = type;
    }

    /**
     * Method to encode a JWToken
     * @param issuer the issuer
     * @param passphrase passphrase
     * @param expiration amount of time that the token is valid
     * @return returns the token, but secured
     */
    public String encode(String issuer, String passphrase, int expiration){
        Key key = getKey(passphrase);

        return Jwts.builder()
                .claim(JWT_CALLNAME_CLAIM, this.callName)
                .claim(JWT_USERID_CLAIM, this.userId)
                .claim(JWT_TYPE_CLAIM, this.type)
                .setIssuer(issuer).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    //Key used when encoding, uses HS512
    private static Key getKey(String pass){
        byte[] hmacKey = pass.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(hmacKey, SignatureAlgorithm.HS512.getJcaName());
    }

    /**
     * Method to decode the token back to its original form. Throws different exceptions based on what went wrong
     * @param token the encoded token
     * @param issuer the issuer defined in application.properties
     * @param pass passphrase defined in application.properties
     * @return returns a decoded JWToken
     */
    public static JWToken decode(String token, String issuer, String pass)
            throws ExpiredJwtException, MalformedJwtException{
        Key key = getKey(pass);
        Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        Claims claims = jws.getBody();

        //check if issuer is valid
        if (!claims.get(JWT_ISSUER_CLAIM).toString().equals(issuer)){
            throw new MalformedJwtException("Issuer is invalid");
        }

        return new JWToken(claims.get(JWT_CALLNAME_CLAIM).toString(),
                Long.valueOf(claims.get(JWT_USERID_CLAIM).toString()),
                claims.get(JWT_TYPE_CLAIM).toString());
    }
}
