package massacritica.comVal;

import java.util.*;
import massacritica.strGioco.*;

public class Arbitro {

  private static LinkedList statiTransitori=null;

  public static boolean controllaVittoria(Stato s,boolean giocatore){

  if(s.contaPedine()==0)
     return false;
  for (byte i = 0; i < 5; i++)
      for (byte j = 0; j < 6; j++)
        if (s.scacchiera[i][j].numeroPedine != 0)
          if (s.scacchiera[i][j].giocatore != giocatore)
              return false;

  if((s.contaPedine()>2))
    return true;
  return false;
  }

/* controlla se la lista l contiene uno stato esploso con coordinate
   della casella che sta esplodendo = (x,y).
   Controlla dall'indice passato come parametro fino alla fine
 */
private static boolean isCasellaInserita(
        LinkedList l,int indiceInizio,int x,int y){
    for(int i=indiceInizio+1;i<l.size();++i){
      StatoEsploso SE=(StatoEsploso)l.get(i);
      if(SE.getX()==x && SE.getY()==y)
        return true;
    }
    return false;
}
public static LinkedList getStatiTransitori(Stato s,int x,int y){

  if(s.get((byte)x,(byte)y).numeroPedine <
     s.get((byte)x,(byte)y).getMassaCritica())
    return null;
  LinkedList l=new LinkedList();
  Stato s1=(Stato)s.clone();
  s1.get((byte)x,(byte)y).numeroPedine=0;
  Vector caselleAdiacenti=s1.getCaselleAdiacenti((byte)x,(byte)y);
  for(int i=0;i<caselleAdiacenti.size();++i){
    Casella c=(Casella)caselleAdiacenti.elementAt(i);
    c.numeroPedine++;
    c.giocatore=s1.get((byte)x,(byte)y).giocatore;
  }
  l.addLast(new StatoEsploso(s1,(byte)x,(byte)y));
  return getStatiTransitori(l,0);

}

private static LinkedList getStatiTransitori(LinkedList l,int index){

  if(index>=l.size()) return l;
  StatoEsploso SE=(StatoEsploso)l.getLast();
  StatoEsploso SEE=(StatoEsploso)l.get(index);
  Stato s=SE.getStato();
  int x=SEE.getX();
  int y=SEE.getY();
  Vector caselleAdiacenti1=s.getCaselleAdiacenti((byte)x,(byte)y);
  for(int i=0;i<caselleAdiacenti1.size();++i){
    Casella c=(Casella)caselleAdiacenti1.elementAt(i);
    if(c.numeroPedine>=c.getMassaCritica() &&
       !isCasellaInserita(l,index,c.x,c.y)){
      Stato s1=(Stato)((StatoEsploso)l.getLast()).getStato();
      Stato s2=(Stato)s1.clone();
      s2.get((byte)c.x,(byte)c.y).numeroPedine=0;
      Vector caselleAdiacenti2=s2.getCaselleAdiacenti((byte)c.x,(byte)c.y);
      for(int j=0;j<caselleAdiacenti2.size();++j){
        Casella c1=(Casella)caselleAdiacenti2.elementAt(j);
        c1.giocatore=c.giocatore;
        c1.numeroPedine++;

      }
      l.addLast(new StatoEsploso(s2,(byte)c.x,(byte)c.y));

    }

  }
  return getStatiTransitori(l,index+1);

}
/*
  ritorna una lista vuota se la mossa effettuata non provoca esplosioni,
          lo stato con l'esplosione effettuata altrimenti.
 questo metodo è il punto di partenza per calcolare una sequenza di esplosioni
 a catena
*/
public static LinkedList controllaMossa(Stato s,byte x,byte y){
    statiTransitori=new LinkedList();

    if(s.scacchiera[y][x].numeroPedine>=s.scacchiera[y][x].getMassaCritica())
     {
      Stato newStato=(Stato)s.clone();
      Casella c=newStato.get(x,y);
      c.numeroPedine=0;
      Vector caselleAdiacenti=newStato.getCaselleAdiacenti(c.x,c.y);
      for(int j=0;j<caselleAdiacenti.size();j++){
        Casella c_=(Casella)caselleAdiacenti.elementAt(j);
        c_.giocatore=c.giocatore;
        c_.numeroPedine++;
       }
      statiTransitori.addLast(new StatoEsploso(newStato,x,y));
    }


   return statiTransitori;
}
/*da una lista di esplosioni "sorelle"
  (ovvero da effettuarsi "contemporaneamente")
 Calcola la lista di esplosioni figlie, ovvero le "sorelle" del prossimo turno.
  Dopo il calcolo, le esplosioni già viusalizzate vengono tolte dalla lista
*/

public static LinkedList getStatiTransitoriEsplosioniMultiple
    (LinkedList l, int inizio, int fine)

