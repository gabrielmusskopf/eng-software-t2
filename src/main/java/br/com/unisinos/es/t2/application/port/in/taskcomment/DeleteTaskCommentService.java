package br.com.unisinos.es.t2.application.port.in.taskcomment;

public interface DeleteTaskCommentService {

    void deleteComment(DeleteTaskCommentCommand command);

    record DeleteTaskCommentCommand(String taskId, String commentId) {}
}
