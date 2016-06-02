package com.vsct.quicky.vertx.labrute.eventstore;

import com.vsct.quicky.vertx.labrute.aggregate.Brute;
import com.vsct.quicky.vertx.labrute.fwk.Event;

/**
 * Created by Sylvain on 02/06/2016.
 */
public abstract class BruteEvent extends Event<Brute> {
    public BruteEvent(String id) {
        super(id);
    }
}
