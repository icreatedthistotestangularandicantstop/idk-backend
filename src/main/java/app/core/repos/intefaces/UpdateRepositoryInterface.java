package app.core.repos.intefaces;

import app.http.pojos.Page;
import app.pojo.Favorite;
import app.pojo.Update;

import java.util.List;

public interface UpdateRepositoryInterface {
    int add(Update update);
    List<Update> findPaged(Page page);
    List<Update> findPagedByUserId(Page page, Integer targetUserId);
    List<Update> findPagedByTag(Page page, String tag);
    Update findById(int id);
    List<Update> findByUserId(int userId);
    Update findByIdAndUserId(int id, int userId);
    void addFavorite(int updateId, int userId);
    Favorite findFavoriteByUpdateIdAndUserId(int updateId, int userId);
    void incrementUpdateComments(int updateId);
    void incrementUpdateLikes(int updateId);
    void decrementUpdateLikes(int updateId);
    boolean exists(int id);

}
