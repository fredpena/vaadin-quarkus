package dev.fredpena.app.data;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

/**
 * @author me@fredpena.dev
 * @created 28/01/2024  - 13:23
 */
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private String email;

    private String password;

    private String passwordRepeat;

    private String displayName;

    private String phone;

    private String photoUrl;

    private boolean emailVerified;

    private boolean disabled;

    private boolean agree;

}
