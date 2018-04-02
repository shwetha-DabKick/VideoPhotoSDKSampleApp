package sample.sdk.dabkick.snap;

import com.dabkick.videosdk.publicsettings.DabKick;
import com.dabkick.videosdk.publicsettings.DabKickMediaProvider;
import com.dabkick.videosdk.publicsettings.SdkApp;

/**
 * Created by iFocus on 02-04-2018.
 */

public class MyApplication extends SdkApp {

    @Override
    public void onCreate() {
        super.onCreate();

        DabKickMediaProvider dabkickMediaProvider =  Util.createDabKickMediaProvider(null);
        DabKick.setMediaProvider(dabkickMediaProvider);

    }
}
