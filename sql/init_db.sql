CREATE TABLE IF NOT EXISTS `users` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `first_name` VARCHAR(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
    `last_name` VARCHAR(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
    `username` VARCHAR(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci UNIQUE NOT NULL,
    `password` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
    `followers` INT NOT NULL DEFAULT 0,
    `following` INT NOT NULL DEFAULT 0,
    `updates` INT NOT NULL DEFAULT 0,
    `created_at` INT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `updates` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `content` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
    `favorites` INT NOT NULL DEFAULT 0,
    `user_id` INT NOT NULL REFERENCES `users` (`id`),
    `comments` INT NOT NULL DEFAULT 0,
    `likes` INT NOT NULL DEFAULT 0,
    `created_at` INT NOT NULL,
    UNIQUE (`id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `comments` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `content` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
    `update_id` INT NOT NULL REFERENCES `updates` (`id`),
    `user_id` INT NOT NULL REFERENCES `users` (`id`),
    `likes` INT NOT NULL DEFAULT 0,
    `created_at` INT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `favorites` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `update_id` INT NOT NULL REFERENCES `updates` (`id`),
    `user_id` INT NOT NULL REFERENCES `users` (`id`),
    `favorited_at` INT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `update_likes` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `update_id` INT NOT NULL REFERENCES `updates` (`id`),
    `user_id` INT NOT NULL REFERENCES `users` (`id`),
    `liked_at` INT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `comment_likes` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `comment_id` INT NOT NULL REFERENCES `updates` (`id`),
    `user_id` INT NOT NULL REFERENCES `users` (`id`),
    `liked_at` INT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `followers` (
    `follower_id` INT NOT NULL REFERENCES `users` (`id`),
    `followed_id` INT NOT NULL REFERENCES `users` (`id`),
    `followed_at` INT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `tags` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
    `created_at` INT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `update_tags` (
    `tag_id` INT NOT NULL REFERENCES `tags` (`id`),
    `update_id` INT NOT NULL REFERENCES `updates` (`id`),
    UNIQUE(`tag_id`, `update_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE images (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `created_at` INT NOT NULL,
    `mime_type` VARCHAR(40) NOT NULL,
    `user_id` INT NOT NULL REFERENCES `users` (`id`)
);

CREATE TABLE IF NOT EXISTS `notifications` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `to_user_id` INT NOT NULL REFERENCES `users` (`id`),
    `from_user_id` INT NOT NULL REFERENCES `users` (`id`),
    `rel_id` INT NOT NULL,
    `rel_type` VARCHAR(55) NOT NULL,
    `seen` TINYINT(1) NOT NULL,
    `created_at` INT NOT NULL,
    UNIQUE(`to_user_id`, `rel_id`, `rel_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
