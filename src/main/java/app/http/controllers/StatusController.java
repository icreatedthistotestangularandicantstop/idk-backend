package app.http.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(value = "/hello")
public class StatusController {

    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "You're not gay enough")
    static class E extends RuntimeException {}

    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    @RequestMapping(method = RequestMethod.GET)
    public String hello() {
//        throw new E();
//        throw new RuntimeException("EE!");

        return "Hello World. It works!";
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class M {
        @NotNull
        private String s;

        @NotEmpty
        private String s2;
    }

    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    @RequestMapping(method = RequestMethod.POST)
    public String hello1(final @Valid @RequestBody M m) {
        System.out.println(m);
//        throw new E();
//        throw new RuntimeException("EE!");

        return "Hello World. It works!" + m.getS();
    }

}
