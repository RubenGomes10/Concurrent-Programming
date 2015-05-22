using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace ExchangerSynchronizer
{
    public class Exchanger<T> : IExchanger<T>
    {
        T msg1, msg2;
        public readonly object _lock = new object();

        public T Exchange(T myMsg, int   timeout)
        {
            lock (_lock)
            {
                T exchangeMsg;
                // if already exists msg1 exchange,notify the waiting thread and return value for the current thread
                if (msg1 != null)
                {
                    msg2 = myMsg;
                    exchangeMsg = msg1;
                    msg1 = default(T);
                    Monitor.Pulse(_lock);
                    return exchangeMsg;
                }
                if(timeout == 0) return default(T);
                msg1 = myMsg;
                int lastTime = (timeout != Timeout.Infinite) ? Environment.TickCount : 0;
                do
                {
                    try
                    {
                        Monitor.Wait(_lock, timeout);
                    }
                    catch (ThreadInterruptedException)
                    {
                        // thread interrupted and exists ms2 - interrupt thread,
                        // notify waiting threads, put null ms1 and ms2 and return exchangeMsg
                        if (msg2 != null)
                        {
                            exchangeMsg = msg2;
                            msg1 = msg2 = default(T);
                            Monitor.Pulse(_lock);
                            Thread.CurrentThread.Interrupt();
                            return exchangeMsg;
                        }
                        // if exit by interrupt and msg2 is null the running thread must clear his msg and throw exception
                        msg1 = default(T);
                        throw;
                    }
                    if (msg2 != null)
                    {
                        exchangeMsg = msg2;
                        msg1 = msg2 = default(T);
                        Monitor.Pulse(_lock); // notify because one thread can be waiting for another thread to exchange msg
                        return exchangeMsg;
                    }
                    //if exit by timeout, the running thread must clear his msg
                    if (AdjustTimeout(ref lastTime, ref timeout) == 0)
                    {
                        msg1 = default(T);
                        return msg1;
                    }
                } while (true);
            }    
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
