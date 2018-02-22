package app.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    private int id;
    private String name;
    private int updateId;
    private int createdAt;

//    private List<Integer> updateIds;
//    public static Tag createEmptyTag() {
//        final Tag tag = new Tag();
//        tag.setUpdateIds(new ArrayList<>());
//
//        return tag;
//    }

}
