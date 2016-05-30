package com.vsct.quicky.vertx.eventstore;

import com.vsct.quicky.vertx.aggregate.Brute;
import io.vertx.core.Handler;

import java.util.List;

/**
 * Created by Sylvain on 29/05/2016.
 */
public abstract class BruteCommand {

    public abstract void execute(Brute brute, Handler<List<BruteEvent>> handler);
}
