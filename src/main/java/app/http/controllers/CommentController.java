package app.http.controllers;

import app.core.repos.CommentRepository;
import app.http.pojos.CommentResource;
import app.http.pojos.CustomUserDetails;
import app.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/comment")
public class CommentController {
    @Autowired
    private CommentRepository commentRepository;

    @RequestMapping(method = RequestMethod.POST)
    public Comment add(
            final @RequestBody @Valid CommentResource commentData,
            final @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        final Comment comment = new Comment();
        comment.setContent(commentData.getContent());
        comment.setUpdateId(commentData.getUpdateId());
        comment.setUserId(userDetails.getId());

        int newCommentId = commentRepository.add(comment);
        comment.setId(newCommentId);

        return comment;
    }

    @RequestMapping(path = "/{id}")
    public Comment getById(final @PathVariable(value = "id") int id) {
        return commentRepository.findById(id);
    }

    @RequestMapping(path = "/update/{updateId}")
    public List<Comment> getByUpdateId(final @PathVariable(value = "updateId") int updateId) {
        return commentRepository.findByUpdateId(updateId);
    }

}
