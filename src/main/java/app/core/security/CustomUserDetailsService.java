package app.core.security;

import app.core.repos.UserRepository;
import app.http.pojos.CustomUserDetails;
import app.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import static app.http.pojos.CustomUserDetails.Type.*;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(final String uname) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername(uname);
        if (user == null) {
            throw new UsernameNotFoundException(uname);
        }

        return new CustomUserDetails(user.getId(), user.getUsername(), user.getPassword(), USER.getRoles());
    }

}
