# Twoter REST API built with Java8 and Spring5

![Screenshot](images/diagram1.png)

## Description

Part of a mini social network project combining features from Facebook and Twitter (post updates and comments, like updates and comments, register, login, follow other users, receive notification in realtime when your comment/update is liked, when someone you followed has posted an update).

## Features

- Auth: sign up, login, logout
- Update profile info, profile picture, etc.
- Follow other users
- Post updates. Updates may contain tags.
- Search updates by tags
- Comment updates
- Like posts and updates
- Receive realtime notifications for someone commenting on your updates, liking your updates or comments, someone you follow posts something.

## Purpose

This project main purpose is for skill sharpenning

## Run project

`docker-compose build` will build the project image
`docker-compose up` will run the project image
`docker exec -it twoter_db bash /usr/db_setup.sh` will run SQL scripts to create and populate tables
