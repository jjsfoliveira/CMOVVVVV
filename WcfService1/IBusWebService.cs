using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace WcfService1
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IBusWebService" in both code and config file together.
    [ServiceContract]
    public interface IBusWebService
    {
        [OperationContract]
        [WebGet(UriTemplate = "/{index}", ResponseFormat = WebMessageFormat.Json)]
        string DoWork(string index);

        [OperationContract]
        [WebGet(UriTemplate = "/createuser?name={name}&password={password}&creditcard={creditcard}&email={email}", ResponseFormat = WebMessageFormat.Json)]
        string createUser(string name, string password, string creditcard, string email);

        [OperationContract]
        [WebGet(UriTemplate = "/login?email={email}&password={password}", ResponseFormat = WebMessageFormat.Json)]
        string login(string email, string password);

        
        [OperationContract]
        [WebGet(UriTemplate = "/spots?name={name}&android_id={android_id}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        int AddSpot(string name, string android_id);
        /*
        [OperationContract]
        [WebGet(UriTemplate = "/validations?type={spot}&androi_id={android_id}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        */
    }
}
