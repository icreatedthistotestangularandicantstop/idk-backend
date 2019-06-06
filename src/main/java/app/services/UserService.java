package app.services;

import app.core.repos.FollowRepository;
import app.core.repos.ImageRepository;
import app.core.repos.UserRepository;
import app.http.pojos.UserCreateResource;
import app.http.pojos.UserUpdateResource;
import app.pojo.Image;
import app.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final FollowRepository followRepository;

    @Autowired
    UserService(
            final UserRepository userRepository,
            final ImageRepository imageRepository,
            final FollowRepository followRepository
    ) {
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
        this.followRepository = followRepository;
    }

    public User findById(final int userId, final int loggedUserId) {
        final User user = userRepository.findById(userId);
        final Image image = imageRepository.findForUser(userId);
        user.setImage(image);
        user.setFollowed(loggedUserId == userId ? false : followRepository.isFollowd(loggedUserId, userId));

        return user;
    }

    public void upate(final UserUpdateResource user, final int userId) {
        userRepository.update(user, userId);
    }

    public long create(final UserCreateResource user) {
        return userRepository.add(user);
    }

    public List<User> findTopPopular() {
        return userRepository.findMostPopular();
    }

}
