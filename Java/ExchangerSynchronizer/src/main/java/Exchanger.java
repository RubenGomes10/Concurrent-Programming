/**
 * Created by Ruben Gomes on 21/05/2015.
 */
public class Exchanger<T> implements IExchanger<T> {

    T msg1,msg2;
    private final Object lock = new Object();

    @Override
    public T exchange(T myMsg, long timeout) throws InterruptedException {
        synchronized (lock){
            T exchangeMsg;
            // if already exists msg1 exchange,notify the waiting thread and return value for the current thread
            if(msg1 != null){
                exchangeMsg = msg1;
                msg2 = myMsg;
                msg1 = null;
                lock.notify();
                return exchangeMsg;
            }
            if(timeout == 0) return null;
            msg1 = myMsg;
            long lastTime = System.currentTimeMillis();
            do{
                try{
                    lock.wait(timeout);
                } catch (InterruptedException e) {
                // thread interrupted and exists ms2 - interrupt thread,
                // notify waiting threads, put null ms1 and ms2 and return exchangeMsg
                    if(msg2 != null){
                        exchangeMsg = msg2;
                        msg1 = msg2 = null;
                        lock.notify(); // notify because one thread can be waiting for another thread to exchange msg
                        Thread.currentThread().interrupt();
                        return exchangeMsg;
                    }
                    // if exit by interrupt and msg2 is null the running thread must clear his msg and throw exception
                    msg1 = null;
                    throw e;
                }
                if( msg2 != null){
                    exchangeMsg = msg2;
                    msg1 =  msg2 = null;
                    lock.notify(); // notify because one thread can be waiting for another thread to exchange msg
                    return exchangeMsg;
                }
                //if exit by timeout, the running thread must clear his msg
                if(timeout > 0){
                    long currTime = System.currentTimeMillis();
                    long deltaTime = currTime -lastTime;
                    timeout = (deltaTime >= timeout)? 0 : timeout -deltaTime;
                    msg1 = null;
                }
            }while(timeout > 0);
            return null;
        }
    }
}
