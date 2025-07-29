package br.com.dio.service;

import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.com.dio.service.EventEnum.CLEAR_SPACE;

public class NotifierService {

    private final Map<EventEnum, List<EventListener>> listeners = new HashMap<>() {{
        put(CLEAR_SPACE, new ArrayList<>());
    }};

    public void subscriber(final EventEnum eventType, final EventListener listener) {
        var selectedListeners = listeners.get(eventType);
        if (listener != null && selectedListeners != null) {
            selectedListeners.add(listener);
        }
    }

    public void notify(EventEnum event) {
        var selectedListeners = listeners.get(event);
        if (selectedListeners != null) {
            selectedListeners.forEach(listener -> {
                if (listener != null) {
                    listener.update(event);
                }

            });
        }
    }
}