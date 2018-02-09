package app.core.repos.intefaces;

import app.pojo.Update;

import java.util.List;

public interface UpdateRepositoryInterface {
    int add(Update update);
    Update findById(int id);
    List<Update> findByUserId(int userId);
}
