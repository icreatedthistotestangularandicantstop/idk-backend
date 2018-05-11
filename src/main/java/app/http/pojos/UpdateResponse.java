package app.http.pojos;

import app.pojo.Tag;
import app.pojo.Update;
import app.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateResponse implements Serializable {
    private int id;
    private User user;
    private List<Tag> tags;
    private String content;
    private int userId;
    private int imageId;
    private int likes;
    private int commentsCount;
    private long createdAt;
    private boolean liked;
    private boolean favorited;

    public UpdateResponse(Update update) {
        this.setCommentsCount(update.getCommentsCount());
        this.setContent(update.getContent());
        this.setCreatedAt(update.getCreatedAt());
        this.setId(update.getId());
        this.setLikes(update.getLikes());
        this.setUserId(update.getUserId());
        this.setTags(new ArrayList<>());
    }

    public static UpdateResponse createFromUpdate(Update update) {
        return new UpdateResponse(update);
    }

}
