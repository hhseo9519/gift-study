package gift.service;

import gift.dto.MemberRegisterRequestDto;
import gift.repository.MemberRepository;
import gift.security.JwtProvider;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // 테스트 순서 보장
class MemberServiceIntegrationTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    DataSource dataSource;

    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        //jdbcTemplate.execute("DELETE FROM members");
    }

    @Test
    @Order(1)
    void 회원가입_성공() {
        // given
        MemberRegisterRequestDto request = new MemberRegisterRequestDto("user1@test.com", "1234");

        // when
        String token = memberService.register(request);

        // then
        assertNotNull(token);
        assertTrue(token.startsWith("ey")); // jwt 형식 간단 검증
    }

    @Test
    @Order(2)
    void 회원가입_중복이메일_예외() {
        // given
        MemberRegisterRequestDto request = new MemberRegisterRequestDto("duplicate@test.com", "1234");
        memberService.register(request); // 최초 가입

        // when & then
        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> {
            memberService.register(request);
        });
        assertEquals("403 FORBIDDEN \"이미 등록된 이메일입니다.\"", e.getMessage());
    }

    @Test
    @Order(3)
    void 로그인_성공() {
        // given
        MemberRegisterRequestDto request = new MemberRegisterRequestDto("user2@test.com", "1234");
        memberService.register(request);

        // when
        String token = memberService.login(request);

        // then
        assertNotNull(token);
        assertTrue(token.contains("ey")); // jwt 확인
    }

    @Test
    @Order(4)
    void 로그인_이메일없음_예외() {
        // given
        MemberRegisterRequestDto request = new MemberRegisterRequestDto("notfound@test.com", "1234");

        // when & then
        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> {
            memberService.login(request);
        });
        assertEquals("403 FORBIDDEN \"이메일이 존재하지 않습니다.\"", e.getMessage());
    }

    @Test
    @Order(5)
    void 로그인_비밀번호틀림_예외() {
        // given
        MemberRegisterRequestDto saved = new MemberRegisterRequestDto("user3@test.com", "1234");
        memberService.register(saved);

        MemberRegisterRequestDto wrongPassword = new MemberRegisterRequestDto("user3@test.com", "wrong");

        // when & then
        ResponseStatusException e = assertThrows(ResponseStatusException.class, () -> {
            memberService.login(wrongPassword);
        });
        assertEquals("403 FORBIDDEN \"비밀번호가 일치하지 않습니다.\"", e.getMessage());
    }
}
