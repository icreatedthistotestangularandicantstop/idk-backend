package app.http.controllers;

import app.core.repos.UserRepository;
import app.http.pojos.UserCreateResource;
import app.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    UserController(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.GET)
    public User getById(final @PathVariable int userId) {
        return userRepository.findById(userId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public User add(final @RequestBody @Valid UserCreateResource userCreateData) {
        User user = new User();
        user.setFirstName(userCreateData.getFirstName());
        user.setLastName(userCreateData.getLastName());
        user.setUsername(userCreateData.getUsername());

        int newUserId = userRepository.add(user);
        user.setId(newUserId);

        return user;
    }

}
