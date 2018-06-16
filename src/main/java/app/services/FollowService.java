package app.services;

import app.core.repos.FollowRepository;
import app.core.repos.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("unused")
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    FollowService(
            final FollowRepository followRepository,
            final UserRepository userRepository
    ) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public boolean follow(final int followerId, final int followedId) {
        if (followedId == followerId) {
            return false;
        }
        userRepository.incrementFollowedFor(followerId);
        userRepository.incrementFollowersFor(followedId);

        return followRepository.follow(followerId, followedId);
    }

    @Transactional
    public boolean unfollow(final int followerId, final int followedId) {
        if (followedId == followerId) {
            return false;
        }
        userRepository.decrementFollowedFor(followerId);
        userRepository.decrementFollowersFor(followedId);

        return followRepository.unfollow(followerId, followedId);
    }

}
