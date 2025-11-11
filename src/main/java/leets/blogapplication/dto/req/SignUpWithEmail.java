package leets.blogapplication.dto.req;

import java.time.LocalDate;

public class SignUpWithEmail {
    private String email;
    private String name;
    private String password;
    private LocalDate birthDate;
    private String nickname;
    private String intro;
    private String profileImage;

    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getPassword() { return password; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getNickname() { return nickname; }
    public String getIntro() { return intro; }
    public String getProfileImage() { return profileImage; }
}
