package org.sunlib.turtle;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import sunlib.turtle.TurtleServer;

/**
 * @author linuo
 * @version 1.0
 * @date 8/25/13
 */
public class TurtleServerService extends Service {

    public void onCreate(){
        Log.i("Turtle", "Service onCreated");
        TurtleServer.main(null);
        super.onCreate();
    }


    public IBinder onBind(Intent intent) {
        return null;
    }
}