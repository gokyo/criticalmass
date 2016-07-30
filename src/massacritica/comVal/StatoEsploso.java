package massacritica.comVal;

import massacritica.strGioco.*;

public class StatoEsploso {

private Stato mioStato;
private byte x;
private byte y;
/*crea uno stato esploso partendo da uno stato e da due coordinate
   relatve ad una sua casella
      */
public StatoEsploso(Stato s, byte x, byte y)
       {mioStato = s;
        this.x=x;
        this.y=y;
       }

public Stato getStato()
 {return mioStato;
 }
public byte getX()
 {return x;
 }
public byte getY()
 {return y;
 }

 public boolean equals(Object obj)
  {StatoEsploso se = (StatoEsploso) obj;
   if(!this.mioStato.equals(se.getStato()) ||
      (this.x != se.getX()) || (this.y != se.getY()))
       {return false;
       }
   return true;
  }

}
