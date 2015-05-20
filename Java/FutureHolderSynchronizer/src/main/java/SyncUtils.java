/**
 * Created by Ruben Gomes on 20/05/2015.
 */
public class SyncUtils {


    public static long AdjustTimeout(int lastTime, long timeout) {
        if(timeout != -1){
            int now = (int) System.currentTimeMillis();
            int elapsed = (now == lastTime)? 1 : now -lastTime;
            if(elapsed >= timeout){
                timeout = 0;
            }else{
                timeout -= elapsed;
                lastTime = now;
            }
        }
        return timeout;
    }
}
