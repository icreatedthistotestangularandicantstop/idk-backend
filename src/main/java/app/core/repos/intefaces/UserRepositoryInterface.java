package app.core.repos.intefaces;

import app.http.pojos.UserCreateResource;
import app.http.pojos.UserUpdateResource;
import app.pojo.User;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.util.List;
import java.util.Set;

public interface UserRepositoryInterface {
    int add(UserCreateResource user);
    void update(UserUpdateResource user, int userId);
    User findByUsername(String username);
    User findById(int id);
    List<User> findByIds(Set<Integer> ids);
    void incrementFollowersFor(int userId);
    void decrementFollowersFor(int userId);
    void incrementFollowedFor(int userId);
    void decrementFollowedFor(int userId);

}
