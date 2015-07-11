package ntKeyEvent;

import java.util.LinkedList;

/**
 * Created by Ruben Gomes on 02/07/2015.
 */
public class NtKeyEvent {

    private static class Key{
        Object key;
        boolean match;

        public Key(Object key){
            this.key = key;
        }
    }

    private LinkedList<Key> releases = new LinkedList<Key>();
    private LinkedList<Key> waits = new LinkedList<Key>();
    private Object lock = new Object();

    public boolean release(Object key, long timeout)throws InterruptedException{
        synchronized (lock){
            Key releaseKey = new Key(key);
            int lengthWaits = waits.size();
            int i = -1;
            while(++i < lengthWaits){ // search keys in waits
                Key waitKey = waits.get(i);
                if(waitKey.equals(releaseKey)){ // if any key is equals to new key
                    waitKey.match = true; // alert wait key that match and notify threads. Don`t insert in releases don`t need to wait
                    lock.notifyAll();
                    return true;
                }
            }
            releases.add(releaseKey); // if not equals to anyone insert in releases and start wait

            do{
                long start = System.currentTimeMillis();
                try{
                    lock.wait(timeout);
                }catch (InterruptedException e){ // if interrupt verify if this key are math a true
                    if(releaseKey.match){
                        releases.remove(releaseKey); // if have remove from releases to stop wait this thread
                        Thread.currentThread().interrupt(); // interrupt thread
                        lock.notifyAll(); // notify all threads to continue the process
                        return true;
                    }
                    releases.remove(releaseKey); // if key don`t have match this thread abandon process
                    throw e;
                }
                if(releaseKey.match){ // if key have match remove for stop waiting this thread and notifyAll that data is changed
                    releases.remove(releaseKey);
                    lock.notifyAll();
                    return true;
                }
                long deltaTime = System.currentTimeMillis() - start;
                timeout = (deltaTime >= timeout)? 0 : timeout - deltaTime;
                if(timeout == 0){ // if timeout expires this thread abandon process
                    releases.remove(releaseKey);
                    return false;
                }
            }while(true);
        }
    }

    //the same as release but for waits queue
    public boolean wait(Object key, long timeout)throws InterruptedException{
        synchronized (lock){
            Key waitKey = new Key(key);
            int lengthWaits = releases.size();
            int i = -1;
            while(++i < lengthWaits){
                Key releaseKey = releases.get(i);
                if(releaseKey.equals(waitKey)){
                    releaseKey.match = true;
                    lock.notifyAll();
                    return true;
                }
            }
            waits.add(waitKey);
            do{
                long start = System.currentTimeMillis();
                try{
                    lock.wait(timeout);
                }catch (InterruptedException e){
                    if(waitKey.match){
                        waits.remove(waitKey);
                        Thread.currentThread().interrupt();
                        lock.notifyAll();
                        return true;
                    }
                    waits.remove(waitKey);
                    throw e;
                }
                if(waitKey.match){
                    waits.remove(waitKey);
                    lock.notifyAll();
                    return true;
                }
                long deltaTime = System.currentTimeMillis() - start;
                timeout = (deltaTime >= timeout)? 0 : timeout - deltaTime;
                if(timeout == 0){
                    waits.remove(waitKey);
                    return false;
                }
            }while(true);
        }
    }
}
