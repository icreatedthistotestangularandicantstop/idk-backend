package app.pojo;

public enum NotificationType {
    UPDATE_COMMENT,
    UPDATE_LIKE,
    COMMENT_LIKE;

    public static NotificationType fromString(final String target) {
        final NotificationType type;
        switch (target) {
            case "UPDATE_LIKE":
                type = NotificationType.UPDATE_LIKE;
                break;
            case "COMMENT_LIKE":
                type = NotificationType.COMMENT_LIKE;
                break;
            case "UPDATE_COMMENT":
                type = NotificationType.UPDATE_COMMENT;
                break;
            default:
                type = null;
                break;
        }

        return type;
    }

}
