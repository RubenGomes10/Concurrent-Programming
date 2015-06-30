using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace ResolvedQuestions.Exer4
{
    class APMOps
    {
        interface Services
        {
            IAsyncResult BeginGetDeviceAddress(int devId, AsyncCallback cb, object state);
            string EndGetDeviceAddress(IAsyncResult ar);
            IAsyncResult BeginGetVersionFromDevice(string addr, AsyncCallback cb, object state);
            int EndGetVersionFromDevice(IAsyncResult ar);
            IAsyncResult BeginGetStoredVersion(int devId, AsyncCallback cb, object state);
            int EndGetStoredVersion(IAsyncResult ar);
        }

        //BeginGetDeviceAddress and BeginGetStoredVersion can process in parallel mode 
        //since BeginGetStoredVersion don`t need addr which returned from return of EndGetDeviceAddress call.
        public IAsyncResult BeginCheckDeviceVersion(Services services, int devId, AsyncCallback cb, object state)
        {
            AsyncCallback
                onGetDeviceAddress = null,
                onGetVersionFromDevice = null,
                onGetStoredVersion = null;
            var gar = new GenericAsyncResult<bool>(cb,state);

            int count = 2; // control completion
            int devVersion = -1, stoVersion = -1;

            //getAddr and begin getVersionFromDevice asynchronously
            onGetDeviceAddress = (ar) =>
            {
                string addr = services.EndGetDeviceAddress(ar);
                services.BeginGetVersionFromDevice(addr, onGetVersionFromDevice, null);
            };

            //End getVersionFromDevice asynchronously and check if devices have the same version if it was finished in last
            onGetVersionFromDevice = (ar) =>
            {
                devVersion = services.EndGetVersionFromDevice(ar);
                if (Interlocked.Decrement(ref count) == 0)
                {
                    gar.OnComplete(devVersion == stoVersion, null);

                }
            };

            //End getStoredVersion asynchronously and check if devices have the same version if it was finished in last
            onGetStoredVersion = (ar) =>
            {
                stoVersion = services.EndGetStoredVersion(ar);
                if (Interlocked.Decrement(ref count) == 0)
                {
                    gar.OnComplete(stoVersion == devVersion, null);
                }
            };

            services.BeginGetDeviceAddress(devId, onGetDeviceAddress, null);
            services.BeginGetStoredVersion(devId, onGetStoredVersion, null);
            
            return gar;
        }

        public bool EndCheckDeviceVersion(IAsyncResult ar)
        {
            return ((GenericAsyncResult<bool>)ar).Result;
        }
    }
}
