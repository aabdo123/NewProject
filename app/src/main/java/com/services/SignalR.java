package com.services;

import com.models.SignalRCommandModel;
import com.models.SignalRModel;

/**
 * Created by Saferoad-Dev1 on 9/13/2017.
 */

public interface SignalR {

    void onMessageReceived(SignalRModel signalRModel);
    void onCommandReceived(SignalRCommandModel signalRCommandModel);
}
