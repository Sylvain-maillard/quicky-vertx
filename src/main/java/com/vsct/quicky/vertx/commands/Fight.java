package com.vsct.quicky.vertx.commands;

import com.google.common.collect.ImmutableList;
import com.vsct.quicky.vertx.Main;
import com.vsct.quicky.vertx.aggregate.Brute;
import com.vsct.quicky.vertx.events.BruteLooseFight;
import com.vsct.quicky.vertx.events.BruteWinFight;
import com.vsct.quicky.vertx.eventstore.BruteCommand;
import com.vsct.quicky.vertx.eventstore.BruteEvent;
import io.vertx.core.Handler;

import java.security.SecureRandom;
import java.util.List;

/**
 * Created by Sylvain on 30/05/2016.
 */
public class Fight extends BruteCommand {

    private static final SecureRandom random = new SecureRandom();

    private final String opponentId;

    public Fight(String opponentId) {
        this.opponentId = opponentId;
    }

    @Override
    public void execute(Brute brute, Handler<List<BruteEvent>> handler) {
        System.out.println(brute.getId() + " will fight against " + opponentId);
        // file un random: 1 chance sur 2 de gagner
        Main.vertx.setTimer(random.nextInt(500) + 500, h -> {
            if (random.nextBoolean()) {
                handler.handle(ImmutableList.of(new BruteWinFight(brute.getId()), new BruteLooseFight(opponentId)));
            } else {
                handler.handle(ImmutableList.of(new BruteLooseFight(brute.getId()), new BruteWinFight(opponentId)));
            }
        });
    }
}
