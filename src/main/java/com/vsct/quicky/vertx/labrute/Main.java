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
        Brute brute = new Brute().setId("ChuckNorris");
        brute.join();
        // creer une nouvelle brute:
        Brute brute2 = new Brute().setId("FifiBrindacier");
        brute2.join();

        System.in.read();

        // virer une brute:
        brute2.quit();

        // creer des nouvelles brutes:
        Brute brute3 = new Brute().setId("brute3");
        brute3.join();
        Brute brute4 = new Brute().setId("brute4");
        brute4.join();
        for (int i = 5; i < 1000; i++) {
            int finalI = i;
            vertx.runOnContext(h -> {
                new Brute().setId("brute" + finalI).join();
            });
        }
    }
}
