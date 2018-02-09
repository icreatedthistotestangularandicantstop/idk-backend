package app.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    private int id;
    private int itemId;
    private int userId;
    private int likedAt;
    private LikeType type;

    public static Like createUpdateLike(int itemId, int userId) {
        return createLikeOfType(itemId, userId, LikeType.UPDATE);
    }

    public static Like createCommentLike(int itemId, int userId) {
        return createLikeOfType(itemId, userId, LikeType.COMMENT);
    }

    private static Like createLikeOfType(int itemId, int userId, LikeType type) {
        final Like like = new Like();
        like.itemId = itemId;
        like.userId = userId;
        like.type = type;

        return like;
    }

}
