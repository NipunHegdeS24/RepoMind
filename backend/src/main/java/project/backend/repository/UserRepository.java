package project.backend.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.backend.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> 
{
    Optional<User> findByGithubId(Long githubId);
    Optional<User> findByGithubUsername(String githubUsername);
    
}
