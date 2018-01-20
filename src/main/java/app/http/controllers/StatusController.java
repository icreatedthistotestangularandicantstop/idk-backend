package app.http.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/hello")
public class StatusController {

    @RequestMapping(method = RequestMethod.GET)
    public String hello() {
        return "Hello World. It works!";
    }

}
