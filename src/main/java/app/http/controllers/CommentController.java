package app.http.controllers;

import app.core.repos.CommentRepository;
import app.http.pojos.CommentResource;
import app.http.pojos.CommentResponse;
import app.http.pojos.CustomUserDetails;
import app.http.pojos.Page;
import app.pojo.Comment;
import app.services.CommentService;
import app.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@SuppressWarnings("unused")
@RestController
@RequestMapping(path = "/api/comment")
public class CommentController {

    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private final LikeService likeService;

    @Autowired
    CommentController(
            final CommentRepository commentRepository,
            final CommentService commentService,
            final LikeService likeService
    ) {
        this.commentRepository = commentRepository;
        this.commentService = commentService;
        this.likeService = likeService;
    }

    @RequestMapping(path = "list/{updateId}", method = RequestMethod.GET)
    public List<CommentResponse> getComments(
            final @Valid Page page,
            final @PathVariable int updateId,
            final @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return commentService.findByUpdateIdPaged(page, updateId, userDetails == null ? null : userDetails.getId());
    }

    @RequestMapping(method = RequestMethod.POST)
    public CommentResponse add(
            final @RequestBody @Valid CommentResource commentData,
            final @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        final int loggedUserId = userDetails.getId();
        final Comment comment = commentService.addNew(commentData, loggedUserId);
        final CommentResponse response = commentService.findById(comment.getId(), loggedUserId);

        return response;
    }

    @RequestMapping(path = "/{id}")
    public Comment getById(final @PathVariable(value = "id") int id) {
        return commentRepository.findById(id);
    }

    @RequestMapping(path = "/update/{updateId}")
    public List<Comment> getByUpdateId(final @PathVariable(value = "updateId") int updateId) {
        return commentRepository.findByUpdateId(updateId);
    }

    @RequestMapping(path = "/like/{commentId}", method = RequestMethod.PUT)
    public boolean like(
            final @PathVariable("commentId") int commentId,
            final @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return likeService.likeComment(commentId, userDetails.getId());
    }

    @RequestMapping(path = "/unlike/{commentId}", method = RequestMethod.PUT)
    public boolean unlike(
            final @PathVariable("commentId") int commentId,
            final @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return likeService.unlikeComment(commentId, userDetails.getId());
    }

}
