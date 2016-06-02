package com.vsct.quicky.vertx.labrute;


import com.vsct.quicky.vertx.labrute.aggregate.Brute;
import com.vsct.quicky.vertx.labrute.api.RestApi;
import com.vsct.quicky.vertx.labrute.commands.JoinArena;
import com.vsct.quicky.vertx.labrute.commands.QuitArena;
import com.vsct.quicky.vertx.labrute.eventstore.MyEventStore;
import com.vsct.quicky.vertx.labrute.services.BruteService;
import com.vsct.quicky.vertx.labrute.views.HallOfFame;
import com.vsct.quicky.vertx.labrute.services.ArenaService;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import org.slf4j.Logger;

import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Sylvain on 26/05/2016.
 */
public class Main {

    public static Vertx vertx = Vertx.vertx();

    public static void main(String[] args) throws IOException {
        vertx.deployVerticle(ArenaService.class.getName());
        vertx.deployVerticle(MyEventStore.class.getName());
        vertx.deployVerticle(HallOfFame.class.getName());
        vertx.deployVerticle(BruteService.class.getName());
        vertx.deployVerticle(RestApi.class.getName(), new DeploymentOptions().setInstances(8));

        // creer une nouvelle brute:
        Brute brute = new Brute().setId("Axel");
        brute.processCommand(new JoinArena());
        // creer une nouvelle brute:
        Brute brute2 = new Brute().setId("Sylvain");
        brute2.processCommand(new JoinArena());

        /// display all brutes status

        System.in.read();

        // virer une brute:
        brute2.processCommand(new QuitArena());

        // creer une nouvelle brute:
        Brute brute3 = new Brute().setId("brute3");
        brute3.processCommand(new JoinArena());
        Brute brute4 = new Brute().setId("brute4");
        brute4.processCommand(new JoinArena());
        for (int i = 5; i < 1000; i++) {
            int finalI = i;
            vertx.runOnContext(h -> {
                new Brute().setId("brute" + finalI).processCommand(new JoinArena());
            });
        }

        System.in.read();
    }
}
