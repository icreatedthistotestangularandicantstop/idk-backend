package app.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Update implements Serializable {
    private int id;
    private String content;
    private int userId;
    private int likes;
    private int commentsCount;
    private long createdAt;

}
