package com.eclubprague.cardashboard.tablet.model.eventbus.interfaces;

/**
 * Created by Michael on 5. 4. 2015.
 */
public interface BackgroundThreadReciever<T extends Event>  {
    public void onEventBackgroundThread(T event);
}
