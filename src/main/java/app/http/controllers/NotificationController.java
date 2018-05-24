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

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/notification")
public class NotificationController {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

//    @RequestMapping(path = "list", method = RequestMethod.GET)
//    public List<CommentResponse> list(
//            final @Valid Page page,
//            final @AuthenticationPrincipal CustomUserDetails userDetails
//    ) {
//        return commentService.findByUpdateIdPaged(page, updateId, userDetails == null ? null : userDetails.getId());
//    }

//    @RequestMapping(path = "/see", method = RequestMethod.POST)
//    public CommentResponse see(
//            final @RequestBody @Valid CommentResource commentData,
//            final @AuthenticationPrincipal CustomUserDetails userDetails
//    ) {
//        final int loggedUserId = userDetails.getId();
//        final Comment comment = commentService.addNew(commentData, loggedUserId);
//        final CommentResponse response = commentService.findById(comment.getId(), loggedUserId);
//
//        return response;
//    }

}
