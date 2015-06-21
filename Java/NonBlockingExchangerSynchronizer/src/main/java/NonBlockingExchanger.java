import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by Ruben Gomes on 21/06/2015.
 */
public class NonBlockingExchanger<T> {

    public class MessageHolder<T> {
        volatile T msg1, msg2;
        volatile boolean isDone;

        public MessageHolder(T msg) {
            this.msg1 = msg;
            this.isDone = false;
        }
    }

    private final AtomicReference<MessageHolder<T>> msgRef = new AtomicReference<MessageHolder<T>>(null);

    public T exchange(T myMsg, long timeout) throws InterruptedException, TimeoutException {
        do{
            MessageHolder<T> oldMsgHolder = msgRef.get();
            //first thread
            if(oldMsgHolder == null){
                if(timeout == 0) throw new TimeoutException("Timeout is 0!");
                MessageHolder<T> newMsgHolder = new MessageHolder<T>(myMsg);
                if(msgRef.compareAndSet(oldMsgHolder,newMsgHolder)) { // change atomically the value of AtomicReference
                    try {
                        long start = System.currentTimeMillis();
                        synchronized (msgRef) { // atomically wait`s until another thread insert the 2nd message or timeout expires.
                            if (!newMsgHolder.isDone) {
                                msgRef.wait(timeout);
                            }
                        }
                        long deltaTime = System.currentTimeMillis() - start;
                        timeout = (deltaTime >= timeout) ? 0 : timeout - deltaTime;

                        // if thread interrupted and 2nd message is inserted interrupt current thread and return 2nd message
                    } catch (InterruptedException e) {
                        if (newMsgHolder.isDone) {
                            Thread.currentThread().interrupt();
                            return newMsgHolder.msg2;
                        }//if not reset messages in messageHolder atomically and throw InterruptedException
                        msgRef.compareAndSet(newMsgHolder, null);
                        throw e;
                    }

                    if (newMsgHolder.isDone) { // if 2nd is present return that
                        return newMsgHolder.msg2;
                    }

                    if (timeout == 0) { // if timeout expires reset messages atomically
                        msgRef.compareAndSet(newMsgHolder, null);
                        throw new TimeoutException("Timeout expired!");
                    }
                }
                //2nd thread
            }else{
                if(msgRef.compareAndSet(oldMsgHolder,null)) { // reset atomicReference
                    oldMsgHolder.msg2 = myMsg; // put the 2nd message in reference for the Message Holder
                    synchronized (msgRef){ // atomically set work done( 2nd message is present) and notify de current AtomicReference
                        oldMsgHolder.isDone = true;
                        msgRef.notify();
                    }
                    return oldMsgHolder.msg1; // return msg1
                }
            }
        }while(true);
    }

}
