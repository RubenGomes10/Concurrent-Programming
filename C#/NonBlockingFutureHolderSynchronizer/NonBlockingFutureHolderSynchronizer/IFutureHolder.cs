using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NonBlockingFutureHolderSynchronizer
{
    interface IFutureHolder<T>
    {
        void SetValue(T value);
        T GetValue(int timeout);
        bool IsValueAvailable();
    }
}
