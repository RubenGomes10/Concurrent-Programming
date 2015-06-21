using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace NonBlockingExchangerSynchronizer
{
    class MessageHolder<T> where T : class
    {
        public volatile T msg1, msg2;
        internal volatile bool IsDone;
        internal object _lock = new object();

        public MessageHolder(T myMsg)
        {
            this.msg1 = myMsg;
        }
    }


    class NonBlockingExchanger<T> where T : class
    {
        private MessageHolder<T> msgHolder; // holder used as atomicReference
        public T Exchange(T myMsg, int timeout)
         {

             do
             {
                 MessageHolder<T> oldMsgHolder = Volatile.Read(ref msgHolder); // atomically get the messageHolder

                 if (oldMsgHolder == null) // 1st thread
                 {
                     if (timeout == 0) throw new TimeoutException("Timeout is 0!");
                     MessageHolder<T> newMsgHolder = new MessageHolder<T>(myMsg);
                     if (CompareAndSwap(ref msgHolder, oldMsgHolder, newMsgHolder)) // change atomically the value of AtomicReference
                     {
                         int lastTime = (timeout != Timeout.Infinite) ? Environment.TickCount : 0;
                         try
                         {
                             lock (newMsgHolder._lock)// atomically wait`s until another thread insert the 2nd message or timeout expires.
                             {
                                 if (!newMsgHolder.IsDone)
                                 {
                                     Monitor.Wait(newMsgHolder._lock, timeout);
                                 }
                             }
                         }
                         // if thread interrupted and 2nd message is inserted interrupt current thread and return 2nd message
                         catch (ThreadInterruptedException)
                         {
                             if (newMsgHolder.IsDone)
                             {
                                 Thread.CurrentThread.Interrupt();
                                 return newMsgHolder.msg2;
                             }//if not reset messages in messageHolder atomically and throw InterruptedException
                             CompareAndSwap(ref msgHolder, newMsgHolder, null);
                             throw;
                         }
                         if (newMsgHolder.IsDone)// if 2nd is present return that
                         {
                             return newMsgHolder.msg2;
                         }
                         if (AdjustTimeout(ref lastTime, ref timeout) == 0) // if timeout expires reset messages atomically
                         {
                             CompareAndSwap(ref msgHolder, newMsgHolder, null);
                             throw new TimeoutException("Timeout expired");
                         }
                     }
                 }
                 else // 2nd thread  
                 {
                     if (CompareAndSwap(ref msgHolder, oldMsgHolder, null)) // reset atomicReference
                     {
                         oldMsgHolder.msg2 = myMsg; // put the 2nd message in reference for the Message Holder
                         lock (oldMsgHolder._lock)// atomically set work done( 2nd message is present) and notify de current AtomicReference
                         {
                             oldMsgHolder.IsDone = true;
                             Monitor.Pulse(oldMsgHolder._lock);
                         }
                         return oldMsgHolder.msg1; // return msg1
                     }
                 }
             } while (true);
         }

        private static bool CompareAndSwap<E>(ref E obj, E expected, E newVal) where E : class
        {
            return expected == Interlocked.CompareExchange(ref obj, newVal, expected);
        }


        public static int AdjustTimeout(ref int lastTime, ref int timeout)
        {
            if (timeout != Timeout.Infinite)
            {
                int now = Environment.TickCount;
                int elapsed = (now == lastTime) ? 1 : now - lastTime;
                if (elapsed >= timeout)
                {
                    timeout = 0;
                }
                else
                {
                    timeout -= elapsed;
                    lastTime = now;
                }
            }
            return timeout;
        }

    }
}
