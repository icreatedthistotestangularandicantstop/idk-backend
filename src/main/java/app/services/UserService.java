package app.services;

import app.core.repos.ImageRepository;
import app.core.repos.UserRepository;
import app.pojo.Image;
import app.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final ImageRepository imageRepository;

    @Autowired
    UserService(final UserRepository userRepository, final ImageRepository imageRepository) {
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    public User findById(final int userId) {
        final User user = userRepository.findById(userId);
        final Image image = imageRepository.findForUser(userId);
        user.setImage(image);

        return user;
    }

}
