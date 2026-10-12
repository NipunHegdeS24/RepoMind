package project.backend.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.backend.dto.UserResponse;
import project.backend.entity.User;
import project.backend.security.AppUserPrincipal;
import project.backend.security.CurrentUser;

@RestController 
@RequestMapping ("/api/auth")
@RequiredArgsConstructor 
public class AuthController 
{
    private final CurrentUser currentUser;


    // Returns the Spring Security OAuth2 URL to initiate GitHub login.
    @GetMapping("/login-url")
    public Map<String, String> loginUrl()
    {
        return Map.of("url", "/oauth2/authorization/github");
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me()
    {
        AppUserPrincipal principal = currentUser.require();
        User user = principal.getUser();
        return ResponseEntity.ok(new UserResponse(
            user.getId(),
            user.getGithubId(),
            user.getGithubUsername(),
            user.getDisplayName(),
            user.getAvatarUrl()
        ));
    }
}
