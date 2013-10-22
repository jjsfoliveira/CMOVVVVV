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

        public string DoWork(string index)
        {
            return "eagoracaralho" + index;
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
            catch (DbEntityValidationException e)
            {
                foreach (var eve in e.EntityValidationErrors)
                {
                    Console.WriteLine("Entity of type \"{0}\" in state \"{1}\" has the following validation errors:",
                        eve.Entry.Entity.GetType().Name, eve.Entry.State);
                    foreach (var ve in eve.ValidationErrors)
                    {
                        Console.WriteLine("- Property: \"{0}\", Error: \"{1}\"",
                            ve.PropertyName, ve.ErrorMessage);
                    }
                }
                throw;
            }
            string token = generateToken();

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
                    return generateToken();
                }
            }
                return "0";
        }
    }
}
