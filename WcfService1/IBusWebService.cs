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
        string createUser(string name, string password, string creditcard, string email);
        [OperationContract]
        [WebGet(UriTemplate = "/login?email={email}&password={password}", ResponseFormat = WebMessageFormat.Json)]
        string login(string email, string password);

		[OperationContract]
        [WebGet(UriTemplate = "/spots?name={name}&android_id={android_id}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        int AddSpot(string name, string android_id);


		[OperationContract]
        [WebGet(UriTemplate = "/getuserinfo?token={token}", ResponseFormat = WebMessageFormat.Json)]
        User getUserInfo(string token);

        [OperationContract]
        [WebGet(UriTemplate = "/buyTicket?token={token}&n1={n1}&n2={n2}&n3={n3}", ResponseFormat = WebMessageFormat.Json)]
        string buyTicket(string token,int n1, int n2, int n3);

        [OperationContract]
        [WebGet(UriTemplate = "/activateTicket?user={user}&tickettype={ticketType}&spot={spot}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        String activateTicket(int user, int ticketType, int spot);

        [OperationContract]
        [WebGet(UriTemplate = "/getSpots", ResponseFormat = WebMessageFormat.Json)]
        List<Spot> getSpots();

        [OperationContract]
        [WebGet(UriTemplate = "/validation?spot={spot}", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        List<Validation> validation(int spot);
    }
}
