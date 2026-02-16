package org.pingvin.AdvancedTeamSystem;

import java.util.ArrayList;


public class ATSEventManager {
    private final ArrayList<ATSListener> listeners;

    public ATSEventManager() { this.listeners = new ArrayList<>(); };

    public void addListener(ATSListener listener) {
        listeners.add(listener);
    }

    public void sendTeamsUpdate() {
        for (ATSListener listener:listeners) {
            listener.onTeamsUpdate();
        }
    }
}
