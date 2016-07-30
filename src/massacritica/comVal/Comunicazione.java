package massacritica.comVal;

import massacritica.interfaccia.MyException;
import java.io.*;
import java.net.*;

public class Comunicazione {

  public static final int TIMEOUT=10000;

  public int portaRicezione;
  public int portaInvio;
  public InetAddress ip=null;
  public Socket server=null; // usato solo per leggere , non scrivo
  public Socket client=null; // usato solo per scrivere , non leggo
  public BufferedReader bufferLettura=null;
  public BufferedWriter bufferScrittura=null;


  public Comunicazione(int portaRicezione,int portaInvio,InetAddress ip){
    this.ip=ip;
    this.portaInvio=portaInvio;
    this.portaRicezione=portaRicezione;
  }

  public void chiudiCanaleScrittura() throws MyException{
    try{
      client.close();
    }catch(Exception e){
       throw new MyException(MyException.msg1,MyException.tit1);
    }
  }

  public void chiudiCanaleLettura() throws MyException{
    try{
      server.close();
    }catch(Exception e){
       throw new MyException(MyException.msg1,MyException.tit1);
    }
  }

  // da chiamare subito dopo il costruttore
  public void impostaCanaleScrittura() throws Exception{

    client=new Socket(ip,portaInvio);
    bufferScrittura=new BufferedWriter(new OutputStreamWriter(
                             client.getOutputStream()));
  }
  // da chiamare subito dopo il costruttore
  public void impostaCanaleLettura() throws SocketTimeoutException,Exception{

      ServerSocket ss = new ServerSocket(portaRicezione);
      ss.setSoTimeout(TIMEOUT);
      server=ss.accept();
      bufferLettura=new BufferedReader(new InputStreamReader(
                              server.getInputStream()));
  }

  // da chiamare dopo aver costruito l'oggetto e aver impostato i 2 canali
  public String readLine() throws MyException{
    String x="";
    try{
      x = bufferLettura.readLine();
    }catch(Exception r){
      throw new MyException(MyException.msg1,MyException.tit1);
    }
    if (x==null){
      throw new MyException(MyException.msg4,MyException.tit1);
    }
    if (x.equals("END")){
      // devo uscire
      throw new MyException(MyException.msg4,MyException.tit1);
    }
    if(x.length()==2){
      // se e' una mossa
      int a_=-1+x.charAt(0)-'0';
      int b_=-1+x.charAt(1)-'0';
      x=""+a_+b_;

    }

    return x;
  }

  // da chiamare dopo aver costruito l'oggetto e aver impostato i 2 canali
  public void writeLine(String x) throws MyException{
    if(x.length()==2){
      // se e' una mossa
      int a_=1+x.charAt(0)-'0';
      int b_=1+x.charAt(1)-'0';
      x=""+a_+b_;
    }

    String end=System.getProperty("line.separator");
    String scrivi=x+end;
    try{
      bufferScrittura.write(scrivi);
      bufferScrittura.flush();
    }catch(Exception e){
      throw new MyException(MyException.msg1,MyException.tit1);
    }
  }

}
