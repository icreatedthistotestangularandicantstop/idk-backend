package app.services;

import app.core.repos.UpdateRepository;
import app.http.pojos.UpdateResource;
import app.pojo.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateService {
    @Autowired
    private UpdateRepository updateRepository;

    public Update addNew(UpdateResource updateResource) {
        Update update = new Update();
        update.setContent(updateResource.getContent());
        update.setUserId(updateResource.getUserId());

        int newUpdateId = updateRepository.add(update);
        update.setId(newUpdateId);

        return update;
    }
}
