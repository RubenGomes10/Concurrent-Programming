using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace RendezvousChannelSynchronizer
{
    class RequestServerItem<T>
    {
        public Token<T> val;
        public bool rendAccepted = false;
        Thread thread;
        public RequestServerItem()
        {
            this.thread = Thread.CurrentThread;
        }
    }
}
