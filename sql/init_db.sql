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
    `user_id` INT NOT NULL REFERENCES `users` (`id`),
    `created_at` INT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `comments` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `content` VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
    `update_id` INT NOT NULL REFERENCES `updates` (`id`),
    `user_id` INT NOT NULL REFERENCES `users` (`id`),
    `created_at` INT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `favorites` (
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `update_id` INT NOT NULL REFERENCES `updates` (`id`),
    `user_id` INT NOT NULL REFERENCES `users` (`id`),
    `favorited_at` INT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

