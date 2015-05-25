import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Ruben Gomes on 24/05/2015.
 */
public class RendezvousChannel<S,R> {

    public static class TokenClient<T, R> implements Token<T> {
        private Condition cond;
        private boolean rendDone = false, pending = false;
        private T val;
        private R response;

        public TokenClient(T val, Condition cond) {
            this.val = val;
            this.cond = cond;

        }

        @Override
        public T value() {

            return val;
        }
    }

    public static class RequestServerItem<T> {
        private Condition cond;
        private boolean rendAccepted = false;
        private Token<T> val;

        public RequestServerItem(Condition cond) {
            this.cond = cond;
        }
    }



    private final Lock lock = new ReentrantLock();
    private List<TokenClient<S, R>> queueRequest = new ArrayList<TokenClient<S, R>>();
    private List<RequestServerItem<S>> queueServer = new ArrayList<RequestServerItem<S>>();

    public R request(S service, long timeout) throws TimeoutException,InterruptedException {
        lock.lock();
        try {
            TokenClient<S, R> token = new TokenClient<S, R>(service,lock.newCondition()); // put a service and a new Condition Lock
            if (queueServer.isEmpty()) { // if queue server is empty and timeout expires throw TimeoutException
                if(timeout == 0)
                    throw new TimeoutException("Timeout is 0");
                queueRequest.add(token);// if not expires, add request and start wait until not pending
                do {
                    long start = System.currentTimeMillis();
                    try {
                        token.cond.awaitNanos(timeout);

                    } catch (InterruptedException e) {
                        if (token.pending) { //if is pending wait until rend is done
                            do {
                                token.cond.await();
                            } while (!token.rendDone); // when its done interrupt current Thread and return token response
                            Thread.currentThread().interrupt();
                            return token.response;
                        }
                        queueRequest.remove(token); // if not pending remove request and throw exception
                        throw e;
                    }
                    long end = System.currentTimeMillis();
                    long delta = end - start;
                    timeout = (delta >= timeout ? 0 : timeout - delta);
                    if (timeout == 0) {
                        queueRequest.remove(token);
                        throw new TimeoutException("timeout exceeded");
                    }
                } while (!token.pending);
            } else { // if queue server is not empty remove item and
                RequestServerItem<S> item = queueServer.remove(0);
                item.val = token;   // save token into server into
                item.rendAccepted = true; // rend its accepted
                item.cond.signal(); // notify waiting thread
            }
            do { // waits until token have rend done
                token.cond.await();
            } while (!token.rendDone); // when its rendDone return token response
            return token.response;
        } finally { // final unlock lock
            lock.unlock();
        }
    }


    @SuppressWarnings("unchecked")
    public Token<S> accept(long timeout) throws TimeoutException,InterruptedException {
        lock.lock();
        try {
            RequestServerItem<S> item = new RequestServerItem<S>(lock.newCondition()); // adds new condition lock
            TokenClient<S,R> token;
            if (queueRequest.isEmpty()) { // if request queue is empty
                if(timeout == 0)
                    throw new TimeoutException("Timeout is 0");
                queueServer.add(item);  // add requestServerItem into server queue and waits until item have rend Accepted
                do {
                    long start = System.currentTimeMillis();
                    try {
                        item.cond.awaitNanos(timeout);
                    } catch (InterruptedException e) {
                        if (item.rendAccepted) {    // if rend are accepted
                            token = (TokenClient<S,R>)item.val;// save item val into token
                            token.pending = true; // pending become true
                            token.cond.signal(); // notify waiting thread
                            Thread.currentThread().interrupt(); // interrupt current thread and return token
                            return token;
                        }
                        queueServer.remove(item); // if ins`t accepted remove item from queue server and throw exception
                        throw e;
                    }
                    long end = System.currentTimeMillis();
                    long delta = end - start;
                    timeout = (delta >= timeout ? 0 : timeout - delta);

                    if (timeout == 0) { // if timeout expires remove item from server queue , throw timeoutException
                        queueServer.remove(item);
                        throw new TimeoutException("timeout exceeded");
                    }
                } while (!item.rendAccepted);
                token = (TokenClient<S,R>)item.val; // if rend accepted save item value into token
            }
            else{// if queue request ins`t empty remove first token
                token = queueRequest.remove(0);
            }
            token.pending = true; // set pending a true
            token.cond.signal(); // notify waiting thread and return token
            return token;

        } finally { // final unlock lock
            lock.unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public void reply(Token<S> token, R response) {
        lock.lock();
        try {

            ((TokenClient<S, R>) token).response = response; // save response
            ((TokenClient<S, R>) token).rendDone = true; // alert rend its done
            ((TokenClient<S, R>) token).cond.signal(); // and notify waiting condition thread

        } finally {
            lock.unlock();
        }
    }

}
