package com.vsct.quicky.vertx.labrute.fwk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.vsct.quicky.vertx.labrute.fwk.Aggregate;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * Created by Sylvain on 29/05/2016.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Event<T extends Aggregate> {

    protected final String id;
    private final Date time = new Date();

    public Event(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Date getTime() {
        return time;
    }

    @JsonProperty
    public String getType() { return this.getClass().getSimpleName(); }

    public abstract void apply(T brute);

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("time", time)
                .toString();
    }
}
