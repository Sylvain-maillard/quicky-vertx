package com.vsct.quicky.vertx.labrute.commands;

import com.google.common.collect.ImmutableList;
import com.vsct.quicky.vertx.labrute.aggregate.Brute;
import com.vsct.quicky.vertx.labrute.events.BruteJoined;
import com.vsct.quicky.vertx.labrute.eventstore.BruteCommand;
import com.vsct.quicky.vertx.labrute.fwk.Event;
import io.vertx.core.Handler;

import java.util.List;

/**
 * Created by Sylvain on 29/05/2016.
 */
public class JoinArena extends BruteCommand {

    @Override
    public void execute(Brute brute, Handler<List<Event>> handler) {
        handler.handle(ImmutableList.of(new BruteJoined(brute.getId())));
    }

}
