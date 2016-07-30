package massacritica.interfaccia;

public class Giocatore {


  public String nomeGiocatore;
  public boolean umano;
  public boolean giocatoreLocale;

  // va bene anche il nome della macchina, non solo l'ip numerico
  public String ipAddress;// ha significato solo se giocatoreLocale==false
  public int livelloDifficolta; // ha significato solo se il gioc e' macchina
  public int portaRicezione;
  public int portaInvio;

}
