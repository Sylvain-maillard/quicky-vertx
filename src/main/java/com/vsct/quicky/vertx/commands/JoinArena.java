package com.vsct.quicky.vertx.commands;

import com.google.common.collect.ImmutableList;
import com.vsct.quicky.vertx.events.BruteJoined;
import com.vsct.quicky.vertx.eventstore.BruteCommand;
import com.vsct.quicky.vertx.eventstore.BruteEvent;

import java.util.List;

/**
 * Created by Sylvain on 29/05/2016.
 */
public class JoinArena extends BruteCommand {
    @Override
    public List<BruteEvent> execute(String id) {
        System.out.println("brute join arena");

        return ImmutableList.of(new BruteJoined(id));
    }
}
