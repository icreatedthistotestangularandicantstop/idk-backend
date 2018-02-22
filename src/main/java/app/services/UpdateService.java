package app.services;

import app.core.repos.LikeRepository;
import app.core.repos.TagRepository;
import app.core.repos.UpdateRepository;
import app.core.repos.UserRepository;
import app.http.pojos.Page;
import app.http.pojos.UpdateResource;
import app.http.pojos.UpdateResponse;
import app.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;

@Component
public class UpdateService {
    @Autowired
    private UpdateRepository updateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagService tagService;

    public List<UpdateResponse> findPaged(Page page, Integer userId) {
        final List<Update> updates = updateRepository.findPaged(page);
        final Set<Integer> likedUpdateIds = getLikedUpdates(updates, userId);
        final Map<Integer, User> users = getUserUpdateOwners(updates);
        final Map<Integer, List<Tag>> updateTags = getUpdateTagsFromUpdates(updates);

        final List<UpdateResponse> response = new ArrayList<>();
        for (Update update : updates) {
            final UpdateResponse item = UpdateResponse.createFromUpdate(update);
            item.setLiked(likedUpdateIds.contains(update.getId()));
            item.setUser(users.get(update.getUserId()));
            item.setTags(updateTags.get(update.getId()));

            response.add(item);
        }

        return response;
    }

    private Map<Integer, List<Tag>> getUpdateTagsFromUpdates(final List<Update> updates) {
        final Map<Integer, List<Tag>> result = new HashMap<>();
        final List<Tag> tags = tagRepository.findUpdateTagsByUpdateIds(getUpdateIds(updates));

        for (Tag tag : tags) {
            List<Tag> l = result.get(tag.getUpdateId());
            if (null == l) {
                l = new ArrayList<>();
                result.put(tag.getUpdateId(), l);
            }
            l.add(tag);
        }

        return result;
    }

    private Map<Integer, User> getUserUpdateOwners(List<Update> updates) {
        final Map<Integer, User> result = new HashMap<>();
        final Set<Integer> userIds = getUserIds(updates);
        System.out.println(userIds);
        final List<User> users = userRepository.findByIds(userIds);
        for (User user : users) {
            result.put(user.getId(), user);
        }

        return result;
    }

    private Set<Integer> getUserIds(List<Update> updates) {
        final Set<Integer> userIds = new HashSet<>();
        for (Update update : updates) {
            userIds.add(update.getUserId());
        }

        return userIds;
    }

    private Set<Integer> getLikedUpdates(List<Update> updates, Integer userId) {
        List<Like> updateLikes = Collections.EMPTY_LIST;
        if (userId != null) {
            updateLikes = likeRepository.findUpdateLikesByIds(getUpdateIds(updates), userId);
        }
        Set<Integer> likedUpdateIds = new HashSet<>();
        for (Like like : updateLikes) {
            likedUpdateIds.add(like.getItemId());
        }

        return likedUpdateIds;
    }

    private Set<Integer> getUpdateIds(List<Update> updates) {
        final Set<Integer> result = new HashSet<>();
        for (Update update : updates) {
            result.add(update.getId());
        }

        return result;
    }

    @Transactional
    public Update addNew(UpdateResource updateResource) {
        final Update update = new Update();
        update.setContent(updateResource.getContent());
        update.setUserId(updateResource.getUserId());

        final int newUpdateId = updateRepository.add(update);
        update.setId(newUpdateId);
        addUpdateTags(update);

        return update;
    }

    private void addUpdateTags(final Update update) {
        final Set<String> tagNames = getTagsFromContent(update.getContent());
        final List<Tag> tags = tagService.addTags(tagNames);
        tagService.addUpdateTagLink(tags, update.getId());
    }

    private Set<String> getTagsFromContent(final String content) {
        final Set<String> tags = new HashSet<>();
        final char[] c = content.toCharArray();
        String tag = "";
        for (int i = 0; i < c.length; ) {
            if (Pattern.matches("\\s", "" + c[i])) {
                if (i < c.length - 1 && '#' == c[i + 1]) {
                    int j;
                    for (j = i + 2; j < c.length && '#' != c[j] && !Pattern.matches("\\s", "" + c[j]); j++) {
                        tag += c[j];
                    }
                    i = j;
                    tags.add(tag);
                    tag = "";
                } else {
                    i++;
                }
            } else {
                i++;
            }
        }

        return tags;
    }

    @Transactional
    public boolean favorite(int updateId, int userId) {
        if (!checkIfUserOwnsUpdate(updateId, userId)) {
            return false;
        }
        final boolean addedFavorite = favoriteUpdate(updateId, userId);
        if (addedFavorite) {
            incrementFavorites(updateId);

            return true;
        } else {
            return false;
        }
    }

    public boolean checkIfUserOwnsUpdate(int updateId, int userId) {
        final Update update = updateRepository.findByIdAndUserId(updateId, userId);
        if (update == null) {
            return false;
        }

        return true;
    }

    private void incrementFavorites(int updateId) {
        updateRepository.updateFavorites(updateId);
    }

    private boolean favoriteUpdate(int updateId, int userId) {
        if (hasUserFavoritedUpdate(updateId, userId)) {
            return false;
        }
        addFavoriteLink(updateId, userId);

        return true;
    }

    public boolean hasUserFavoritedUpdate(int updateId, int userId) {
        final Favorite favorite = updateRepository.findFavoriteByUpdateIdAndUserId(updateId, userId);
        if (favorite == null) {
            return false;
        }

        return true;
    }

    private void addFavoriteLink(int updateId, int userId) {
        updateRepository.addFavorite(updateId, userId);
    }

}
