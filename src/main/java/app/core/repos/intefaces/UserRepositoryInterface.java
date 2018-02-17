package app.core.repos.intefaces;

import app.pojo.User;

import java.util.List;
import java.util.Set;

public interface UserRepositoryInterface {
    int add(User user);
    User findByUsername(String username);
    List<User> findByIds(Set<Integer> ids);
}
