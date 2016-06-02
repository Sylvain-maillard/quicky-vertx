package com.vsct.quicky.vertx.labrute.commands;

import com.google.common.collect.ImmutableList;
import com.vsct.quicky.vertx.labrute.aggregate.Arena;
import com.vsct.quicky.vertx.labrute.aggregate.Brute;
import com.vsct.quicky.vertx.labrute.events.BruteShouldRest;
import com.vsct.quicky.vertx.labrute.events.OpponentFound;
import com.vsct.quicky.vertx.labrute.eventstore.ArenaCommand;
import com.vsct.quicky.vertx.labrute.fwk.Event;
import io.vertx.core.Handler;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Sylvain on 30/05/2016.
 */
public class FindOpponent extends ArenaCommand {

    private static final Logger LOGGER = getLogger(FindOpponent.class);

    private final Brute forBrute;
    private final List<Brute> readyToFightBrute;

    public FindOpponent(Brute forBrute, List<Brute> readyToFightBrute) {
        this.forBrute = forBrute;
        this.readyToFightBrute = readyToFightBrute;
    }

    public Optional<Brute> lockABruteWithSameXpAs(Brute currentBrute) {
        Optional<Brute> first = readyToFightBrute.stream().filter(brute -> brute.sameXp(currentBrute) && !brute.getId().equals(currentBrute.getId()))
                .findFirst();
        if (first.isPresent()) {
            readyToFightBrute.remove(first.get());
        }
        return first;
    }

    @Override
    public void execute(Arena arena, Handler<List<Event>> handler) {
        //
        // select les brutes qui n'ont pas atteint le max de combat.
        if (forBrute.getFightCount() >= 3) {
            LOGGER.debug("brute " + forBrute + " should rest for this day ! ");
            handler.handle(ImmutableList.of(new BruteShouldRest(forBrute.getId())));
        } else {
            LOGGER.debug("check fight for brute " + forBrute);
            // select les brutes qui ont le même niveau d'xp
            Optional<Brute> opponent = lockABruteWithSameXpAs(forBrute);
            if (opponent.isPresent()) {
                String opponentId = opponent.get().getId();
                handler.handle(ImmutableList.of(new OpponentFound(forBrute.getId(), opponentId))); //, new OpponentFound(opponentId, id));
            } else {
                readyToFightBrute.add(forBrute);
            }
        }
    }
}
