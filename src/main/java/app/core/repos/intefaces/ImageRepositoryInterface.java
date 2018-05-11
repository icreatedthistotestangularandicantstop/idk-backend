package app.core.repos.intefaces;

import app.pojo.Image;

public interface ImageRepositoryInterface {
    int add(Image user);
    Image findById(int id);
}
