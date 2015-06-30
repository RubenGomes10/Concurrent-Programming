using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace ResolvedQuestions.Exer1
{
    class SafeSpinSemaphore
    {

        private readonly int maximum; // atomic variable (granted)
        private int count;
        public SafeSpinSemaphore(int i, int m) 
        {
            if (i < 0 || m <= 0)
                throw new ArgumentException("i/m");
                count = i; maximum = m;
        }

        public void Aquire()
        {
            SpinWait sw = new SpinWait();
            do
            {
                int oldCount = Volatile.Read(ref count);
                if (oldCount == Volatile.Read(ref count))
                {
                    if (count == 0)
                        sw.SpinOnce();
                    else
                    {
                        int newValue = oldCount + 1;
                        if (CompareAndSwap(ref count, oldCount, newValue))
                        {
                            return;
                        }
                    }

                }
            } while (true);
        }

        public void Release(int rs)
        {
            do
            {
                int oldValue = Volatile.Read(ref count);
                
                if (oldValue == Volatile.Read(ref count))
                {
                    if (rs < 0 || oldValue + rs > maximum)
                        throw new ArgumentException("rs");
                    else
                    {
                        if (CompareAndSwap(ref count, oldValue, oldValue + rs))
                        {
                            return;
                        }
                    }
                }
            } while (true);
        }

     

        private bool CompareAndSwap(ref int oldValue, int expected, int newValue)
        {
            return expected == Interlocked.CompareExchange(ref oldValue, newValue, expected);
        }
    }
}
