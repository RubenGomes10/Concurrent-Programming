/**
 * Created by Ruben Gomes on 20/05/2015.
 */
public class FutureHolder<T> implements IFutureHolder<T> {

    private T value ;
    private final Object lock = new Object();

    @Override
    public void setValue(T value) {
        synchronized (lock){
            if(isValueAvailable()){
                throw new IllegalStateException();
            }else{
                this.value = value;
                lock.notifyAll();
            }
        }
    }

    @Override
    public T getValue(long timeout) throws InterruptedException {
        synchronized (lock){
            if(isValueAvailable()) return value;
            if(timeout == 0) return null;
            int lastTime = (timeout != -1)? (int) System.currentTimeMillis() : 0;
            do{
                try{
                    lock.wait(timeout);
                } catch (InterruptedException e) {
                    if(isValueAvailable()){
                        Thread.currentThread().interrupt();
                        return value;
                    }
                   throw new InterruptedException(e.getMessage());
                }
                if(isValueAvailable()){
                    return value;
                }
                if(SyncUtils.AdjustTimeout(lastTime,timeout) == 0){
                    return null;
                }
            }while(true);
        }
    }

    @Override
    public boolean isValueAvailable() {
        return this.value != null;
    }
}
