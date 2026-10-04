package project.backend.services;

import java.util.UUID;

import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import project.backend.entity.User;
import project.backend.repository.UserRepository;

@Service
@RequiredArgsConstructor 
public class UserService 
{
    public final UserRepository userRepository;
    public final TextEncryptor textEncryptor;

    @Transactional(readOnly = true)
    public User requiredById(UUID id)
    {
        return userRepository.findById(id).orElseThrow(() -> 
            new RuntimeException("User not found"));
    }

    public String decryptAccessToken(User user)
    {
        return textEncryptor.decrypt(user.getAccessToken());
    }

    private static long toLong(Object value) 
    {
        if (value instanceof Number number) 
        {
            return number.longValue();
        } 
        return Long.parseLong(String.valueOf(value));
    }
}
