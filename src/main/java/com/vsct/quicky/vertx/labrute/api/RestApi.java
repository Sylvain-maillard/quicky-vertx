package com.vsct.quicky.vertx.labrute.api;

import com.vsct.quicky.vertx.labrute.aggregate.Brute;
import com.vsct.quicky.vertx.labrute.commands.JoinArena;
import com.vsct.quicky.vertx.labrute.commands.QuitArena;
import com.vsct.quicky.vertx.labrute.eventstore.BruteCommand;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Sylvain on 02/06/2016.
 */
public class RestApi extends AbstractVerticle {

    private static final Logger LOGGER = getLogger(RestApi.class);

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);

        router.post("/brute/:bruteId/").handler(this::handleNew);
        router.post("/brute/:bruteId/:command").handler(this::handleCommand);

        router.get("/brute/:bruteId/").handler(this::handleQuery);
        router.get("/brute/:bruteId/history").handler(this::handleHistory);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8080);
    }

    private void handleHistory(RoutingContext routingContext) {
        String bruteId = routingContext.request().getParam("bruteId");
        // get brute events:
        vertx.eventBus().send("listEvents", bruteId, (AsyncResult<Message<String>> handler) -> {
            if (handler.succeeded()) {
                String body = handler.result().body();
                routingContext.response().putHeader("Content-Type","application/json").end(body);
            } else {
                routingContext.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end(handler.cause().getMessage());
            }
        });
    }

    private void handleNew(RoutingContext routingContext) {
        // create new brute:
        String bruteId = routingContext.request().getParam("bruteId");
        vertx.eventBus().send("createNew", bruteId, result ->  {
           if (result.failed()) {
               routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end("brute with id " + bruteId + " allready exist");
           } else {
               routingContext.response().setStatusCode(HttpResponseStatus.CREATED.code()).end();
           }
        });
    }

    private void handleQuery(RoutingContext routingContext) {
        String bruteId = routingContext.request().getParam("bruteId");
        // build the brute:
        vertx.eventBus().send("applyEvents", bruteId, (AsyncResult<Message<String>> handler) -> {
            if (handler.succeeded()) {
                routingContext.response().putHeader("Content-Type","application/json").end(handler.result().body());
            } else {
                routingContext.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end(handler.cause().getMessage());
            }
        });
    }

    private void handleCommand(RoutingContext routingContext) {
        String bruteId = routingContext.request().getParam("bruteId");
        String command = routingContext.request().getParam("command");
        // check command:
        try {
            final Commands commands = Commands.valueOf(command.toUpperCase());
            // build the brute:
            vertx.eventBus().send("applyEvents", bruteId, (AsyncResult<Message<String>> handler) -> {
                if (handler.failed()) {
                    routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end(handler.cause().getMessage());
                } else {
                    Brute brute = Json.decodeValue(handler.result().body(), Brute.class);
                    brute.processCommand(commands.getInstance());
                    // respond to client:
                    routingContext.response().setStatusCode(HttpResponseStatus.ACCEPTED.code()).end();
                }
            });
        } catch (IllegalArgumentException e) {
            routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end(e.getMessage());
        }

    }

    enum Commands {
        JOINARENA {
            @Override
            public BruteCommand getInstance() {
                return new JoinArena();
            }
        },
        QUITARENA {
            @Override
            public BruteCommand getInstance() {
                return new QuitArena();
            }
        };

        public abstract BruteCommand getInstance();
    }


}
