
package com.javi.tareasAPI.event;

import java.time.Instant;

public record TareaCreadaEvent(
        Integer id,
        String titulo,
        boolean completada,
        String username,
        Instant fechaCreacion
) {
}
