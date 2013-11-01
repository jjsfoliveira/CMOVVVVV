using BusServer;
using System;
using System.Collections.Generic;
using System.Data.Entity.Validation;
using System.Linq;
using System.Runtime.Serialization;
using System.Security.Cryptography;
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

        private string generateToken()
        {
            RNGCryptoServiceProvider rng = new RNGCryptoServiceProvider();
            var buffer = new byte[8]; // 8 bytes for a long
            rng.GetBytes(buffer);
            ulong result = BitConverter.ToUInt64(buffer, 0); // unsigned to avoid having to use Abs
            var token = result.ToString("D10"); // pads the result to 10 digits
            return token.Substring(token.Length - 10); // strip out extra digits, if any
        }

        public string createUser(string name, string password, string creditcard, string email) // TODO verificar se ja existe outro igual
        {
            User u = new User();
            u.name = name;
            u.password = password;
            u.creditcard = creditcard;
            u.email = email;

            db.User.Add(u);
            //db.SaveChanges();

            try
            {
                // Your code...
                // Could also be before try if you know the exception occurs in SaveChanges

                db.SaveChanges();
            }
            catch (DbEntityValidationException ex)
            {
                // Retrieve the error messages as a list of strings.
                var errorMessages = ex.EntityValidationErrors
                        .SelectMany(x => x.ValidationErrors)
                        .Select(x => x.ErrorMessage);

                // Join the list to a single string.
                var fullErrorMessage = string.Join("; ", errorMessages);

                // Combine the original exception message with the new one.
                var exceptionMessage = string.Concat(ex.Message, " The validation errors are: ", fullErrorMessage);

                // Throw a new DbEntityValidationException with the improved exception message.
                throw new DbEntityValidationException(exceptionMessage, ex.EntityValidationErrors);
            }

            return db.User.Count().ToString();
            //return token;
        }

        public string login(string email, string password)
        {
            User myUser = db.User.SingleOrDefault(user => user.email == email);
            if (myUser != null)
            {   
                if (myUser.password == password)
                {
                    string s = generateToken();
                    myUser.authtoken = s;
                    db.SaveChanges();
                    return s;
                }
            }
                return "0";
        }
        public int AddSpot(string name, string android_id)
        {   
            Spot s = db.Spot.SingleOrDefault(p => p.android_id == android_id);
           
            if (s != null)
            {
                return s.Id;
            }
            else
            {
                s = new Spot{name = name, android_id = android_id};
                db.Spot.Add(s);
                db.SaveChanges();

                return s.Id;
            }
        }


        public User getUserInfo(string token)
        {
            return db.User.SingleOrDefault(user => user.authtoken == token);
        }

        public string buyTicket(string token, int n1, int n2, int n3)
        {
            User u = db.User.SingleOrDefault(user => user.authtoken == token);
            if(u!= null)
            {
                if (n1 + n2 + n3 >= 10)
                {
                }
                u.t1 += n1;
                u.t2 += n2;
                u.t3 += n3;
            }
            return "0";
        }

        public string activateTicket(string token, int ticketType, int spot)
        {
            User u = db.User.SingleOrDefault(user => user.authtoken == token);
            if (u != null)
            {
                Validation v = db.Validation.SingleOrDefault(p => p.user == u.Id);
                if (v == null)
                {
                    switch (ticketType)
                    {
                        case 1:
                            if (u.t1 > 0)
                                u.t1 -= 1;
                            else
                                return "0";
                            break;
                        case 2:
                            if (u.t2 > 0)
                                u.t2 -= 1;
                            else
                                return "0";
                            break;
                        case 3:
                            if (u.t3 > 0)
                                u.t3 -= 1;
                            else
                                return "0";
                            break;
                    }
                    v = new Validation();
                    v.type = ticketType;
                    v.user = u.Id;
                    v.spot = spot;
                }
                else
                {
                    /*
                    TimeSpan t = v.stamp - DateTime.Now;
                    int t1;
                    switch (v.type)
                    {
                        case 1:
                            t1 = 60;
                            break;
                        case 2:
                            t1 = 30;
                            break;
                        case 3:
                            t1 = 15;
                            break;
                    }
                     * */
                    v.type = ticketType;
                    v.spot = spot;
                }
                db.SaveChanges();
                return "1";
            }
            return "0";
        }
    }
}
