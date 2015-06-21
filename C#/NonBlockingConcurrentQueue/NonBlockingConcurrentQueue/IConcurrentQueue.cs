using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace NonBlockingConcurrentQueue
{
    interface IConcurrentQueue<T>
    {
        void Put(T elem);
        bool IsEmpty();
        T TryTake();
    }
}
