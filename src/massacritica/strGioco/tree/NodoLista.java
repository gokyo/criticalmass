package massacritica.strGioco.tree;

public class NodoLista{
private NodoAlbero val;
private NodoLista next;
/*crea un nodolista vuoto
      */
public NodoLista()
 {val = null;
  next = null;
 }
 /*crea un nodolista specificando l'elemento di tipo nodoalbero contenuto
     */
public NodoLista(NodoAlbero na)
 {val = na;
  next = null;
 }
 /*imposta il riferimento al prossimo elemento della lista
     */
public void setNext(NodoLista nl)
 {next = nl;
 }
 /*restituisce il riferimento al prossimo elemento della lista
     */
public NodoLista getNext()
 {return next;
 }
 /*restituisce il riferimento al contenuto del nodolista
     */
public NodoAlbero getVal()
 {return val;
 }
 /*imposta il riferimento al contenuto del nodolista
     */
public void setVal(NodoAlbero na)
 {val = na;
 }
 /*conversione a stringa
     */
public String toString()
 {String tmp=new String("NodoListaVuoto\n");
   if (val!=null)
      tmp =val.getSituazione().toString();
   return tmp;
  }
}
