package com.example.yosef;



public class Order {
   public String name,email,latit,longit , Otime , Odate;


    public Order()
    {
    }


       public Order(String name,String email,String latit,String longit,String Otime  ,String Odate) {
           this.name = name;
        this.email = email;
           this.latit = latit;
           this.longit = longit;
           this.Otime = Otime;
           this.Odate = Odate;

        }

    public String getEmail() {
        return email;
    }

    public String getLatit() {
        return latit;
    }

    public String getLongit() {
        return longit;
    }
}

