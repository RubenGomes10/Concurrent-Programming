using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace FutureHolderSynchronizer
{
    class FutureHolder<T> : IFutureHolder<T>
    {
        private T value;
        private readonly Object _lock = new Object();
        public void SetValue(T value)
        {
            lock (_lock)
            {
                if (IsValueAvailable())
                {
                    throw new InvalidOperationException();
                }
                else
                {
                    this.value = value;
                    Monitor.PulseAll(_lock);
                }
            }
        }

       

        public T GetValue(long timeout)
        {
            lock (_lock)
            {
                if(IsValueAvailable()) return value;
                if (timeout == 0) return default(T);
                int lastTime = (timeout != Timeout.Infinite) ? Environment.TickCount : 0;
                do
                {
                    try
                    {
                        Monitor.Wait(_lock, (int)timeout);
                    }
                    catch (ThreadInterruptedException)
                    {
                        if (IsValueAvailable())
                        {
                            Thread.CurrentThread.Interrupt();
                            return value;
                        }
                        throw;
                    }
                    if (IsValueAvailable())
                    {
                        return value;
                    }
                    if (SyncUtils.AdjustTimeout(ref lastTime, ref timeout) == 0) 
                        return default(T);

                } while (true);
            }
        }

        public bool IsValueAvailable()
        {
            return this.value != null;
        }
    }
}
