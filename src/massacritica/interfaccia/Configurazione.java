package massacritica.interfaccia;

import java.io.*;
import java.util.*;

public class Configurazione {

  public Partita partita;
  public String pathFile;

  public Configurazione(String pathFile) throws Exception{

    this.pathFile=pathFile;
    partita=new Partita();
    partita.giocatore1=new Giocatore();
    partita.giocatore2=new Giocatore();
    leggiFileConfigurazione();

  }

  private void leggiFileConfigurazione() throws Exception{

    File f=new File(this.pathFile);
    if (!f.exists()) throw new FileNotFoundException(
                                    "File di configurazione non trovato!");

    BufferedReader buff=new BufferedReader(new FileReader(f));
    String line="@";
    line=buff.readLine();
    while(line!=null){
      StringTokenizer str=new StringTokenizer(line);
      if(str.countTokens()==2){
        String key = str.nextToken();
        String value = str.nextToken();

        if (key.equalsIgnoreCase("GIOCATORE1_TIPO")){
          if(value.equalsIgnoreCase("UMANO"))
            partita.giocatore1.umano=true;
          else if(value.equalsIgnoreCase("MACCHINA"))
            partita.giocatore1.umano=false;
          else throw new RuntimeException("file di configurazione errato!");
        }
        else if(key.equalsIgnoreCase("GIOCATORE1_DIFFICOLTA")){
          int level=Integer.parseInt(value);
          partita.giocatore1.livelloDifficolta=level;
        }
        else if(key.equalsIgnoreCase("GIOCATORE1_HOST")){
          if(value.equalsIgnoreCase("LOCALE"))
            partita.giocatore1.giocatoreLocale=true;
          else if(value.equalsIgnoreCase("REMOTO"))
            partita.giocatore1.giocatoreLocale=false;
          else throw new RuntimeException("file di configurazione errato!");
        }
        else if(key.equalsIgnoreCase("GIOCATORE1_NOME")){
          partita.giocatore1.nomeGiocatore=value;
        }
        else if(key.equalsIgnoreCase("GIOCATORE1_IP_ADDRESS")){
          partita.giocatore1.ipAddress=value;
        }
        else if(key.equalsIgnoreCase("GIOCATORE1_PORTA_INVIO")){
          partita.giocatore1.portaInvio=Integer.parseInt(value);
        }
        else if(key.equalsIgnoreCase("GIOCATORE1_PORTA_RICEZIONE")){
          partita.giocatore1.portaRicezione=Integer.parseInt(value);
        }
        ///////////////////////////////////////////////////////////
        else if (key.equalsIgnoreCase("GIOCATORE2_TIPO")){
          if(value.equalsIgnoreCase("UMANO"))
            partita.giocatore2.umano=true;
          else if(value.equalsIgnoreCase("MACCHINA"))
            partita.giocatore2.umano=false;
          else throw new RuntimeException("file di configurazione errato!");
        }
        else if(key.equalsIgnoreCase("GIOCATORE2_DIFFICOLTA")){
          int level=Integer.parseInt(value);
          partita.giocatore2.livelloDifficolta=level;
        }
        else if(key.equalsIgnoreCase("GIOCATORE2_HOST")){
          if(value.equalsIgnoreCase("LOCALE"))
            partita.giocatore2.giocatoreLocale=true;
          else if(value.equalsIgnoreCase("REMOTO"))
            partita.giocatore2.giocatoreLocale=false;
          else throw new RuntimeException("file di configurazione errato!");
        }
        else if(key.equalsIgnoreCase("GIOCATORE2_IP_ADDRESS")){
          partita.giocatore2.ipAddress=value;
        }
        else if(key.equalsIgnoreCase("GIOCATORE2_NOME")){
          partita.giocatore2.nomeGiocatore=value;
        }
        else if(key.equalsIgnoreCase("GIOCATORE2_PORTA_INVIO")){
          partita.giocatore2.portaInvio=Integer.parseInt(value);
        }
        else if(key.equalsIgnoreCase("GIOCATORE2_PORTA_RICEZIONE")){
          partita.giocatore2.portaRicezione=Integer.parseInt(value);
        }

        // sleep time

        else if(key.equalsIgnoreCase("TEMPO_SLEEP_ESPLOSIONI")){
          partita.sleepTimeEsplosioni=Integer.parseInt(value);
        }
        else if(key.equalsIgnoreCase("TEMPO_SLEEP_AI_AI")){
          partita.sleepTimeAiAi=Integer.parseInt(value);
        }

        else if(key.equalsIgnoreCase("INIZIA_PER_PRIMO")){
          partita.iniziaPerPrimo=Integer.parseInt(value);
        }
        else if(key.equalsIgnoreCase("COLORE_GIALLO")){
          partita.coloreGiallo=Integer.parseInt(value);
        }
        else if(key.equalsIgnoreCase("COLORE_BLU")){
          partita.coloreBlu=Integer.parseInt(value);
        }


      }
      line=buff.readLine();
    }
    buff.close();
    // rispettare sintassi nel file conf.txt

  }

}
