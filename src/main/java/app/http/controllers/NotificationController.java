package app.http.controllers;

import app.http.pojos.CustomUserDetails;
import app.http.pojos.NotificationInfoResponse;
import app.http.pojos.NotificationResponse;
import app.http.pojos.Page;
import app.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@SuppressWarnings("unused")
@RestController
@RequestMapping(path = "/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    NotificationController(final NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/{notificationId}/see", method = RequestMethod.POST)
    public void see(
            final @PathVariable int notificationId,
            final @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        final int loggedUserId = userDetails.getId();
        notificationService.see(notificationId, loggedUserId);
    }

    @RequestMapping(path = "/info", method = RequestMethod.GET)
    public NotificationInfoResponse info(final @AuthenticationPrincipal CustomUserDetails userDetails) {
        final int loggedUserId = userDetails.getId();
        final NotificationInfoResponse response = notificationService.getInfo(loggedUserId);

        return response;
    }

    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public List<NotificationResponse> list(
            final @Valid Page page,
            final @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        final int loggedUserId = userDetails.getId();
        final List<NotificationResponse> response = notificationService.getPaged(loggedUserId, page);

        return response;
    }

}
