package app.http.pojos;

import app.pojo.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse implements Serializable {
    private int id;
    private String content;
    private int updateId;
    private int userId;
    private int likes;
    private long createdAt;
    private boolean liked;

    public CommentResponse(Comment comment) {
        this.setId(comment.getId());
        this.setContent(comment.getContent());
        this.setCreatedAt(comment.getCreatedAt());
        this.setLikes(comment.getLikes());
        this.setUpdateId(comment.getUpdateId());
        this.setUserId(comment.getUserId());
    }

    public static CommentResponse createFromComment(Comment comment) {
        return new CommentResponse(comment);
    }

}
