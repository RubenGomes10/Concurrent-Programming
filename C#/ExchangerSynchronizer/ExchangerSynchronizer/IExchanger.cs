using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ExchangerSynchronizer
{
    interface IExchanger<T>
    {
        T Exchange(T msg, int timeout);
    }
}
