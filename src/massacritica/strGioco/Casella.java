package massacritica.strGioco;

public class Casella implements Cloneable{

  public byte x;
  public byte y;
  public byte numeroPedine=0;
  public boolean giocatore;

  public Casella(int x,int y,int numeroPedine,boolean gioc){
    this.x=(byte)x;
    this.y=(byte)y;
    this.giocatore=gioc;
    this.numeroPedine=(byte)numeroPedine;
  }
  public Casella()
   {
   }

   public boolean equals(Object obj)
   {
      Casella c=(Casella)obj;
      if (c.x!=this.x) return false;
      if (c.y!=this.y) return false;
      if (c.numeroPedine!=this.numeroPedine) return false;
      if(c.numeroPedine!=0)
          if (c.giocatore!=this.giocatore) return false;
      return true;

   }

public Object clone()
 {Casella tmp = new Casella();
  tmp.x=this.x;
  tmp.y=this.y;
  tmp.numeroPedine=this.numeroPedine;
  tmp.giocatore=this.giocatore;
  return tmp;
 }

 public byte getMassaCritica()
 {
   if ( (x == 0) || (x == 5))
     {if ( (y == 0) || (y == 4))
         {return (byte) 2;
          }
      else
         {return (byte) 3;
         }
     }
   else
     {if( (y == 0) || (y == 4))
         {return (byte) 3;
         }
     else
        {return (byte) 4;
        }
     }

  }



}
