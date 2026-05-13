package br.com.unisinos.es.t2.adapter.in.web;

import br.com.unisinos.es.t2.application.domain.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface UserMapper {

    CreateUserResponse toCreateUserResponse(User user);

    GetUserResponse toUserResponse(User user);
}
