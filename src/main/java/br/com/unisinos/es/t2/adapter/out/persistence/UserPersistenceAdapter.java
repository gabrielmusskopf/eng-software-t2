package br.com.unisinos.es.t2.adapter.out.persistence;

import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.out.DeleteUserPort;
import br.com.unisinos.es.t2.application.port.out.ExistsUserByEmailPort;
import br.com.unisinos.es.t2.application.port.out.ExistsUserByIdPort;
import br.com.unisinos.es.t2.application.port.out.GetUserByEmailPort;
import br.com.unisinos.es.t2.application.port.out.GetUserByIdPort;
import br.com.unisinos.es.t2.application.port.out.SaveUserPort;
import br.com.unisinos.es.t2.application.port.out.UpdateUserPort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class UserPersistenceAdapter
        implements SaveUserPort,
                GetUserByIdPort,
                GetUserByEmailPort,
                UpdateUserPort,
                DeleteUserPort,
		ExistsUserByIdPort,
                ExistsUserByEmailPort {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        UserEntity savedUser = userRepository.save(userMapper.toEntity(user));
        user.setId(savedUser.getId());
        user.setCreatedAt(savedUser.getCreatedAt());
        user.setUpdatedAt(savedUser.getUpdatedAt());
        return user;
    }

    @Override
    public Optional<User> getById(String id) {
        return userRepository.findByIdAndDeletedFalse(id).map(userMapper::toDomain);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmailAndDeletedFalse(email).map(userMapper::toDomain);
    }

    @Override
    public void update(User user) {
        UserEntity updatedUser = userRepository.save(userMapper.toEntity(user));
        user.setUpdatedAt(updatedUser.getUpdatedAt());
    }

    @Override
    public void delete(String id) {
        userRepository.deleteById(id);
    }

	@Override
	public boolean exists(String id) {
		return userRepository.existsByIdAndDeletedFalse(id);
	}

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmailAndDeletedFalse(email);
    }

}
