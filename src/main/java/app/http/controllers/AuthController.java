package app.http.controllers;

import app.http.pojos.AuthResource;
import app.http.pojos.CustomUserDetails;
import app.http.pojos.LoginResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.Http401AuthenticationEntryPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

@SuppressWarnings("unused")
@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    @Autowired
    AuthController(final AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @RequestMapping(path = "/login-data", method = RequestMethod.GET)
    public ResponseEntity loginData(final @AuthenticationPrincipal CustomUserDetails user, final HttpSession session) {
        if (user == null || session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(new AuthResource(user.getId(), session.getId()));
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public AuthResource login(final @RequestBody LoginResource loginData, final HttpSession session) {
        System.out.println(loginData.getUsername() + ", " + loginData.getPassword());
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
