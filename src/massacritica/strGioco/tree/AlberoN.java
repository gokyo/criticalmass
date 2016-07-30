package massacritica.strGioco.tree;

import massacritica.strGioco.*;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class AlberoN{

private NodoAlbero radice=null;
private byte livelli;
boolean primo;
private int numNodi;
  /*limiti per il numero di nodi nell'albero in relazione al livello
    di difficoltà scelto
  */
private final static int nodeHardLimit = 30000;
private final static int nodeMedLimit = 1000;
private final static int nodeEasyLimit = 100;

//ritorna il valore aggiornato l'ultima volta con contaNodi
public int getNumNodi()
{
  return numNodi;
}
//inizia il computer, creo stato iniziale
public AlberoN(byte liv,boolean b)
       {if(liv <= 0)
           {liv = 3;
           }
        Stato s = new Stato();
        radice = new NodoMax(s);
        radice.setPadre(null);
        this.livelli = liv;
        primo=b;
        costrAlbero(s,liv);
      }


//ha iniziato l'altro, passo lo stato con la mossa effettuata.
//inizio comunque con un nodo max
public AlberoN(Stato s, byte liv, boolean b)
       {if(liv <= 0)
           {liv = 3;
           }
        radice = new NodoMax(s);
        radice.setPadre(null);
        this.livelli = liv;
        primo=b;
        costrAlbero(s,liv);
      }



public boolean getPrimo()
{return primo;
      }
public void setPrimo(boolean b)
{primo = b;
}
public AlberoN(NodoAlbero na)
       {radice = na;
        radice.setPadre(null);
       }

public void setRadice(NodoAlbero na)
       {radice = na;
        radice.setPadre(null);
       }

public NodoAlbero getRadice()
       {return radice;
       }
/*costruzione dell'albero in profondità, partendo dallo stato indicato  e per i
livelli indicati
*/
private void costrAlbero(Stato s, byte liv)
 {
  NodoLista padreTmp;
  NodoAlbero padreNodoTmp;
  NodoLista figliTmp = s.getMieMossePossibili(primo);
  radice.addFiglio(figliTmp);
  if(liv==1)//se l'albero è a 1 livello, ho già finito.
      return;

  LinkedList pilaLiv = new LinkedList();
  pilaLiv.addLast(radice);
  pilaLiv.addLast(figliTmp);
  byte livCorrente = (byte)pilaLiv.size();
  if(liv == 1) return;
  while(livCorrente!=1)
       {padreTmp = (NodoLista)pilaLiv.removeLast();
        if(padreTmp != null)
           {padreNodoTmp = padreTmp.getVal();
            if(padreNodoTmp instanceof NodoMax)
               {//la lista di stati seguenti è composta da stati senza
                //esplosioni multiple in questo modo la valutazione si limita
                //al calcolo sullo stato finale
                  figliTmp = padreNodoTmp.getSituazione().
                                             getMieMossePossibili(primo);

              }
            else
            {// la lista di stati seguenti è composta da stati senza
             // esplosioni multiple in questo modo la valutazione
             // si limita al calcolo sullo stato finale
            figliTmp = padreNodoTmp.getSituazione().getSueMossePossibili(primo);
               }
            padreNodoTmp.addFiglio(figliTmp);

            padreTmp=padreTmp.getNext();
            pilaLiv.addLast(padreTmp);

            if(livCorrente < liv)
               {pilaLiv.addLast(figliTmp);
               }

          }
       livCorrente = (byte)pilaLiv.size();

       }
 }

 /*dopo che sia io che l'altro giocatore abbiamo effettuato una mossa
 l'albero di gioco viene ricostruito, mantenendo la parte già sviluppata
 precedentemente: se l'albero è a 3 (o +) livelli, la parte ancora utilizzabile
 del 3° livello viene ripresa. La radice dell'albero diventa quella
 contraddistinta dallo stato s.
 Dopo l'aggiornamento del nodo radice e della conseguente pulizia dei nodi non
 più necessari, l'albero viene massimizzato in base al livello di difficoltà
 scelto.
*/
  public void aggDueLivelli(Stato s)
    {//se l'albero ha solo 1 livello, devo solamente costruire un altro albero
      if(livelli==1)
        {radice=new AlberoN(s,(byte)1,getPrimo()).getRadice();
         massimizzaAlbero();
         System.gc();
         return;
        }

      //trovare la nuova radice in base allo stato
      NodoAlbero oldRad = radice;


      radice = findIter(s);
      radice.setPadre(null);
     //faccio un pò di pulizia



     //livelli attuali nell'albero
      int livAttuali = (byte)(livelli-2);

     NodoLista figliTmp = radice.getListaFigli();

     //l'albero era di due livelli, dopo due mosse sarei su una foglia.
     //lo ricostruisco da capo
     if(figliTmp == null || (livAttuali==0))
      {  radice = new AlberoN(s,(byte)2,getPrimo()).getRadice();
         massimizzaAlbero();
        livelli =2;
             System.gc();
        return;
        }

    //l'albero ha più di 2 livelli
    massimizzaAlbero();
    }



//vecchia versione di aggDueLivelli, come la prima solo che mantiene costante
//il numero dilivelli invece del numero di nodi. Questo si traduce in uno spreco
//andando avanti nel gioco

/* public void aggDueLivelli(Stato s)
  {//trovare la nuova radice in base allo stato

    System.out.println("Stato da trovare:\n"+s);

    System.out.println("radice:\n"+radice.getSituazione());
    radice = find(s,radice);


    radice.setPadre(null);
   //faccio un pò di pulizia

    System.gc();

   //livelli attuali nell'albero
    int livAttuali = (byte)(livelli-2);

   LinkedList listaLiv = new LinkedList();
   NodoLista figliTmp = radice.getListaFigli();

   if(figliTmp == null || (livAttuali==0))//l'albero era di due livelli
    {  radice = new AlberoN(s,(byte)2,getPrimo()).getRadice();
      livelli =2;
       return;
      }
   //l'albero ha più di 2 livelli
   listaLiv.addLast(figliTmp);
   NodoLista padreTmp;
   NodoAlbero padreTmpNodo;
   while(listaLiv.size() != 0)
    {System.out.println("in while ..");
      padreTmp = (NodoLista)listaLiv.removeLast();
      if(padreTmp != null)
        {padreTmpNodo = padreTmp.getVal();
         if (padreTmpNodo.getListaFigli() != null)
            {listaLiv.addLast(padreTmp);
             listaLiv.addLast(padreTmpNodo.getListaFigli());
            }
         else if(listaLiv.size() == (livAttuali-1))
                  // ho tolto l'ultimo padre (per questo -1)
                {
                 /*sono in una foglia dell'albero rimasto.
                   Devo aggiungere 2 livelli.
                   Considerazioni:
                  1. devo aggiungere 2 livelli di stati.
                  2. il tipo di nodi figli (e quindi anche quello dei nipoti)
                     dipende dal tipo di istanza della foglia attuale.

                  */
  /*        if(padreTmpNodo instanceof NodoMin) //i figli sono min, i nipoti max
                 {Stato stDellaFoglia = padreTmpNodo.getSituazione();
                  NodoLista figlioNuovo =
                            stDellaFoglia.getSueMossePossibili(primo);
                  NodoLista nipoteNuovo;
                  padreTmpNodo.addFiglio(figlioNuovo);
                   //per ogni figlio creo i nipoti
                    while(figlioNuovo != null)
                      {stDellaFoglia = figlioNuovo.getVal().getSituazione();
                       nipoteNuovo = stDellaFoglia.getMieMossePossibili(primo);
                       figlioNuovo.getVal().addFiglio(nipoteNuovo);
                       figlioNuovo = figlioNuovo.getNext();
                       }


             }
                 else //i figli sono max, i nipoti min
                   {Stato stDellaFoglia = padreTmpNodo.getSituazione();
                    NodoLista figlioNuovo =
                            stDellaFoglia.getMieMossePossibili(primo);
                    NodoLista nipoteNuovo;
                    padreTmpNodo.addFiglio(figlioNuovo);
                      //per ogni figlio creo i nipoti
                    while(figlioNuovo != null)
                      {stDellaFoglia = figlioNuovo.getVal().getSituazione();
                       nipoteNuovo = stDellaFoglia.getSueMossePossibili(primo);
                       figlioNuovo.getVal().addFiglio(nipoteNuovo);
                       figlioNuovo = figlioNuovo.getNext();
                       }

                     }
                 listaLiv.addLast(padreTmp.getNext());
                 System.out.println("\nPadre appena creato: \n"+padreTmpNodo);
                }
        }
     else
        {try
           {padreTmp = (NodoLista)listaLiv.removeLast();
            listaLiv.addLast(padreTmp.getNext());
           }
         catch(NoSuchElementException necc)
           {
           }
        }

    }
  System.out.println("fine agg 2 livelli");
  }

*/
//alpha-beta pruning
public int eval()
 {int val=eval(radice, Integer.MIN_VALUE, Integer.MAX_VALUE);
   return val;
 }

public int eval(NodoAlbero n, int alfa, int beta)
 {NodoLista tmp = n.getListaFigli();
  int valFiglio;
  if(tmp == null)
    {
     return Evaluator.stimaSgorlon(n.getSituazione(),primo);
    }
  if(n instanceof NodoMax)
     {while(tmp != null)
            {valFiglio = eval(tmp.getVal(),alfa,beta);
              //tmp di tipo nodo lista faccio getval per prendere nodoalbero !
             //alfa = Math.max(alfa, valFiglio);
             if(alfa < valFiglio)
                 {alfa = valFiglio;
                  n.setBestFiglio(tmp);
                 }

             if (alfa >= beta)
                {tmp.setNext(null);
                 return beta;
                }
             tmp=tmp.getNext();
            }
      return alfa;
     }
  else
     {while(tmp != null)
            {valFiglio = eval(tmp.getVal(),alfa,beta);
             //beta = Math.min(beta,valFiglio);
             if(beta > valFiglio)
                 {beta = valFiglio;
                  //n.setBestFiglio(tmp);
                 }
             if (alfa >= beta)
                {//tmp.setNext(null);
                 return alfa;
                }
             tmp=tmp.getNext();
            }
      return beta;
     }
 }
/*versione ricorsiva depth-first della ricerca. Per i nostri scopi è più utile
  una versione breadth-first perchè:
 1.l'utilizzo di questa funzione nel nostro caso è rivolta alla ricerca della
   nuova radice, che di sicuro è presente al massimo nel 2° livello dell'albero
 2.nelle ultime fasi di gioco l'albero può crescere molto come altezza,
   aumentando il numero di chiamate ricorsive.
   Utilizzando una versione breadth-first iterativa viene evitato questo spreco.
 */
private NodoAlbero find(Stato s, NodoAlbero na)
 {
   if(na.getSituazione().equals(s))
     {return na;
     }
  NodoLista nl = na.getListaFigli();
  NodoAlbero tmp = null;
  while((nl != null) && (tmp == null))
       {tmp = find(s, nl.getVal());
        nl = nl.getNext();
       }
  return tmp;
 }

 private NodoAlbero findIter(Stato s)
  {LinkedList l = new LinkedList();
   l.add(radice);
   NodoAlbero tmp;
   NodoLista listaTmp;
   while(l.size()!=0)
     {tmp=(NodoAlbero)l.removeFirst();
      listaTmp=tmp.getListaFigli();
      if(tmp.getSituazione().equals(s))//ho trovato il nodo cercato
          return (NodoAlbero)tmp.clone();
      else//metto in coda i figli
         {while(listaTmp!=null)
              {l.addLast(listaTmp.getVal());
                listaTmp=listaTmp.getNext();
              }
         }
     }
    //se sono qui il nodo non è stato trovato, il risultato è null
    return null;
  }



/*stampa il contenuto dell'albero per livelli (nelle prime fasi è inutile....)
*/
public void stampaAlbero()
 {

  int i=0;
  LinkedList l = new LinkedList();
  l.addLast(radice);
  NodoAlbero nodoTmp;
  NodoLista listaTmp;
  while (l.size() != 0)
   {nodoTmp = (NodoAlbero)l.removeFirst();
    listaTmp = nodoTmp.getListaFigli();
    while(listaTmp != null)
         {l.addLast(listaTmp.getVal());
          listaTmp=listaTmp.getNext();
         }
    }

 }

 public void stampaAlbero(NodoAlbero n)
  {
   System.out.println("Stampa dell'albero -------------------")  ;
   int i=0;
   LinkedList l = new LinkedList();
   l.addLast(n);
   NodoAlbero nodoTmp;
   NodoLista listaTmp;
   while (l.size() != 0)
    {nodoTmp = (NodoAlbero)l.removeFirst();
     System.out.println(nodoTmp.getSituazione());
     listaTmp = nodoTmp.getListaFigli();
     while(listaTmp != null)
          {l.addLast(listaTmp.getVal());
           listaTmp=listaTmp.getNext();
          }
     }
     System.out.println("La radice è\n"+n.getSituazione());
  }


/*funzione iterativa che conta i nodi presenti nell'albero
  */
 public void contaNodi()
  {numNodi=0;
    LinkedList l = new LinkedList();
    l.addLast(radice);
    NodoAlbero nodoTmp;
    NodoLista listaTmp;
    while (l.size() != 0)
     {nodoTmp = (NodoAlbero)l.removeFirst();
       numNodi++;
      listaTmp = nodoTmp.getListaFigli();
      while(listaTmp != null)
           {l.addLast(listaTmp.getVal());
            listaTmp=listaTmp.getNext();
           }
      }


 }




 public void massimizzaAlbero() {
      //alle foglie aggiungo figli finchè la dim attuale dell'albero
      //più la dimensione della lista di figli
      //(calcolabile tramite la funzione nella classe stato) è minore del limite
      contaNodi();
      int allocabili;
      if(livelli==3)
         allocabili = AlberoN.nodeHardLimit - numNodi;
      else if(livelli == 2)
         allocabili = AlberoN.nodeMedLimit - numNodi;
      else
          allocabili = AlberoN.nodeEasyLimit - numNodi;
      int numFigli = 0;
      LinkedList l = new LinkedList();
      l.add(radice);
      NodoAlbero tmp;
      NodoLista listaTmp;
      while (l.size()!=0) {
        tmp = (NodoAlbero) l.removeFirst();
        listaTmp = tmp.getListaFigli();
        if (listaTmp != null) {
          while (listaTmp != null) {
            l.addLast(listaTmp.getVal());
            listaTmp = listaTmp.getNext();
          }
        }
        else { //calcolo quanti figli (ancora da creare) ha lo stato attuale
          if (tmp instanceof NodoMax) { // i prossimi figli sono miei
            numFigli = tmp.getSituazione().quantiFigli(primo);
            if (allocabili >= numFigli) { //alloco i figli
              listaTmp=tmp.getSituazione().getMieMossePossibili(primo);
              tmp.addFiglio(listaTmp);
              allocabili-=numFigli;
            }
            else { //non posso continuare
              return;
            }

          }
          else { //i prossimi figli sono dell'avversario
            numFigli = tmp.getSituazione().quantiFigli(!primo);
            if (allocabili >= numFigli) { //alloco i figli
              listaTmp=tmp.getSituazione().getSueMossePossibili(primo);
              tmp.addFiglio(listaTmp);
              allocabili-=numFigli;
            }
            else { //non posso continuare
              return;
            }

          }
         //aggiungo in testa il nodo di cui ho appena calcolato la "prole".
         //potevo aggiungere in coda i figli direttamente qui, ma così il codice
         //risulta più compatto.
          if(listaTmp!=null)
             l.addFirst(tmp);
        }

      }

    }


}
