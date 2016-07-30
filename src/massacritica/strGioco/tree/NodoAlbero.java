package massacritica.strGioco.tree;

import massacritica.strGioco.*;

public class NodoAlbero implements Cloneable{

protected NodoAlbero padre;
protected NodoLista primoFiglio;
protected NodoLista bestFiglio;
protected Stato situazione;
/*ritorna lo stato contenuto nel nodo.
  Se  questo campo non è impostato, ritorna null
*/
public Stato getSituazione()
{return situazione;
}
/*costruisce un nodo vuoto
*/
public NodoAlbero()
 {situazione = null;
  primoFiglio=null;
  bestFiglio=null;
 }
/*costruisce un nodo specificando la situazione di gioco
 */
public NodoAlbero(Stato s)
 {situazione = s;
  primoFiglio=null;
  bestFiglio=null;
 }
 /*imposta il riferimento al figlio migliore
 */
public void setBestFiglio(NodoLista nl)
 {bestFiglio = nl;
 }
 /*ritorna il riferimento al figlio migliore.
 */
public NodoLista getBestFiglio()
 {return bestFiglio;
 }
 /*imposta il riferimento al padre per questo nodo
  */
public void setPadre(NodoAlbero padre)
 {this.padre = padre;
 }

 /*
  aggiunge un nodo (NodoAlbero) in testa alla lista dei figli
 */

public void addFiglio(NodoAlbero figlio)
 {figlio.setPadre(this);
  NodoLista tmp = new NodoLista(figlio);
  if (primoFiglio == null) //la lista è vuota
     {primoFiglio = tmp;
     }
  else
     {tmp.setNext(primoFiglio);
      primoFiglio=tmp;
     }
 }
/*
  aggiunge una lista di nodi in coda
 */
public void addFiglio(NodoLista figlio)
 {NodoLista tmp=primoFiglio;
  if (figlio==null)
      return;
  figlio.getVal().setPadre(this);
  if (primoFiglio == null) //la lista è vuota
     {primoFiglio = figlio;
     }
  else
     {while(tmp.getNext()!=null)
           tmp=tmp.getNext();
      tmp.setNext(figlio);
     }
 tmp = figlio;
     while(tmp != null)
     {tmp.getVal().setPadre(this);
      tmp = tmp.getNext();
     }
}
  //elimina l'elemento specificato come parametro

public void delFiglio(NodoLista canc)
 {//tmp è il nodo nella lista precedente a quello da cancellare
  NodoLista tmp = primoFiglio;
  if (canc == tmp) // controllo se l'elemento è il primo
     {primoFiglio = tmp.getNext();
       return;
     }
  while ((tmp != null) && (tmp.getNext() != canc))
         {
           tmp = tmp.getNext();
         }
  if (tmp != null)
    {tmp.setNext(tmp.getNext().getNext());
    }
 }
/*
 */
public void stampaPadreEFigli()
 {
  NodoLista l = primoFiglio;
  while (l != null)
   {
    l = l.getNext();
   }
 }
 public String toString()
 {return new String(""+situazione+"\n");
 }

/*ritorna il primo figlio
*/
public NodoLista getListaFigli()
 {return primoFiglio;
 }

public NodoAlbero getPadre()
 {return padre;
 }

public Object clone()
{NodoAlbero na = new NodoAlbero();
  na.bestFiglio=this.bestFiglio;
  na.padre=this.padre;
  na.primoFiglio=this.primoFiglio;

    //imposto il campo "padre" dei figli
  NodoLista t = na.primoFiglio;
  while(t!=null)
     {t.getVal().setPadre(na);
      t=t.getNext();
     }
  na.situazione=(Stato)this.situazione.clone();
  return na;
}
}
