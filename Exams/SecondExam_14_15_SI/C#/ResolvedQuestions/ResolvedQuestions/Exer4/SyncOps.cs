using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ResolvedQuestions.Exer4
{
    class SyncOps
    {
        public interface Services
        {
            String GetDeviceAddress(int devId);
            int GetVersionFromDevice(String addr);
            int GetStoredVersion(int devId);
        }

        public bool CheckDeviceVersion(Services svc, int devId)
        {
            String addr = svc.GetDeviceAddress(devId);
            int devVer = svc.GetVersionFromDevice(addr);
            int stoVer = svc.GetStoredVersion(devId);
            return devVer == stoVer;
        }
    }
}
