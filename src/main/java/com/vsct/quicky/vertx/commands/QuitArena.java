package com.vsct.quicky.vertx.commands;

import com.google.common.collect.ImmutableList;
import com.vsct.quicky.vertx.aggregate.Brute;
import com.vsct.quicky.vertx.events.BruteQuit;
import com.vsct.quicky.vertx.eventstore.BruteCommand;
import com.vsct.quicky.vertx.eventstore.BruteEvent;
import io.vertx.core.Handler;

import java.util.List;

/**
 * Created by Sylvain on 30/05/2016.
 */
public class QuitArena extends BruteCommand {

    @Override
    public void execute(Brute brute, Handler<List<BruteEvent>> handler) {
        handler.handle(ImmutableList.of(new BruteQuit(brute.getId())));
    }
}
