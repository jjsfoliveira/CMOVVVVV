using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace WcfService1
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "BusWebService" in code, svc and config file together.
    // NOTE: In order to launch WCF Test Client for testing this service, please select BusWebService.svc or BusWebService.svc.cs at the Solution Explorer and start debugging.
    public class BusWebService : IBusWebService
    {
        DatabaseEntities db;

        public BusWebService()
        {
            db = new DatabaseEntities();
        }

        public string DoWork(string index)
        {
            return "eagoracaralho" + index;
        }
    }
}
