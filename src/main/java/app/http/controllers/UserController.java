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
    @Autowired
    private UserRepository userRepository;

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
