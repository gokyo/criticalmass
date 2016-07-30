package massacritica.strGioco.tree;

import java.util.Vector;

import massacritica.strGioco.*;
import massacritica.comVal.Arbitro;


public class Evaluator{
public static int evaluate(Stato s)
 {return 1;
 }
public static int evaluate(NodoAlbero n)
 {return 1;
 }


  // Ecco una semplice stima, la prima che mi e' venuta in mente
  // Passo lo Stato s con le eventuali esplosioni gia' fatte.
  public static int stimaSgorlon(Stato s,boolean giocatore){
/*tende a intestardirsi su certe posizioni, perdendo mosse e pedine.
        */
    int e=0;
    if(s.contaPedine()<5)
     { int ris = (int)(Math.random()*1000);
       // le prime 4 mosse sono casuali per fare in modo
       // che le partite Ai - Ai non siano esattamente uguali
       return ris;
     }
    if(Arbitro.controllaVittoria(s,giocatore))
      return Integer.MAX_VALUE-1;
    if(Arbitro.controllaVittoria(s,!giocatore))
      return Integer.MIN_VALUE+1;

    for(int i=0;i<5;++i){
      for(int j=0;j<6;++j){
        if(s.scacchiera[i][j].numeroPedine!=0)
          if(s.scacchiera[i][j].giocatore!=giocatore)
            e=e-pesoCasella2(s,s.scacchiera[i][j],giocatore);
          else
            e=e+pesoCasella2(s,s.scacchiera[i][j],giocatore);
      }
    }
return e;
}

  private static int pesoCasella2(Stato s,Casella c,boolean gioc){
    int result=0;
    if(c.numeroPedine!=0 && c.giocatore==gioc)
      result+=200*c.numeroPedine;
    Vector caselle = s.getCaselleAdiacenti(c.x,c.y);
    int mygap=c.getMassaCritica()-c.numeroPedine;

    boolean ok=true;
    for(int i=0;i<caselle.size();++i){
      Casella c_=(Casella)caselle.elementAt(i);
      int hisgap=c_.getMassaCritica()-c_.numeroPedine;

      if(mygap>=hisgap && gioc!=c_.giocatore)
      {
        result-=10;
        ok=false;
      }
    }
    if(ok)
      result+=20;


    if(c.x==0 || c.x==5)
      result+=5;
    if(c.y==0 || c.y==4)
      result+=5;

    caselle=s.getCaselleDiagonali(c.x,c.y);
    for(int i=0;i<caselle.size();++i){
      Casella c_=(Casella)caselle.elementAt(i);
      if(c_.numeroPedine==c_.getMassaCritica()-1 && gioc!=c_.giocatore)
        result+=1;
    }

    caselle=s.getCaselleVicine2(c.x,c.y);
    for(int i=0;i<caselle.size();++i){
      Casella c_=(Casella)caselle.elementAt(i);
      if(c_.numeroPedine==c_.getMassaCritica()-1 && gioc!=c_.giocatore)
        result+=1;
    }


    return result;
  }


}
