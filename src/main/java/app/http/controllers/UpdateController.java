package app.http.controllers;

import app.core.repos.UpdateRepository;
import app.http.pojos.UpdateResource;
import app.pojo.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/update")
public class UpdateController {
    @Autowired
    private UpdateRepository updateRepository;

    @RequestMapping(method = RequestMethod.POST)
    public Update add(final @RequestBody @Valid UpdateResource updateData) {
        Update update = new Update();
        update.setContent(updateData.getContent());
        update.setUserId(updateData.getUserId());

        int newUpdateId = updateRepository.add(update);
        update.setId(newUpdateId);

        return update;
    }

    @RequestMapping(path = "/{updateId}", method = RequestMethod.GET)
    public Update getById(final @PathVariable(value = "updateId") int updateId) {
        return updateRepository.findById(updateId);
    }

    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET)
    public List<Update> getByUserId(final @PathVariable(value = "userId") int userId) {
        return updateRepository.findByUserId(userId);
    }

}
