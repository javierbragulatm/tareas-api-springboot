
package com.javi.tareasAPI.messaging;

import com.javi.tareasAPI.event.TareaCreadaEvent;

public interface TareaEventPublisher {

    void publicarTareaCreada(TareaCreadaEvent evento);
}
