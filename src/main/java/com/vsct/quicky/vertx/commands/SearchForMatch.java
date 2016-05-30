package com.vsct.quicky.vertx.commands;

import com.vsct.quicky.vertx.eventstore.BruteCommand;
import com.vsct.quicky.vertx.eventstore.BruteEvent;

import java.util.List;

/**
 * Created by Sylvain on 30/05/2016.
 */
public class SearchForMatch extends BruteCommand {

    @Override
    public List<BruteEvent> execute(String id) {
        return null;
    }
}
