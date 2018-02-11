CREATE TABLE IF NOT EXISTS `users` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `first_name` VARCHAR(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
    `last_name` VARCHAR(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
    `username` VARCHAR(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci UNIQUE NOT NULL,
    `password` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
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
