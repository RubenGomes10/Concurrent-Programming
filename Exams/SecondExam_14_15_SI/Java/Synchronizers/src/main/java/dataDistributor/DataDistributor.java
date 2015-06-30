package dataDistributor;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ruben Gomes on 30/06/2015.
 */
public class DataDistributor<D> {

    private int maxElems = 1;
    private Object lock = new Object();
    private LinkedList<D> dataQueue = new LinkedList<D>();
    private LinkedList<Integer> consumers = new LinkedList<Integer>();

    public DataDistributor(int n){
        if(n > 0)
            this.maxElems = n;
    }


    public void Put(List<D> data){
        synchronized(lock){
            while(data.size() > 0){
                D value = data.remove(0);
                dataQueue.add(value);
            }
            lock.notifyAll();
        }

    }


    public List<D> Take(){
        synchronized (lock){
            int id = (int)Thread.currentThread().getId();
            consumers.addFirst(id);
            if(dataQueue.size() == 0) { // wait if not exist work!
                do{
                    try{
                        lock.wait();
                    }catch (InterruptedException e){// if interrupted abandon process remove thread from consumers queue
                        consumers.remove(id);
                    }
                }while (dataQueue.size() == 0);
            }
            //if exist work
            LinkedList<D> retData = new LinkedList<D>();
            int maxToTake = maxElems;
            while(maxToTake-->0 && dataQueue.size()>0){
                D value = dataQueue.removeFirst();
                retData.add(value);
            }
            //remove currentThread since its done all of this work!
            consumers.removeFirst();
            lock.notifyAll();
            return retData;
        }
    }
}
