package com.vsct.quicky.vertx.commands;

import com.google.common.collect.ImmutableList;
import com.vsct.quicky.vertx.aggregate.Brute;
import com.vsct.quicky.vertx.eventhandler.Arena;
import com.vsct.quicky.vertx.events.BruteShouldRest;
import com.vsct.quicky.vertx.events.OpponentFound;
import com.vsct.quicky.vertx.eventstore.BruteCommand;
import com.vsct.quicky.vertx.eventstore.BruteEvent;
import io.vertx.core.Handler;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Sylvain on 30/05/2016.
 */
public class FindOpponent extends BruteCommand {

    private static final Logger LOGGER = getLogger(FindOpponent.class);

    private final Arena arena;

    public FindOpponent(Arena arena) {
        this.arena = arena;
    }

    @Override
    public void execute(Brute brute, Handler<List<BruteEvent>> handler) {
        //
        // select les brutes qui n'ont pas atteint le max de combat.
        if (brute.getFightCount() >= 3) {
            LOGGER.debug("brute " + brute + " should rest for this day ! ");
            handler.handle(ImmutableList.of(new BruteShouldRest(brute.getId())));
        } else {
            LOGGER.debug("check fight for brute " + brute);
            // select les brutes qui ont le même niveau d'xp
            Optional<Brute> opponent = arena.lockABruteWithSameXpAs(brute);
            if (opponent.isPresent()) {
                String opponentId = opponent.get().getId();
                handler.handle(ImmutableList.of(new OpponentFound(brute.getId(), opponentId))); //, new OpponentFound(opponentId, id));
            } else {
                arena.registerAvailableBrute(brute);
            }
        }
    }
}
