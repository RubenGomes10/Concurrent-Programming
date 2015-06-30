package bulletinBoard;

/**
 * Created by Ruben Gomes on 30/06/2015.
 */
public class BulletinBoard<W> {

    private static class Notification<W>{
        W warning;
        int validity;
        boolean isValid;

        public Notification(W warning , int validity){
            this.warning = warning;
            this.validity = validity;
            this.isValid = true;
        }
    }

    private Notification<W> notification;
    private Notification<W> oldNotification;
    private Object lock = new Object();


    public void Post(W warning, int validity){
        synchronized(lock){
            notification = new Notification<W>(warning,validity);
            if(validity == 0) {
                oldNotification = notification;
                notification.isValid = false;
            }
                lock.notifyAll();
        }
    }

// miss update time
    public W Receive() throws InterruptedException {
        synchronized (lock){
            if(notification.isValid){
                return notification.warning;
            }
            do{
                try{
                    lock.wait();
                }catch (InterruptedException e){
                    if(oldNotification != null){// if oldNotification is not null - its because clear is called or validity is 0
                        Thread.currentThread().interrupt();
                        lock.notifyAll();
                        return oldNotification.warning;
                    }
                    if(notification.isValid){
                        Thread.currentThread().interrupt();
                        lock.notifyAll();
                        return notification.warning;
                    }
                    throw e;
                }
                if(oldNotification != null){ // if oldNotification is not null - its because clear is called or validity is 0
                    return oldNotification.warning;
                }
                if(notification.isValid)
                    return notification.warning;

            }while(true);
        }
    }


    public void Clear(){
        synchronized(lock){
            if(notification != null){
                oldNotification = notification;
                notification.isValid = false;
                lock.notifyAll();
            }
        }
    }
}
