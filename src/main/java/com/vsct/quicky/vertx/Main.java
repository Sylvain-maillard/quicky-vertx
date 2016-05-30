package com.vsct.quicky.vertx;


import com.vsct.quicky.vertx.aggregate.Arena;
import com.vsct.quicky.vertx.aggregate.Brute;
import com.vsct.quicky.vertx.commands.JoinArena;
import com.vsct.quicky.vertx.commands.QuitArena;
import com.vsct.quicky.vertx.eventstore.BruteEvent;
import com.vsct.quicky.vertx.eventstore.BruteEventStore;
import com.vsct.quicky.vertx.projections.HallOfFame;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

import java.io.IOException;
import java.util.List;

/**
 * Created by Sylvain on 26/05/2016.
 */
public class Main {

    public static Vertx vertx = Vertx.vertx();

    public static BruteEventStore bruteEventStore = new BruteEventStore();

    public static void main(String[] args) throws IOException {
        vertx.deployVerticle(Arena.class.getName());
        vertx.deployVerticle(bruteEventStore);
        HallOfFame hallOfFame = new HallOfFame();
        vertx.deployVerticle(hallOfFame);

        // creer une nouvelle brute:
        Brute brute = new Brute().setId("brute1");
        brute.processCommand(new JoinArena());
        // creer une nouvelle brute:
        Brute brute2 = new Brute().setId("brute2");
        brute2.processCommand(new JoinArena());

        /// display all brutes status
        vertx.setTimer(1000,  h-> {
            bruteEventStore.displayAllBrutes();
        });

        System.in.read();

        // virer une brute:
        brute2.processCommand(new QuitArena());

        // creer une nouvelle brute:
        Brute brute3 = new Brute().setId("brute3");
        brute3.processCommand(new JoinArena());
        Brute brute4 = new Brute().setId("brute4");
        brute4.processCommand(new JoinArena());
        for (int i = 5; i < 10; i++) {
            new Brute().setId("brute"+i).processCommand(new JoinArena());
        }

        vertx.setTimer(1000,  h-> {
            hallOfFame.displayHallOfFame();
        });
    }
}
