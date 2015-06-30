package dataDistributor;

import java.util.List;

/**
 * Created by Ruben Gomes on 30/06/2015.
 */
public class WorkerThread<D> extends Thread {


    private DataDistributor<D> dataDistributor;
    private List<D> listToSend;
    private int id;

    public WorkerThread(DataDistributor<D> dataDistributor, List<D> listToSend, int id){
        this.dataDistributor = dataDistributor;
        this.listToSend = listToSend;
        this.id = id;
    }


    public void run(){
        System.out.printf("Worker Thread with id %d try post data with dim %d \n",id,listToSend.size());
        dataDistributor.Put(listToSend);
        System.out.print("Worker Thread post -> " +  listToSend  + "\n");

    }
}
