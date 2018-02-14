package app.core.repos;

import java.util.LinkedList;
import java.util.List;

public class BaseRepository {
    protected <T> List<T> getEmptyList(Class<T> cls) {
        return new LinkedList<>();
    }
}
