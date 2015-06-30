using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Threading;

namespace ResolvedQuestions.Exer1
{
    class UnsafeSpinSemaphore
    {
        private readonly int maximum;
        private int count;
        public UnsafeSpinSemaphore(int i, int m) 
        {
            if (i < 0 || m <= 0)
                throw new ArgumentException("i/m");
                count = i; maximum = m;
        }

        public void Acquire() 
        {
          
            SpinWait sw = new SpinWait();
            
            while (count == 0)
                sw.SpinOnce();
            count -= 1;
        }
        
        public void Release(int rs) {
            if (rs < 0 || rs + count > maximum)
                throw new ArgumentException("rs");
            count += rs;
        }
    }
}
