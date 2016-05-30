package com.vsct.quicky.vertx.commands;

import com.google.common.collect.ImmutableList;
import com.vsct.quicky.vertx.events.BruteQuit;
import com.vsct.quicky.vertx.eventstore.BruteCommand;
import com.vsct.quicky.vertx.eventstore.BruteEvent;

import java.util.List;

/**
 * Created by Sylvain on 30/05/2016.
 */
public class QuitArena extends BruteCommand {
    @Override
    public List<BruteEvent> execute(String id) {
        return ImmutableList.of(new BruteQuit(id));
    }
}
