package app.http.controllers;

import app.http.pojos.CustomUserDetails;
import app.http.pojos.NotificationResponse;
import app.pojo.Comment;
import app.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/notification")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

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

    @RequestMapping(path = "/info", method = RequestMethod.GET)
    public NotificationResponse info(final @AuthenticationPrincipal CustomUserDetails userDetails) {
        final int loggedUserId = userDetails.getId();
        final NotificationResponse response = notificationService.getInfo(loggedUserId);

        return response;
    }

}
