package massacritica.interfaccia;

// per i messaggi d'errore

public class MyException extends Exception{

  public static final String tit1="Fine partita";


  public static final String msg1="Sono stati rilevati problemi di rete";
  public static final String msg2="Configurazione sbagliata degli IP remoti";
  public static final String msg3="Scaduto il timeout per connettersi";
  public static final String msg4="L'utente remoto si e\' disconnesso";
  public static final String msg5="L'utente remoto non e\' connesso";


  public String title;
  public String message;

  public MyException(String message,String title){
    super(message);
    this.message=message;
    this.title=title;
  }

}
