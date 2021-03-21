package ba.unsa.etf.nwt.taskservice.controller;

import ba.unsa.etf.nwt.taskservice.dto.CommentDTO;
import ba.unsa.etf.nwt.taskservice.dto.MetadataDTO;
import ba.unsa.etf.nwt.taskservice.exception.base.ForbiddenException;
import ba.unsa.etf.nwt.taskservice.model.Comment;
import ba.unsa.etf.nwt.taskservice.model.Task;
import ba.unsa.etf.nwt.taskservice.request.CreateCommentRequest;
import ba.unsa.etf.nwt.taskservice.response.SimpleResponse;
import ba.unsa.etf.nwt.taskservice.response.base.PaginatedResponse;
import ba.unsa.etf.nwt.taskservice.response.base.Response;
import ba.unsa.etf.nwt.taskservice.security.ResourceOwner;
import ba.unsa.etf.nwt.taskservice.service.CommentService;
import ba.unsa.etf.nwt.taskservice.service.CommunicationService;
import ba.unsa.etf.nwt.taskservice.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tasks/{taskId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final CommunicationService communicationService;
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Response> create(ResourceOwner resourceOwner,
                                           @PathVariable UUID taskId,
                                           @RequestBody @Valid CreateCommentRequest request) {
        Task task = taskService.findById(taskId);
        communicationService.checkIfCollaborator(resourceOwner.getId(), task.getProjectId());
        Comment comment = commentService.create(request, task, resourceOwner.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new Response(new CommentDTO(comment)));
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse> getCommentsForTask(ResourceOwner resourceOwner,
                                                                @PathVariable UUID taskId,
                                                                Pageable pageable) {
        Task task = taskService.findById(taskId);
        communicationService.checkIfCollaborator(resourceOwner.getId(), task.getProjectId());
        Page<Comment> commentPage = commentService.getCommentsForTask(task, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new PaginatedResponse(new MetadataDTO(commentPage),
                        commentPage.getContent().stream().map(CommentDTO::new).collect(Collectors.toList())));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Response> delete(ResourceOwner resourceOwner,
                                           @PathVariable UUID taskId,
                                           @PathVariable UUID commentId) {
        Comment comment = commentService.findByIdAndTaskId(commentId, taskId);
        if(!comment.getUserId().equals(resourceOwner.getId())) {
            throw new ForbiddenException("You don't have permission for this action");
        }
        commentService.delete(comment);
        return ResponseEntity.status(HttpStatus.OK).body(new Response(new SimpleResponse("Comment successfully deleted")));
    }
}
