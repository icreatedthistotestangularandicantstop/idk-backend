package app.core.repos.intefaces;

import java.util.List;

public interface FollowRepositoryInterface {
    boolean follow(final int followerId, final int followedId);
    boolean unfollow(final int followerId, final int followedId);
    boolean isFollowd(final int followerId, final int followedId);
    List<Integer> getFollowersOf(final int userId);

}
