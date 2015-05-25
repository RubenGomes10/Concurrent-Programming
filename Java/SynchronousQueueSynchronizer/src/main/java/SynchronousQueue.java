import java.util.LinkedList;

/**
 * Created by Ruben Gomes on 23/05/2015.
 */
public class SynchronousQueue<T> implements ISynchronousQueue<T> {

    private class Request<T> {
        boolean isReceived;
        T msg;

        public Request(T msg, boolean isReceived){
            this.msg = msg;
            this.isReceived = isReceived;
        }
    }

    public class ThreadRequest{
        int id;

        public ThreadRequest(int id){
            this.id = id;
        }
    }


    private static final Object lock = new Object();
    ThreadRequest threadServer ; // for debug and tests
    int idThread = 1;
    LinkedList<Request> requestQueue = new LinkedList<Request>(); // queue for put messages
    public LinkedList<ThreadRequest> threadQueue = new LinkedList<ThreadRequest>(); // queue for consumer threads


    @Override
    public void put(T msg) throws InterruptedException {
        synchronized (lock){
            Request request = new Request(msg,false); // new request
            requestQueue.addLast(request);  // add request in requestQueue
            lock.notifyAll();
            if(request.isReceived){
                return;
            }
            do{
                try{
                    lock.wait();
                }catch (InterruptedException e){
                    if(request.isReceived){
                        Thread.currentThread().interrupt();
                        return;
                    }
                    requestQueue.remove(request);
                    throw e;
                }
                if(request.isReceived){
                    return;
                }
            }while(true);
        }
    }

    @Override
    public T take() throws InterruptedException {
        synchronized (lock){
            ThreadRequest threadToTake = new ThreadRequest(idThread++);
            threadQueue.addFirst(threadToTake);

            if(requestQueue.size() == 0){ // if queue of requests is empty wait for work
                do{
                    try{
                        lock.wait();
                    }catch (InterruptedException e){
                        threadServer = threadToTake; // for debug
                        threadQueue.remove(threadToTake);
                        throw e;
                    }
                }while(requestQueue.size() == 0);
            }
            // if have work get references
            Request request = requestQueue.getFirst();
            request.isReceived = true;// request received
            T msg = (T) request.msg; // get message
            requestQueue.remove(request);// remove first request from queue
            threadServer = threadQueue.getFirst();// save into variable for debug and tests
            threadQueue.removeFirst();
            lock.notifyAll();   // notify waiting threads
            return msg;
        }

    }

}
