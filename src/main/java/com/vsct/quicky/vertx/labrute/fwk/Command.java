package com.vsct.quicky.vertx.labrute.fwk;

import com.vsct.quicky.vertx.labrute.fwk.Aggregate;
import com.vsct.quicky.vertx.labrute.fwk.Event;
import io.vertx.core.Handler;

import java.util.List;

/**
 * Created by Sylvain on 02/06/2016.
 */
public interface Command<T extends Aggregate> {
    void execute(T aggregat, Handler<List<Event>> handler);
}
