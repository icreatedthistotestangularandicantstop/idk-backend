package app.services;

import app.core.repos.UpdateRepository;
import app.http.pojos.UpdateResource;
import app.pojo.Favorite;
import app.pojo.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UpdateService {
    @Autowired
    private UpdateRepository updateRepository;

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
