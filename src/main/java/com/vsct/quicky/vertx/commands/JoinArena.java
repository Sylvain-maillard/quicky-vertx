package com.vsct.quicky.vertx.commands;

import com.google.common.collect.ImmutableList;
import com.vsct.quicky.vertx.aggregate.Brute;
import com.vsct.quicky.vertx.events.BruteJoined;
import com.vsct.quicky.vertx.eventstore.BruteCommand;
import com.vsct.quicky.vertx.eventstore.BruteEvent;
import io.vertx.core.Handler;

import java.util.List;

/**
 * Created by Sylvain on 29/05/2016.
 */
public class JoinArena extends BruteCommand {

    @Override
    public void execute(Brute brute, Handler<List<BruteEvent>> handler) {
        handler.handle(ImmutableList.of(new BruteJoined(brute.getId())));
    }
}
