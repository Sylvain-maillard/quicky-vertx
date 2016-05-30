package com.vsct.quicky.vertx.commands;

import com.google.common.collect.ImmutableList;
import com.vsct.quicky.vertx.events.BruteLooseFight;
import com.vsct.quicky.vertx.events.BruteWinFight;
import com.vsct.quicky.vertx.eventstore.BruteCommand;
import com.vsct.quicky.vertx.eventstore.BruteEvent;

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
    public List<BruteEvent> execute(String id) {
        System.out.println(id + " will fight against " + opponentId);
        // file un random: 1 chance sur 2 de gagner
        if (random.nextBoolean()) {
            return ImmutableList.of(new BruteWinFight(id), new BruteLooseFight(opponentId));
        } else {
            return ImmutableList.of(new BruteLooseFight(id), new BruteWinFight(opponentId));
        }
    }
}
