//------------------------------------------------------------------------------
// <auto-generated>
//    This code was generated from a template.
//
//    Manual changes to this file may cause unexpected behavior in your application.
//    Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace WcfService1
{
    using System;
    using System.Collections.Generic;
    
    public partial class User
    {
        public User()
        {
            this.Validation = new HashSet<Validation>();
        }
    
        public int Id { get; set; }
        public string name { get; set; }
        public string password { get; set; }
        public string email { get; set; }
        public int t1 { get; set; }
        public int t2 { get; set; }
        public int t3 { get; set; }
        public string creditcard { get; set; }
        public string authtoken { get; set; }
        public byte[] authtimestamp { get; set; }
    
        public virtual ICollection<Validation> Validation { get; set; }
    }
}
