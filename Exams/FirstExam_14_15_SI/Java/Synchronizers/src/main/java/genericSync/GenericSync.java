package genericSync;

/**
 * Created by Ruben Gomes on 30/06/2015.
 */
public class GenericSync {

    private int permits;
    private boolean signaled;
    private Object lock = new Object();

    public GenericSync(int initial){
        if(initial>0)
            this.permits = initial;
    }


    public boolean waitSync(long timeout) throws InterruptedException {
        synchronized (lock){
            if(signaled)
                return true;

            if(permits>0){
                permits--;
                return true;
            }
            if(timeout == 0) return false;

            do{
                long start = System.currentTimeMillis();
                try{
                    lock.wait(timeout);
                }catch (InterruptedException e){ // if interrupted
                    if(signaled){ // if releaseAll interrupt but consume
                        Thread.currentThread().interrupt();
                        return true;
                    }
                    if(permits>0){// if releaseOne ,interrupt but consume
                        permits--;
                        Thread.currentThread().interrupt();
                        return true;
                    }// if not signaled and don*t have permits throw exception
                    throw e;
                }
                if(signaled)
                    return true;
                if(permits>0){
                    permits--;
                    return true;
                }
                long deltaTime = System.currentTimeMillis()-start;
                timeout = (deltaTime >= timeout)? 0 : timeout - deltaTime;

            }while(timeout>0);
            return false;
        }
    }


    public void releaseOne(){
        synchronized (lock){
            permits++;
            lock.notify();
        }
    }

    public void releaseAll(){
        synchronized (lock){
            signaled = true;
            lock.notifyAll();
        }
    }
}
