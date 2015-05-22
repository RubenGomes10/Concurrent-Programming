import java.util.LinkedList;
import java.util.function.IntPredicate;

/**
 * Created by Ruben Gomes on 21/05/2015.
 */
public class MessageQueue<T> implements IMessageQueue<T> {


    private class Request{
        public T msg;
        public int id;

        public Request(T msg, int id){
            this.msg = msg;
            this.id = id;
        }
    }

    private final Object lock = new Object();

    LinkedList<Request> requests = new LinkedList<Request>();


    @Override
    public void send(T msg, int id) {
        synchronized (lock){
            requests.add(new Request(msg,id));
            lock.notifyAll();
        }
    }

    @Override
    public T receive(IntPredicate selector, long timeout) throws InterruptedException {
        synchronized (lock) {
            if (requests.size() == 0) return null;
            if (timeout == 0) return null;
            for (Request req : requests) { // if match remove from list and return value - don´t wait
                if (selector.test(req.id)) {
                    requests.remove(req);
                    lock.notifyAll();
                    return req.msg;
                }
            }
            long lastTime = System.currentTimeMillis();
            do{
                try{
                    lock.wait(timeout);
                }catch(InterruptedException e){
                    if(requests.size() > 0) { //if queue not empty verify match
                        for (Request req : requests) { // if match remove from list and return value
                            if (selector.test(req.id)) {
                                requests.remove(req);
                                Thread.currentThread().interrupt();
                                lock.notifyAll();
                                return req.msg;
                            }
                        }
                    }//if not match throw exception
                    throw e;
                }
                if(requests.size()>0){//if queue not empty verify match
                    for (Request req : requests) { // if match remove from list and return value
                        if (selector.test(req.id)) {
                            requests.remove(req);
                            lock.notifyAll();
                            return req.msg;
                        }
                    }
                }//if not match verify timeout
                if(timeout > 0){
                    long currTime = System.currentTimeMillis();
                    long deltaTime = currTime - lastTime;
                    timeout = (deltaTime >= timeout)? 0 : timeout - deltaTime;
                }
            }while(timeout>0);
            return null;
        }

    }
}
