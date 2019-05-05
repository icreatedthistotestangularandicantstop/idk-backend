package app.http.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateResource {
    @NotEmpty
    private String username;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String password;


}
