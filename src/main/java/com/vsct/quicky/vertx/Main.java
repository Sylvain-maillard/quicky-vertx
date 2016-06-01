package com.vsct.quicky.vertx;


import com.vsct.quicky.vertx.aggregate.Brute;
import com.vsct.quicky.vertx.commands.JoinArena;
import com.vsct.quicky.vertx.commands.QuitArena;
import com.vsct.quicky.vertx.eventhandler.Arena;
import com.vsct.quicky.vertx.eventhandler.ArenaEmptyEvent;
import com.vsct.quicky.vertx.eventstore.BruteEventStore;
import com.vsct.quicky.vertx.projections.HallOfFame;
import io.vertx.core.Vertx;
import org.slf4j.Logger;

import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Sylvain on 26/05/2016.
 */
public class Main {

    private static final Logger LOGGER = getLogger(Main.class);

    public static Vertx vertx = Vertx.vertx();

    public static void main(String[] args) throws IOException {
        vertx.deployVerticle(Arena.class.getName());
        vertx.deployVerticle(BruteEventStore.class.getName());
        HallOfFame hallOfFame = new HallOfFame();
        vertx.deployVerticle(hallOfFame);

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
//
//        vertx.setTimer(1000, h -> {
            hallOfFame.displayHallOfFame();
//            bruteEventStore.displayAllEventByTime();
//        });
    }
}
