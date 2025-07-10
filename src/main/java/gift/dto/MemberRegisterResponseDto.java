package gift.dto;

public class MemberRegisterResponseDto {

    private String token;

    // 생성자
    public MemberRegisterResponseDto() {
    }

    public MemberRegisterResponseDto(String token) {
        this.token = token;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
