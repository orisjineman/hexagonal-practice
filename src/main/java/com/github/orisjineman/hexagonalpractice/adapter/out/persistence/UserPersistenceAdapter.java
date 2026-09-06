package com.github.orisjineman.hexagonalpractice.adapter.out.persistence;

import com.github.orisjineman.hexagonalpractice.application.port.out.UserRepository;
import com.github.orisjineman.hexagonalpractice.domain.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserPersistenceAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserPersistenceAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = new UserJpaEntity(user.getId(), user.getEmail(), user.getPassword());
        UserJpaEntity saved = userJpaRepository.save(entity);
        return new  User(saved.getId(), saved.getEmail(), saved.getPassword());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(entity -> new User(entity.getId(), entity.getEmail(), entity.getPassword()));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }
}
