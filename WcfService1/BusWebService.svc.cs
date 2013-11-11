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

        public string createUser(String name, String password, String creditcard, String email) // TODO verificar se ja existe outro igual
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

        public User login(string email, string password)
        {
            User myUser = db.User.SingleOrDefault(user => user.email == email);
            if (myUser != null)
            {
                if (String.Equals(Regex.Replace(myUser.password, @"\s", ""), password, StringComparison.OrdinalIgnoreCase))
                {
                    string s = generateToken();
                    myUser.authtoken = s;
                    db.SaveChanges();
                    User u = new User();
                    u.name = myUser.name;
                    u.Id = myUser.Id;
                    u.t1 = myUser.t1;
                    u.t2 = myUser.t2;
                    u.t3 = myUser.t3;
                    u.authtoken = myUser.authtoken;
                    u.creditcard = myUser.creditcard;
                    u.Validation = myUser.Validation;
                    return u;
                    
                    //return Regex.Replace(new JavaScriptSerializer().Serialize(myUser), @"\s", "");
                }
            }
            return null;    
            //return "";
        }
        public int AddSpot(string name, string android_id)
        {   
            Spot s = db.Spot.SingleOrDefault(p => p.name == name);
           
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


        public String getUserInfo(string token)
        {
            User u =  db.User.SingleOrDefault(user => user.authtoken == token);
            if(u != null)
                return new JavaScriptSerializer().Serialize(u);
            return "";
        }

        public string buyTicket(string token, int n1, int n2, int n3)
        {
            User u = db.User.SingleOrDefault(user => user.authtoken == token);
            if(u!= null)
            {
                if (n1 + n2 + n3 >= 10)
                {
                    if (n1 > 0)
                        n1++;
                    else
                        if (n2 > 0)
                            n2++;
                        else
                            if (n3 > 0)
                                n3++;
                }
                u.t1 += n1;
                u.t2 += n2;
                u.t3 += n3;
                db.SaveChanges();
            }
            return "0";
        }

        public String activateTicket(int user, int ticketType, int spot)
        {
            User u = db.User.SingleOrDefault(p => p.Id == user);
            if(u != null){
                Validation v = db.Validation.SingleOrDefault(p => p.user == u.Id);
                if (v != null)
                {
                    db.Validation.Remove(v);
                }
                switch (ticketType)
                {
                    case 1:
                        if (u.t1 > 0)
                            u.t1 -= 1;
                        else
                            return "erro";
                        break;
                    case 2:
                        if (u.t2 > 0)
                            u.t2 -= 1;
                        else
                            return "erro";
                        break;
                    case 3:
                        if (u.t3 > 0)
                            u.t3 -= 1;
                        else
                            return "erro";
                        break;
                }
                Validation v1 = new Validation();
                v1.type = ticketType;
                v1.user = u.Id;
                v1.spot = spot;
                db.Validation.Add(v1);
                db.SaveChanges();

                return Regex.Replace(u.name, @"\s", "");
            }
            else
            {
                return "erro";
            }
        }

        public List<Spot> getSpots()
        {
           List<Spot> l = db.Spot.OrderBy(p => p.name).ToList();
           List<Spot> r = new List<Spot>();
           foreach (Spot s in l)
           {
               if (s.Id == 1)
               {
                   r.Add(new Spot { Id = s.Id, name = "aqui", android_id = s.android_id });
               }
               else {
                   r.Add(new Spot { Id = s.Id, name = s.name, android_id = s.android_id });
               }
              // r.Add(new Spot { Id = s.Id, name = s.name, android_id = s.android_id});
           }
           return r;

        }

    
        public List<Validation> validation(int spot)
        {
            List<Validation> l = db.Validation.Where(p => p.spot == spot).ToList();
            List<Validation> r = new List<Validation>();
            foreach (Validation s in l)
            {

                int t = (DateTime.Now - s.stamp).Minutes;
                if (t <= 90)
                {
                r.Add(new Validation {type = s.type, stamp = s.stamp, user = s.user });
                }
            }
            return r;
        }
    }
}
