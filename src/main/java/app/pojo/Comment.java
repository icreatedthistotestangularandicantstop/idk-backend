package app.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {
    private int id;
    private String content;
    private int updateId;
    private int userId;
    private long createdAt;

}
