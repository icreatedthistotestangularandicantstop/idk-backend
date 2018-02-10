package app.http.controllers;

import app.http.pojos.AuthResource;
import app.http.pojos.CustomUserDetails;
import app.http.pojos.LoginResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping(path = "/login-data", method = RequestMethod.GET)
    public AuthResource loginData(final @AuthenticationPrincipal CustomUserDetails user, final HttpSession session) {
        if (user == null) {
            System.out.println("USER IS NULL");
        }
        if (session == null) {
            System.out.println("SESSION IS NULL");
        }
        return new AuthResource(user.getId(), session.getId());
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public AuthResource login(final @RequestBody LoginResource loginData, final HttpSession session) {
        CustomUserDetails user = authenticate(loginData);

        return new AuthResource(user.getId(), session.getId());
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(final HttpSession session) {
        session.invalidate();
    }

    private CustomUserDetails authenticate(LoginResource loginData) {
        final UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                loginData.getUsername(),
                loginData.getPassword()
        );
        final Authentication auth = authenticationManager.authenticate(authRequest);
        final SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        return (CustomUserDetails) auth.getPrincipal();
    }
}
