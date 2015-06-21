using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace NonBlockingFutureHolderSynchronizer
{
    class ValueHolder<T> where T : class
    {
        public volatile T value;
        public ValueHolder(T value)
        {
            this.value = value;
        }
    }

    class NonBlockingFutureHolder<T> where T : class 
    {
        ValueHolder<T> holder;

        public void SetValue(T value)
        {
            do
            {
                ValueHolder<T> oldHolder = Volatile.Read(ref holder);
                if (IsValueAvailable())
                {
                    throw new InvalidOperationException("Value already inserted!");
                }
                else
                {
                    ValueHolder<T> newHolder = new ValueHolder<T>(value);
                    if (Interlocked.CompareExchange(ref holder, newHolder, oldHolder) == oldHolder)
                    {
                        return;
                    }
                }
            } while (true);
        }

        public T GetValue(int timeout)
        {
            if (IsValueAvailable()) return holder.value;
            if (timeout == 0) throw new TimeoutException("Timeout is 0!");
            do
            {
                int lastTime = (timeout != Timeout.Infinite) ? Environment.TickCount : 0;
                if (IsValueAvailable()) return holder.value;
                int deltaTime = Environment.TickCount - lastTime;
                timeout = (deltaTime >= timeout) ? 0 : timeout - deltaTime;
                if(timeout == 0) throw new TimeoutException("Timeout expired!");
            } while (true);
        }

        public bool IsValueAvailable()
        {
            return holder != null;
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
