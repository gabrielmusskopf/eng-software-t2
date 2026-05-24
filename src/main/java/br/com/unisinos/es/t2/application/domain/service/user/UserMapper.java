package br.com.unisinos.es.t2.application.domain.service.user;

import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.in.user.CreateUserService.CreateUserCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface UserMapper {

    User toUser(CreateUserCommand command);
}
