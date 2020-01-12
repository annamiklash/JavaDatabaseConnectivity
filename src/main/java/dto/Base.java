package dto;

import lombok.*;


@Data
@AllArgsConstructor
public abstract class Base {

    private Integer id;

    public boolean hasExistingId() {
        return getId() > 0;
    }
}
