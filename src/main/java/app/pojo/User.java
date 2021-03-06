package app.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private int followers;
    private int following;
    private int updates;
    private long createdAt;
    private Image image;
    private boolean isFollowed;

}
