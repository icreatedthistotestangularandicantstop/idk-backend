package app.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Image implements Serializable {
    private int id;
    private String mimeType;
    private int userId;
    private long createdAt;
    private byte[] data;

}
