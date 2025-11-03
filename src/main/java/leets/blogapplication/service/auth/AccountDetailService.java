package leets.blogapplication.service.auth;

import leets.blogapplication.domain.User;
import leets.blogapplication.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public AccountDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException((email)));
    }
}