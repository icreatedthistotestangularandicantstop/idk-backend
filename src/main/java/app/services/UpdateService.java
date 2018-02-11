package app.services;

import app.core.repos.LikeRepository;
import app.core.repos.UpdateRepository;
import app.http.pojos.Page;
import app.http.pojos.UpdateResource;
import app.http.pojos.UpdateResponse;
import app.pojo.Favorite;
import app.pojo.Like;
import app.pojo.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class UpdateService {
    @Autowired
    private UpdateRepository updateRepository;

    @Autowired
    private LikeRepository likeRepository;

    public List<UpdateResponse> findPaged(Page page, Integer userId) {
        List<Update> updates = updateRepository.findPaged(page);
        Set<Integer> likedUpdateIds = getLikedUpdates(updates, userId);

        List<UpdateResponse> response = new ArrayList<>();
        for (Update update : updates) {
            final UpdateResponse item = UpdateResponse.createFromUpdate(update);
            item.setLiked(likedUpdateIds.contains(update.getId()));
            response.add(item);
        }

        return response;
    }

    private Set<Integer> getLikedUpdates(List<Update> updates, Integer userId) {
        List<Like> updateLikes = Collections.EMPTY_LIST;
        if (userId != null) {
            updateLikes = likeRepository.findUpdateLikesByIds(getUpdateIds(updates), userId);
        }
        Set<Integer> likedUpdateIds = new HashSet<>();
        for (Like like : updateLikes) {
            likedUpdateIds.add(like.getItemId());
        }

        return likedUpdateIds;
    }

    private Set<Integer> getUpdateIds(List<Update> updates) {
        Set<Integer> result = new HashSet<>();
        for (Update update : updates) {
            result.add(update.getId());
        }

        return result;
    }

    public Update addNew(UpdateResource updateResource) {
        Update update = new Update();
        update.setContent(updateResource.getContent());
        update.setUserId(updateResource.getUserId());

        int newUpdateId = updateRepository.add(update);
        update.setId(newUpdateId);

        return update;
    }

    @Transactional
    public boolean favorite(int updateId, int userId) {
        if (!checkIfUserOwnsUpdate(updateId, userId)) {
            return false;
        }
        final boolean addedFavorite = favoriteUpdate(updateId, userId);
        if (addedFavorite) {
            incrementFavorites(updateId);

            return true;
        } else {
            return false;
        }
    }

    public boolean checkIfUserOwnsUpdate(int updateId, int userId) {
        final Update update = updateRepository.findByIdAndUserId(updateId, userId);
        if (update == null) {
            return false;
        }

        return true;
    }

    private void incrementFavorites(int updateId) {
        updateRepository.updateFavorites(updateId);
    }

    private boolean favoriteUpdate(int updateId, int userId) {
        if (hasUserFavoritedUpdate(updateId, userId)) {
            return false;
        }
        addFavoriteLink(updateId, userId);

        return true;
    }

    public boolean hasUserFavoritedUpdate(int updateId, int userId) {
        final Favorite favorite = updateRepository.findFavoriteByUpdateIdAndUserId(updateId, userId);
        if (favorite == null) {
            return false;
        }

        return true;
    }

    private void addFavoriteLink(int updateId, int userId) {
        updateRepository.addFavorite(updateId, userId);
    }

}
