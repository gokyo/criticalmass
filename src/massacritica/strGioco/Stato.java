package massacritica.strGioco;

import java.util.*;
import massacritica.strGioco.tree.*;
import massacritica.comVal.*;

public class Stato implements Cloneable{

  public Casella[][] scacchiera;
  public Casella ultimaCasella=null;

  public Stato(){
    scacchiera=new Casella[5][6];
    for(int i=0;i<5;++i){
      for (int j = 0; j < 6; ++j)
      {
        scacchiera[i][j] = new Casella(j, i, 0, true);
      }
    }
  }

  // x=ascissa {0 .. 5}, y=ordinata {0 .. 4} --> cambio ordine negli indici
  public boolean aggiungiPedina(byte x,byte y,boolean gioc){
    if(x<0||x>5||y<0||y>4) return false;

    if (scacchiera[y][x].numeroPedine!=0){
      if(scacchiera[y][x].giocatore!=gioc)
                 return false;
      scacchiera[y][x].numeroPedine=(byte)(1+scacchiera[y][x].numeroPedine);
    }
    else{
      scacchiera[y][x].numeroPedine=1;
      scacchiera[y][x].giocatore=gioc;
    }

    return true;
  }

public Vector getCaselleDiagonali(int x,int y){
  Vector v=new Vector();

  if(x-1>=0 && y-1>=0)
    v.add(this.get((byte)(x-1),(byte)(y-1)));

  if(x-1>=0 && y+1<=4)
    v.add(this.get((byte)(x-1),(byte)(y+1)));

  if(x+1<=5 && y-1>=0)
    v.add(this.get((byte)(x+1),(byte)(y-1)));

  if(x+1<=5 && y+1<=4)
    v.add(this.get((byte)(x+1),(byte)(y+1)));



  return v;

}


public Vector getCaselleAdiacenti(int x,int y){
  // okkio ordine indici
  Vector v=new Vector();
  if(x==0){
    if(y==0){
      v.add(this.get((byte)0,(byte)1));
      v.add(this.get((byte)1,(byte)0));
      return v;
    }
    else if(y==4){
      v.add(scacchiera[4][1]);
      v.add(scacchiera[3][0]);
      return v;
    }
    else{
      v.add(scacchiera[y-1][0]);
      v.add(scacchiera[y][1]);
      v.add(scacchiera[y+1][0]);
      return v;
    }
  }
  else if(x==5){
    if(y==0){
      v.add(scacchiera[0][4]);
      v.add(scacchiera[1][5]);
      return v;
    }
    else if(y==4){
      v.add(scacchiera[4][4]);
      v.add(scacchiera[3][5]);
      return v;
    }
    else{
      v.add(scacchiera[y-1][5]);
      v.add(scacchiera[y][4]);
      v.add(scacchiera[y+1][5]);
      return v;
    }
  }
  else{
    if(y==0){
      v.add(scacchiera[0][x-1]);
      v.add(scacchiera[1][x]);
      v.add(scacchiera[0][x+1]);
      return v;
    }
    else if(y==4){
      v.add(scacchiera[y][x-1]);
      v.add(scacchiera[y-1][x]);
      v.add(scacchiera[y][x+1]);
      return v;
    }
    else{
      v.add(scacchiera[y][x-1]);
      v.add(scacchiera[y-1][x]);
      v.add(scacchiera[y][x+1]);
      v.add(scacchiera[y+1][x]);
      return v;
    }
  }
}

public Vector getCaselleVicine2(int x,int y){
  // okkio ordine indici
  Vector v=new Vector();

  if(y-2>=0)
    v.add(this.get((byte)(x),(byte)(y-2)));

  if(y+2<=4)
    v.add(this.get((byte)(x),(byte)(y+2)));

  if(x-2>=0)
    v.add(this.get((byte)(x-2),(byte)(y)));

  if(x+2<=5)
    v.add(this.get((byte)(x+2),(byte)(y)));


  return v;
}
/*crea una lista contenente tutte le mie (io=computer) mosse possibili a
partire dallo stato attuale le esplosioni multiple sono già calcolate,
 salvo un riferimento alla casella in cui la pedina è stata
inserita. Questo serve alla gui per visualizzare la sequenza di esplosioni.
 All'albero invece interessa solo lo stato finale per la valutazione.*/

public NodoLista getMieMossePossibili(boolean sonoPrimo)
   {byte x,y;
    NodoLista primo=null;
    NodoLista tmp=null;
    if((Arbitro.controllaVittoria(this,sonoPrimo)) ||
       (Arbitro.controllaVittoria(this,!sonoPrimo)))
        return null;
    Stato s;
    for(x=0;x<6;x++)//ascisse
     for(y=0;y<5;y++)//ordinate
        {s = (Stato)this.clone();
          if(s.aggiungiPedina(x,y,sonoPrimo))//la mossa è possibile per me?
           {//tengo in memoria che mossa ho fatto per la gui
             s.ultimaCasella=scacchiera[y][x];
            //calcolo espl multiple
            LinkedList listaStati = Arbitro.controllaMossa(s,x,y);
            if (listaStati.size()!=0)
            {while (listaStati.size() > 0)
              { try{s=(Stato)((StatoEsploso)listaStati.getLast()).
                                                       getStato().clone();
                   }
                catch(NoSuchElementException nse){}

                   listaStati = Arbitro.getStatiTransitoriEsplosioniMultiple(
                                            listaStati, 0,listaStati.size());

                  if(Arbitro.controllaVittoria(s,true) ||
                     Arbitro.controllaVittoria(s,false))
                //se lo stato comporta la mia vittoria mi fermo,
                //non calcolo più altre esplosioni
                       {break;}
                  }
            }
            //adesso s dovrebbe contenere lo stato "tranquillo" dopo tutte
            // le esplosioni,o in ogni caso uno stato di vittoria

            if (primo==null)
                {primo=new NodoLista(new NodoMin(s));
                 tmp=primo;
                }
            else
                {tmp.setNext(new NodoLista(new NodoMin(s)));
                 tmp=tmp.getNext();
                }
           }
        }
   return primo;
   }

/*crea una lista contenente tutte le mosse possibili per l'avversario a
  partire dallo stato attuale le esplosioni multiple sono già calcolate,
  salvo un riferimento alla casella in cui la pedina è stata inserita.
 Questo serve alla gui per visualizzare la sequenza di esplosioni.
 All'albero invece interessa solo lo stato finale per la valutazione.
*/

