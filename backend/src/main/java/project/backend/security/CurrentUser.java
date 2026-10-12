package project.backend.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import project.backend.exceptions.UnauthorizedException;

@Component 
public class CurrentUser {
    public AppUserPrincipal require()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AppUserPrincipal principal)) {
            throw new UnauthorizedException("User not authenticated");
        }
        return principal;
    }

}