package gift.security;

import gift.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;


import java.nio.charset.StandardCharsets;

@Component
public class JwtProvider {

    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    private static final byte[] KEY_BYTES = SECRET_KEY.getBytes(StandardCharsets.UTF_8);

    /**
     * 회원 정보를 기반으로 JWT 토큰을 생성합니다.
     * @param member 토큰을 생성할 회원 엔티티
     * @return 발급된 JWT 토큰 문자열
     */
    public String createToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId() != null ? member.getId().toString() : member.getEmail())
                .claim("email", member.getEmail())
                .signWith(Keys.hmacShaKeyFor(KEY_BYTES))
                .compact();
    }



    }