  public NodoLista getSueMossePossibili(boolean sonoPrimo)
   {byte x,y;
    NodoLista primo=null;
    NodoLista tmp=null;
    if((Arbitro.controllaVittoria(this,sonoPrimo)) ||
       (Arbitro.controllaVittoria(this,!sonoPrimo)))
        return null;
    Stato s;
    for(x=0;x<6;x++)//ascisse
     for(y=0;y<5;y++)//ordinate
        {s = (Stato)this.clone();
          if(s.aggiungiPedina(x,y,!sonoPrimo))
             //la mossa è possibile per l'avversario?
           {
            //tengo in memoria che mossa ho fatto per la gui
            s.ultimaCasella=scacchiera[y][x];
            //calcolo espl multiple
            LinkedList listaStati = Arbitro.controllaMossa(s,x,y);
            if (listaStati.size()!=0)
            {while (listaStati.size() > 0)
                 { //calcola la sequenza di esplosioni
                   try{s=(Stato)((StatoEsploso)listaStati.getLast()).getStato();
                      }
                   catch(NoSuchElementException nse){
                                                    }
                   listaStati = Arbitro.getStatiTransitoriEsplosioniMultiple(
                                      listaStati, 0,listaStati.size());

                  if(Arbitro.controllaVittoria(s,false) ||
                     Arbitro.controllaVittoria(s,true))
                       //se lo stato comporta la mia vittoria mi fermo,
                       //non calcolo più altre esplosioni
                       {break;}
                  }
            }
            //adesso s dovrebbe contenere lo stato "tranquillo" dopo tutte le
            // esplosioni,o in ogni caso uno stato di vittoria
            if (primo==null)
                {primo=new NodoLista(new NodoMax(s));
                 tmp=primo;
                }
            else
                {tmp.setNext(new NodoLista(new NodoMax(s)));
                 tmp=tmp.getNext();
                }
           }
        }
   return primo;
   }

  // x=ascissa {0 .. 5}, y=ordinata {0 .. 4} --> cambio ordine negli indici
  // non lo usero' probabilmente
  public Casella get(byte x,byte y){
    if(x<0||x>5||y<0||y>4) return null;// controllo superfluo?
    return scacchiera[y][x];

  }
public boolean equals(Object obj)
{
  Stato s=(Stato)obj;
  for(int i=0;i<6;++i)
    for(int j=0;j<5;++j)
      if(!(this.get((byte)i,(byte)j).equals(s.get((byte)i,(byte)j))))
         return false;
  return true;


}

public int contaPedine()
  {
    int cont=0;
    for(int i=0;i<6;++i)
      for(int j=0;j<5;++j)
        cont+=(int)(scacchiera[j][i]).numeroPedine;
    return cont;
  }
public Object clone()
  {Stato tmp = new Stato();
   for(byte x=0;x<6;x++)
    for(byte y=0;y<5;y++)
        {tmp.scacchiera[y][x] = (Casella)((this.scacchiera[y][x]).clone());
        }
     if(ultimaCasella!=null)
       tmp.ultimaCasella=
                  tmp.scacchiera[this.ultimaCasella.y][this.ultimaCasella.x];
   return tmp;
  }
public String toString()
 {String tmp = new String();
  String end=System.getProperty("line.separator");

  for(byte y = 0; y < 5; y++)
    {for(byte x = 0; x < 6; x++)
       if(scacchiera[y][x].giocatore || (scacchiera[y][x].numeroPedine==0) )
         {tmp += " "+scacchiera[y][x].numeroPedine + " ";
         }
       else
       {tmp += "-"+scacchiera[y][x].numeroPedine +" ";
       }
     tmp += end;
    }
  return tmp;
 }
 public boolean testMossa(byte x, byte y, boolean g)
    {Casella c = scacchiera[y][x];
     if((c.numeroPedine == 0) || (c.giocatore == g))
        {return true;
        }
     else
       {return false;
       }
    }

 public int quantiFigli(boolean giocatore)
  {int count =0;
    for(byte i=0;i<6;i++)
      for(byte j=0;j<5;j++)
        if(testMossa(i,j,giocatore))
            count++;
    return count;
  }


}
