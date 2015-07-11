package transferQueue;

import java.util.LinkedList;

/**
 * Created by Ruben Gomes on 11/07/2015.
 */
public class TransferQueue<E> {


    public class Request<E>{
        E msg;
        boolean received;
        public Request(E msg,boolean received){
            this(msg);
            this.received = received;
        }

        public Request(E msg){
            this.msg = msg;
        }
    }

    private LinkedList<Request> requests = new LinkedList<Request>();
    private LinkedList<Integer> consumers = new LinkedList<Integer>();
    private Object lock = new Object();

    public void put (E msg){
        synchronized (lock){
            requests.add(new Request(msg,true)); // don`t need to wait so just insert a new Request with received a true an notify
            lock.notifyAll();
        }
    }

    public void transfer(E msg) throws InterruptedException{
        synchronized (lock){
            Request<E> newRequest = new Request<E>(msg); // request received is false
            requests.add(newRequest);// FIFO
            do {//wait until msg is received
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    if (newRequest.received) { // if occurred an exception verify if request as received
                        requests.remove(newRequest); // if so remove request from the requests queue
                        Thread.currentThread().interrupt(); // interrupt current thread and notify data changed
                        lock.notifyAll();
                        return;
                    }
                    requests.remove(newRequest); // if msg are`t  received just abandon process
                    throw e;
                }
                if(newRequest.received) { // if received remove from the requests queue
                    requests.remove(newRequest);
                    return;
                }
            }while(true);
        }
    }


    public E Take() throws InterruptedException{
        synchronized (lock){
            int id = (int)Thread.currentThread().getId();
            consumers.add(id);
            if(requests.size() == 0){ // waits for work / until have requests
                do{
                    try{
                        lock.wait();
                    }catch (InterruptedException e){
                        consumers.remove(id);
                        throw e;
                    }
                }while (requests.size() == 0);
            }
            // have request
            Request<E> request = requests.getFirst(); // get First - FIFO
            request.received = true; // put this request received for the waiting thread in transfer be notified
            consumers.removeFirst(); // remove consumer thread - FIFO or LIFO don`t maters in this case
            lock.notifyAll();// notifyAll waiting threads and return msg received
            return request.msg;
        }
    }


}
