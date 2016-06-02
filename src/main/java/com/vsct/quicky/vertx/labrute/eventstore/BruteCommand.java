package com.vsct.quicky.vertx.labrute.eventstore;

import com.vsct.quicky.vertx.labrute.aggregate.Brute;
import com.vsct.quicky.vertx.labrute.fwk.Command;
import com.vsct.quicky.vertx.labrute.fwk.Event;
import io.vertx.core.Handler;

import java.util.List;

/**
 * Created by Sylvain on 29/05/2016.
 */
public abstract class BruteCommand implements Command<Brute> {

    public abstract void execute(Brute brute, Handler<List<Event>> handler);
}
