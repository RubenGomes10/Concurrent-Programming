package dataDistributor;

import java.util.List;

/**
 * Created by Ruben Gomes on 30/06/2015.
 */
public class ConsumerThread<D> extends Thread {


    private DataDistributor<D> dataDistributor;
    private List<D> listTake;
    private int id;

    public ConsumerThread(DataDistributor<D> dataDistributor, int id){
        this.dataDistributor = dataDistributor;
        this.id = id;
    }


    public void run(){
        System.out.printf("Consumer Thread with id %d try take data \n",id);

        try {
            listTake = dataDistributor.Take();
        } catch (InterruptedException e) {
            System.out.println("Interrupted Exception occurred -"+ e.getMessage());
        }

        System.out.printf("Consumer Thread with id %d take data successfully! \n",id);

    }

    public List<D> getDataTaked(){
        return this.listTake;
    }
}
