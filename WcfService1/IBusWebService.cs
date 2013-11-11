using BusServer;
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
        [WebGet(UriTemplate = "/createuser?name={name}&password={password}&creditcard={creditcard}&email={email}", ResponseFormat = WebMessageFormat.Json)]
        string createUser(String name, String password, String creditcard, String email);
        [OperationContract]
        [WebGet(UriTemplate = "/login?email={email}&password={password}", ResponseFormat = WebMessageFormat.Json)]
        User login(string email, string password);

		[OperationContract]
        [WebGet(UriTemplate = "/spots?name={name}&android_id={android_id}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        int AddSpot(string name, string android_id);


		[OperationContract]
        [WebGet(UriTemplate = "/getuserinfo?token={token}", ResponseFormat = WebMessageFormat.Json)]
        String getUserInfo(string token);

        [OperationContract]
        [WebGet(UriTemplate = "/buyTicket?token={token}&n1={n1}&n2={n2}&n3={n3}", ResponseFormat = WebMessageFormat.Json)]
        string buyTicket(string token,int n1, int n2, int n3);

        [OperationContract]
        [WebGet(UriTemplate = "/activateTicket?token={token}&tickettype={ticketType}&spot={spot}", ResponseFormat = WebMessageFormat.Json)]
        string activateTicket(string token, int ticketType, int spot);    }
}
