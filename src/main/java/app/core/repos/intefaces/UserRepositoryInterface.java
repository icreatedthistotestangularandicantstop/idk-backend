package app.core.repos.intefaces;

import app.pojo.User;

import java.util.List;

public interface UserRepositoryInterface {
    int add(User user);
    User findByUsername(String username);
}
