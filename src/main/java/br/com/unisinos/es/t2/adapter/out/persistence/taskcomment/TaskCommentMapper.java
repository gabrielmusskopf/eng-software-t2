package br.com.unisinos.es.t2.adapter.out.persistence.taskcomment;

import br.com.unisinos.es.t2.application.domain.model.TaskComment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskCommentMapper {

    TaskCommentEntity toEntity(TaskComment comment);

    TaskComment toTaskComment(TaskCommentEntity entity);
}
