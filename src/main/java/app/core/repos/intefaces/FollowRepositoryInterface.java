package app.core.repos.intefaces;

public interface FollowRepository {
    boolean follow(final int followerId, final int followedId);
    boolean unfollow(final int followerId, final int followedId);

}
