package app.services;

import app.core.repos.CommentRepository;
import app.core.repos.LikeRepository;
import app.core.repos.TagRepository;
import app.core.repos.UpdateRepository;
import app.http.pojos.CommentResource;
import app.http.pojos.CommentResponse;
import app.http.pojos.Page;
import app.pojo.Comment;
import app.pojo.Like;
import app.pojo.Tag;
import app.pojo.UpdateTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class TagService {

    private final TagRepository tagRepository;

    TagService(final TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> addTags(final Set<String> tags) {
        final List<Tag> added = tagRepository.findByName(tags);
        final Set<String> toAdd = new HashSet<>();
        for (String tagName : tags) {
            if (!contains(added, tagName)) {
                toAdd.add(tagName);
            }
        }
        final List<Tag> addedTags = tagRepository.addTags(toAdd);
        added.addAll(addedTags);

        return added;
    }

    public void addUpdateTagLink(final List<Tag> tags, final int updateId) {
        final List<UpdateTag> currentUpdateTags = tagRepository.findUpdateTagsByUpdateId(updateId);
        final Set<Integer> toAddTagIds = getTagIdsToAdd(tags, currentUpdateTags);
        tagRepository.addUpdateTags(toAddTagIds, updateId);
    }

    private Set<Integer> getTagIdsToAdd(final List<Tag> tags, final List<UpdateTag> updateTags) {
        final Set<Integer> toAdd = new HashSet<>();
        for (Tag tag : tags) {
            if (!containsId(updateTags, tag.getId())) {
                toAdd.add(tag.getId());
            }
        }

        return toAdd;
    }

    private boolean containsId(final List<UpdateTag> updateTags, final int tagId) {
        for (UpdateTag updateTag : updateTags) {
            if (updateTag.getTagId() == tagId) {
                return true;
            }
        }

        return false;
    }

    private Set<Integer> getTagIds(final List<Tag> tags) {
        final Set<Integer> tagIds = new HashSet<>();
        for (Tag tag : tags) {
            tagIds.add(tag.getId());
        }

        return tagIds;
    }

    private boolean contains(final List<Tag> tags, final String tagName) {
        for (Tag tag : tags) {
            if (tag.getName().equals(tagName)) {
                return true;
            }
        }

        return false;
    }

}
