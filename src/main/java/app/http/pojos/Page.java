package app.http.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
public class Page {
    private @Min(1) int page;

    public Page() {
        page = 1;
    }
}
