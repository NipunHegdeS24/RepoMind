package project.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

@Configuration 
public class CryptoConfig 
{
    // Encryptors.text (AES-CBC) is deprecated in favour of Encryptors.delux (AES-GCM),
    // but switching would make already-stored tokens undecryptable.
    @Bean
    @SuppressWarnings("deprecation")
    TextEncryptor textEncryptor(
        @Value("${app.token-encryptor-password}") String password,
        @Value("${app.token-encryptor-salt}") String salt)
    {
        return Encryptors.text(password, salt);
    }   
}
