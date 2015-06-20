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
            if (IsValueAvailable())
            {
                throw new InvalidOperationException("Value already inserted!");
            }
            else
            {
                ValueHolder<T> newHolder = new ValueHolder<T>(value);
                Interlocked.CompareExchange(ref holder, newHolder, null);
            }
        }

        public T GetValue(long timeout)
        { 
            do
            {
                int lastTime = (timeout != Timeout.Infinite) ? Environment.TickCount : 0;
                if (IsValueAvailable()) return holder.value;
                if (timeout == 0) return default(T);
                int deltaTime = Environment.TickCount - lastTime;
                timeout = (deltaTime >= timeout) ? 0 : timeout - deltaTime;           
            } while (true);
        }

        public bool IsValueAvailable()
        {
            return holder != null;
        }
    }
}
