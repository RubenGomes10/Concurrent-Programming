using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace FutureHolderSynchronizer
{
    interface IFutureHolder<T>
    {
        void SetValue(T value);
        T GetValue(long timeout);
        bool IsValueAvailable();
    }
}
