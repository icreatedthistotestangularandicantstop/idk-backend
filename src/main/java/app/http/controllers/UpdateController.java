package app.http.controllers;

import app.core.repos.UpdateRepository;
import app.http.pojos.CustomUserDetails;
import app.http.pojos.FavoriteResponse;
import app.http.pojos.Page;
import app.http.pojos.UpdateResource;
import app.pojo.Comment;
import app.pojo.Update;
import app.services.LikeService;
import app.services.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping(path = "/api/update")
public class UpdateController {
    @Autowired
    private UpdateRepository updateRepository;

    @Autowired
    private UpdateService updateService;

    @Autowired
    private LikeService likeService;

    @RequestMapping(method = RequestMethod.POST)
    public Update add(final @RequestBody @Valid UpdateResource updateData) {
        final Update update = updateService.addNew(updateData);

        return update;
    }


    @RequestMapping(path = "list", method = RequestMethod.GET)
    public List<Update> getUpdates(
            final @Valid Page page
    ) {
        return updateRepository.findPaged(page);
    }

    @RequestMapping(path = "/{updateId}", method = RequestMethod.GET)
    public Update getById(final @PathVariable(value = "updateId") int updateId) {
        return updateRepository.findById(updateId);
    }

    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET)
    public List<Update> getByUserId(final @PathVariable(value = "userId") int userId) {
        return updateRepository.findByUserId(userId);
    }

    @RequestMapping(path = "/favorite/{updateId}", method = RequestMethod.PUT)
    public FavoriteResponse favorite(
            final @PathVariable("updateId") int updateId,
            final @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return new FavoriteResponse(updateService.favorite(updateId, userDetails.getId()));
    }

    @RequestMapping(path = "/like/{updateId}", method = RequestMethod.PUT)
    public boolean like(
            final @PathVariable("updateId") int updateId,
            final @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return likeService.likeUpdate(updateId, userDetails.getId());
    }

    @RequestMapping(path = "/unlike/{updateId}", method = RequestMethod.PUT)
    public boolean unlike(
            final @PathVariable("updateId") int updateId,
            final @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return likeService.unlikeUpdate(updateId, userDetails.getId());
    }

}
