package br.com.unisinos.es.t2.application.domain.service;

import br.com.unisinos.es.t2.application.domain.model.Task;
import br.com.unisinos.es.t2.application.port.in.CreateTaskService.CreateTaskCommand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface TaskMapper {

    Task toTask(CreateTaskCommand command);
}
