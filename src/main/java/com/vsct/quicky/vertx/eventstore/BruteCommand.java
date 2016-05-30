package com.vsct.quicky.vertx.eventstore;

import java.util.List;

/**
 * Created by Sylvain on 29/05/2016.
 */
public abstract class BruteCommand {

    public abstract List<BruteEvent> execute(String id);
}
