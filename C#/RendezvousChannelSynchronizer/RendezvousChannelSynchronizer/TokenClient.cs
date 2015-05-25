using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace RendezvousChannelSynchronizer
{
    class TokenClient<T,R> : Token<T>
    {
        public bool rendDone = false, pending = false;
        public T val;
        public R response;
        Thread thread;
        public TokenClient(T val)
        {
            this.val = val;
            this.thread = Thread.CurrentThread;
        }
        public T Value()
        {
            return val;
        }
    }
}
