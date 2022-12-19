package at.htl.websocket;

import at.htl.controller.SurveyController;
import at.htl.model.Survey;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.requireNonNull;

@ServerEndpoint("/survey")
@ApplicationScoped
public class SurveySocketServer {

    Map<String, Session> sessions = new ConcurrentHashMap<>();

    @Inject
    SurveyController controller;

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("onOpen");
        sessions.put(session.getId(), session);
        Survey survey = controller.getSurvey();
        if (survey != null) {
            session.getAsyncRemote().sendObject(Json.encode(survey));
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("onClose");
        sessions.remove(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("onError: " + throwable);
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("onMessage: " + message);
    }

    public void broadcast() {
        Survey survey = controller.getSurvey();
        if (survey != null) {
            for (Session session : sessions.values()) {
                session.getAsyncRemote().sendObject(Json.encode(survey));
            }
        }
    }

}
