package app.core.repos.intefaces;

import app.pojo.Favorite;
import app.pojo.Update;

import java.util.List;

public interface UpdateRepositoryInterface {
    int add(Update update);
    Update findById(int id);
    List<Update> findByUserId(int userId);
    Update findByIdAndUserId(int id, int userId);
    void addFavorite(int updateId, int userId);
    Favorite findFavoriteByUpdateIdAndUserId(int updateId, int userId);
    void incrementUpdateLikes(int updateId);
    void decrementUpdateLikes(int updateId);
    boolean exists(int id);

}
