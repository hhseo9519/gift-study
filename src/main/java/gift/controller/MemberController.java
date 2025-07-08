package gift.controller;

import gift.dto.MemberRequestDto;
import gift.dto.MemberResponseDto;
import gift.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    public final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
   //아무리 생성자를 통해 넣어준다고 해도 생성자가 존재하기는 해야한다
   //내가 이렇게 만들어놓은 생성자를 사용하여 spring이 DI를 해주는 것이다.

    @PostMapping("/register")
    public ResponseEntity<MemberResponseDto> registerMember(
            @RequestBody @Valid MemberRequestDto requestDto) {
        //@RequestBody는 JSON 요청 본문을 자바 객체로 변환하며,
        //JSON의 key 이름과 DTO의 필드명이 정확히 일치해야 한다.
        //@Valid가 있어야 DTO에 존재하는 @NOTBLANK 등의 유효성 검사가 실행된다.
        //없는 경우 실행되지 않음에 주의하자

        String token = memberService.register(requestDto); // 토큰 생성
        MemberResponseDto responseDto = new MemberResponseDto(token);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        //ResponseEntity에 넣은 ResponseDto에서 Getter가 존재하지 않는다면 JSON으로 변환되어 들어가지 않는다
        //또한 어노테이션을 통해 body에 넣지 않도록 만들수도 있다.
    }

    @PostMapping("/login")
    public ResponseEntity<MemberResponseDto> loginMember(
            @RequestBody @Valid MemberRequestDto requestDto) {

        String token = memberService.login(requestDto);
        MemberResponseDto responseDto = new MemberResponseDto(token);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }



}
