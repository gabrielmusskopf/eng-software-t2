package br.com.unisinos.es.t2.application.domain.service;

import br.com.unisinos.es.t2.application.domain.model.User;
import br.com.unisinos.es.t2.application.port.in.CreateUserService.CreateUserCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface UserMapper {

    User toUser(CreateUserCommand command);
}
