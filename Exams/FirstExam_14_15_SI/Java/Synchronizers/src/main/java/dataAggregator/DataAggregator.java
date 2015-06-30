package dataAggregator;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ruben Gomes on 30/06/2015.
 */
public class DataAggregator<D> {

    private Object lock = new Object();
    private LinkedList<D> dataQueue = new LinkedList<D>();
    private LinkedList<Integer> consumers = new LinkedList<Integer>();


    public void Put(D data){
        synchronized (lock){
            dataQueue.add(data);
            lock.notifyAll();
        }

    }
    public List<D> TakeAll() throws InterruptedException {
        synchronized (lock){
            int id = (int)Thread.currentThread().getId();
            consumers.addFirst(id);
            if(dataQueue.size() == 0){// if dataQueue is empty wait for work!
                do{
                    try{
                        lock.wait();
                    }catch (InterruptedException e){// if interrupt abandon process
                        consumers.remove(id);
                        throw e;
                    }
                }while(dataQueue.size() == 0);
            }
            //if have work
            LinkedList<D> retData = new LinkedList<D>();
            while(dataQueue.size()>0){
                D value = dataQueue.removeFirst();
                retData.add(value);
            }
            consumers.removeFirst();
            lock.notifyAll();
            return retData;
        }
    }
}
