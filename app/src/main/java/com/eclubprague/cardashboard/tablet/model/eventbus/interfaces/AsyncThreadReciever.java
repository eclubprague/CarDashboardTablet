package com.eclubprague.cardashboard.tablet.model.eventbus.interfaces;

/**
 * Created by Michael on 5. 4. 2015.
 */
public interface AsyncThreadReciever<T extends Event>  {
    public void onEventAsync(T event);
}
