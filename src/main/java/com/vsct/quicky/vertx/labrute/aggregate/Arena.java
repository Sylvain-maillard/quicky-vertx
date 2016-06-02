package com.vsct.quicky.vertx.labrute.aggregate;

import com.vsct.quicky.vertx.labrute.commands.FindOpponent;
import com.vsct.quicky.vertx.labrute.fwk.Aggregate;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Sylvain on 02/06/2016.
 */
public class Arena extends Aggregate {

    // on triche: on aurait du utiliser l'event store pour ça !
    public static List<Brute> readyToFightBrute = new CopyOnWriteArrayList<>();


    public void findOpponent(Brute brute) {
        processCommand(new FindOpponent(brute, readyToFightBrute));
    }
}