   {StatoEsploso lastSE;
    StatoEsploso tmpSE;
    Vector caselleAdiacentidellEsplosa;
    Vector caselleAdiacentidellEsplodente;
    Vector caselleControllate=new Vector();
    boolean giaControllata=false;
    Casella c_2;
    /*in certe condizioni è meglio calcolare un livello per volta,
     restituendolo (rischio di outOfMemory).
     La classe chiamante (GUI) deve ciclare finchè la lista ritornata
     non ha la stessa dimensione della lista passata.
      il metodo pubblico non ha più senso.
     */
     lastSE= (StatoEsploso)l.getLast();
     caselleControllate.removeAllElements();

         for(byte j=0; j<fine-inizio;j++)
       //cicla sulla parte di coda da controllare
            {lastSE= (StatoEsploso)l.getLast();
             tmpSE = (StatoEsploso)l.get(inizio+j);
             caselleAdiacentidellEsplosa = tmpSE.getStato().getCaselleAdiacenti
                                           (tmpSE.getX(), tmpSE.getY());
             Casella c;
             for(byte i =0; i<caselleAdiacentidellEsplosa.size();i++)
           //cicla sulle caselle da controllare per ogni stato
                {c = (Casella)caselleAdiacentidellEsplosa.elementAt(i);
                 Stato newStato=null;
                 lastSE= (StatoEsploso)l.getLast();
                 giaControllata=false;
                 for(byte h=0;h<caselleControllate.size();h++)
                    {Casella cgc=(Casella)caselleControllate.elementAt(h);
                      if(c.x==cgc.x && c.y==cgc.y)
                        {giaControllata=true;
                        }
                    }
                 if(!giaControllata)
                    if(c.numeroPedine>=c.getMassaCritica())
                        {newStato = ((Stato) lastSE.getStato().clone() );
                         newStato.get(c.x,c.y).numeroPedine=0;
                         caselleControllate.add(c);
                         caselleAdiacentidellEsplodente =
                              newStato.getCaselleAdiacenti(c.x,c.y);
                      for(byte k=0;k<caselleAdiacentidellEsplodente.size();k++)
                       //cicla sulle caselle adiacenti a quelle esplodenti
                             {c_2=(Casella)caselleAdiacentidellEsplodente.
                                                               elementAt(k);
                              c_2.numeroPedine++;
                              c_2.giocatore=c.giocatore;
                              for(byte w=0;w<caselleControllate.size();w++)
                                 {Casella cgc=(Casella)caselleControllate.
                                                               elementAt(w);
                                  if(c_2.x==cgc.x && c_2.y==cgc.y &&
                                     c_2.numeroPedine == c_2.getMassaCritica())
                                   {caselleControllate.remove(cgc);
                                    }
                                  }
                             }
                        l.addLast(new StatoEsploso(newStato,c.x,c.y));
                        }
                 }
            }
 //rimozione esplosioni "sorelle" dell'iterazione precedente
       for(int h=0;h<fine;h++)
       {l.removeFirst();
       }
return l;
}

}
