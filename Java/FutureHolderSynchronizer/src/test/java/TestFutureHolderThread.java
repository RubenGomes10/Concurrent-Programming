/**
 * Created by Ruben Gomes on 21/05/2015.
 */
public class TestFutureHolderThread<T> extends Thread {
    FutureHolder<T>  futureHolder;
    long timeout;
    boolean isGetValue;
    T value;

    public TestFutureHolderThread(FutureHolder<T> futureHolder,boolean isGetValue,long timeout,T value){
        this.futureHolder = futureHolder;
        this.timeout = timeout;
        this.isGetValue = isGetValue;
        this.value = value;
    }

    @Override
    public void run() {
        if(isGetValue){
            try {
                System.out.printf(" this thread getValue = %d \n",
                        futureHolder.getValue(timeout))
                ;
            } catch (InterruptedException e) {
                System.out.printf("Thread with id = %d interrupt!!\n",this.getId());
            }
        }else{
            futureHolder.setValue(value);
        }
    }

}

