package br.com.unisinos.es.t2.application.port.out.event;

import br.com.unisinos.es.t2.application.domain.model.Event;

public interface PublishEventPort {

    void publish(Event event);
}
