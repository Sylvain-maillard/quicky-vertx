package com.vsct.quicky.vertx.labrute.commands;

import com.google.common.collect.ImmutableList;
import com.vsct.quicky.vertx.labrute.Main;
import com.vsct.quicky.vertx.labrute.aggregate.Brute;
import com.vsct.quicky.vertx.labrute.events.BruteLooseFight;
import com.vsct.quicky.vertx.labrute.events.BruteWinFight;
import com.vsct.quicky.vertx.labrute.eventstore.BruteCommand;
import com.vsct.quicky.vertx.labrute.fwk.Event;
import io.vertx.core.Handler;
import org.slf4j.Logger;

import java.security.SecureRandom;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Sylvain on 30/05/2016.
 */
public class Fight extends BruteCommand {

    private static final Logger LOGGER = getLogger(Fight.class);

    private static final SecureRandom random = new SecureRandom();

    private final String opponentId;

    public Fight(String opponentId) {
        this.opponentId = opponentId;
    }

    @Override
    public void execute(Brute brute, Handler<List<Event>> handler) {
        LOGGER.debug(brute.getId() + " will fight against " + opponentId);
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
