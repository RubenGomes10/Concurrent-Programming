using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RendezvousChannelSynchronizer
{
    interface Token<T>
    {
        T Value();
    }
}
