package com.vsct.quicky.vertx.labrute;


import com.vsct.quicky.vertx.labrute.aggregate.Brute;
import com.vsct.quicky.vertx.labrute.api.RestApi;
import com.vsct.quicky.vertx.labrute.eventstore.MyEventStore;
import com.vsct.quicky.vertx.labrute.services.ArenaService;
import com.vsct.quicky.vertx.labrute.services.BruteService;
import com.vsct.quicky.vertx.labrute.views.HallOfFame;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

import java.io.IOException;

public class Main {

    public static Vertx vertx = Vertx.vertx();

    public static void main(String[] args) throws IOException {
        vertx.deployVerticle(ArenaService.class.getName());
        vertx.deployVerticle(MyEventStore.class.getName());
        vertx.deployVerticle(HallOfFame.class.getName());
        vertx.deployVerticle(BruteService.class.getName());
        vertx.deployVerticle(RestApi.class.getName(), new DeploymentOptions().setInstances(8));

        // creer une nouvelle brute:
        Brute chuckNorris = new Brute().setId("ChuckNorris");
        chuckNorris.join();
        // creer une nouvelle brute:
        Brute fifiBrindacier = new Brute().setId("FifiBrindacier");
        fifiBrindacier.join();

        System.in.read();

        // virer une brute:
        fifiBrindacier.quit();

        // creer des tas de brutes:
        for (int i = 3; i < 1000; i++) {
            int finalI = i;
            vertx.runOnContext(h -> {
                new Brute().setId("brute" + finalI).join();
            });
        }
    }
}
