package br.com.unisinos.es.t2.application.domain.service.user;

import br.com.unisinos.es.t2.application.domain.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface UserMapper {

    User toUser(String name, String email, String password);
}
