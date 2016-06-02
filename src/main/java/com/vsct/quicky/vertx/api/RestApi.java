package com.vsct.quicky.vertx.api;

import com.vsct.quicky.vertx.aggregate.Brute;
import com.vsct.quicky.vertx.commands.JoinArena;
import com.vsct.quicky.vertx.commands.QuitArena;
import com.vsct.quicky.vertx.eventstore.BruteCommand;
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
                LOGGER.debug(body);
                routingContext.response().putHeader("Content-Type","application/json").end(body);
            } else {
                routingContext.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end(handler.cause().getMessage());
            }
        });
    }

    private void handleNew(RoutingContext routingContext) {
        // create new brute:
        String bruteId = routingContext.request().getParam("bruteId");
        Brute brute = new Brute();
        brute.setId(bruteId);
        brute.processCommand(new JoinArena());
        routingContext.response().setStatusCode(HttpResponseStatus.CREATED.code()).end();
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
            vertx.eventBus().send("applyEvents", bruteId, (AsyncResult<Message<String>> handler) -> processCommandOnBrute(commands, handler));
            // respond to client:
            routingContext.response().setStatusCode(HttpResponseStatus.ACCEPTED.code()).end();
        } catch (IllegalArgumentException e) {
            routingContext.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code()).end(e.getMessage());
        }

    }

    private void processCommandOnBrute(Commands command, AsyncResult<Message<String>> handler) {
        Brute brute = Json.decodeValue(handler.result().body(), Brute.class);
        brute.processCommand(command.getInstance());
    }

    enum Commands {
        QUITARENA {
            @Override
            public BruteCommand getInstance() {
                return new QuitArena();
            }
        };

        public abstract BruteCommand getInstance();
    }


}
