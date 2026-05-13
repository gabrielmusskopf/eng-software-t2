package br.com.unisinos.es.t2.adapter.out.persistence;

import br.com.unisinos.es.t2.application.domain.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface UserMapper {

    UserEntity toEntity(User user);

    User toDomain(UserEntity userEntity);
}
