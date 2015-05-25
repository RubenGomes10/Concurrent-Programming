import java.util.LinkedList;

/**
 * Created by Ruben Gomes on 24/05/2015.
 */
public class Rw_Semaphore {


    private class Request{
        boolean writeAccessGranted;
        boolean readAccessGranted;
        boolean isWrite;

        public Request(boolean writeAccessGranted, boolean readAccessGranted,boolean isWrite){
            this.writeAccessGranted = writeAccessGranted;
            this.readAccessGranted = readAccessGranted;
            this.isWrite = isWrite;
        }

    }

    private final LinkedList<Request> queue = new LinkedList<Request>();
    private boolean writing = false;
    private int readers = 0;
    private int nrReadersInQueue = 0;
    private int nrWritersInQueue = 0;
    private final Object lock = new Object();




    public void downRead() throws InterruptedException {
        synchronized (lock){
            if(queue.size() == 0 && !writing){
                readers++;
                return;
            }
            Request read = new Request(false,false,false);
            queue.addLast(read);
            nrReadersInQueue++;
            do{
                try{
                    lock.wait();
                } catch (InterruptedException e) {
                    if(read.readAccessGranted){
                        Thread.currentThread().interrupt();
                        return;
                    }
                    queue.remove(read);
                    nrReadersInQueue--;
                    throw e;
                }
                if(read.readAccessGranted){
                    return;
                }
            }while(true);
        }
    }


    public void downWrite() throws InterruptedException {
        synchronized (lock){
            if(readers == 0 && !writing && queue.size() == 0){
                writing = true;
                return;
            }
            Request write = new Request(false,false,true);
            queue.add(write);
            nrWritersInQueue++;
            do{
                try{
                    lock.wait();
                } catch (InterruptedException e) {
                    if(write.writeAccessGranted){
                        Thread.currentThread().interrupt();
                        return;
                    }
                    queue.remove(write);
                    nrWritersInQueue--;
                    if(!writing && nrWritersInQueue == 0 && nrReadersInQueue > 0){
                        do{
                            queue.getFirst().readAccessGranted = true;
                            queue.removeFirst();
                            readers++;
                        }while(--nrReadersInQueue> 0);
                        lock.notifyAll();
                    }
                    throw e;
                }
                if(write.writeAccessGranted){
                    return;
                }
            }while(true);

        }

    }

    public void upRead(){
        synchronized (lock){
            readers--;
            if (readers == 0 && nrWritersInQueue-- > 0) {
                queue.getFirst().writeAccessGranted = true;
                queue.removeFirst();
                writing = true;
                lock.notifyAll();
            } else {
                if(queue.size() != 0) {
                    do {
                        Request first = queue.getFirst();
                        if (!first.isWrite && !writing) {
                            first.readAccessGranted = true;
                            queue.remove(first);
                            readers++;
                            --nrReadersInQueue;
                            lock.notifyAll();
                        }
                    }while(nrReadersInQueue > 0);
                }
            }
        }

    }

    public void upWrite(){
        synchronized (lock){
            writing = false;
            if(nrWritersInQueue > 0){
                if(queue.getFirst().isWrite){
                    queue.getFirst().writeAccessGranted = true;
                    queue.removeFirst();
                    nrWritersInQueue--;
                    writing = true;
                    lock.notifyAll();
                }else{
                 throw new IllegalStateException("Thread don`t acquire write access!!");
                }
            }
        }

    }

    public void downgradeWriter() throws InterruptedException {
        synchronized (lock){
            writing = false;
            if(nrWritersInQueue > 0){
                if(queue.getFirst().isWrite){
                    queue.getFirst().writeAccessGranted = true;
                    queue.removeFirst();
                    nrWritersInQueue--;
                    writing = true;
                    lock.notifyAll();
                    downRead();
                }else{
                    throw new IllegalStateException("Thread don`t acquire write access!!");
                }
            }
        }


    }







}
