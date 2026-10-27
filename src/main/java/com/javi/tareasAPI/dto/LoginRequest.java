
package com.javi.tareasAPI.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LoginRequest {

    @NotBlank(message = "El usuario es obligatorio")
    @Size(min = 3, max = 50,
          message = "El usuario debe tener entre 3 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$",
             message = "El formato del usuario no es válido")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(max = 72,
          message = "La contraseña no puede superar los 72 caracteres")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
