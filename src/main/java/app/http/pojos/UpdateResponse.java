package app.http.pojos;

import app.pojo.Update;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateResponse implements Serializable {
    private int id;
    private String content;
    private int userId;
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
    }

    public static UpdateResponse createFromUpdate(Update update) {
        return new UpdateResponse(update);
    }

}