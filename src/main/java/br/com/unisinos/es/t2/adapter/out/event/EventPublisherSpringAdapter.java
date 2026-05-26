package br.com.unisinos.es.t2.adapter.out.event;

import br.com.unisinos.es.t2.application.domain.model.Event;
import br.com.unisinos.es.t2.application.port.out.event.PublishEventPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class EventPublisherSpringAdapter implements PublishEventPort {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(Event event) {
        log.debug("Publishing event: {}", event);
        applicationEventPublisher.publishEvent(event);
    }
}
