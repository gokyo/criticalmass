package massacritica.interfaccia;

import massacritica.comVal.*;
import massacritica.strGioco.*;
import massacritica.strGioco.tree.*;
import javax.swing.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class GUI extends JFrame {

  // caso remoto - remoto
  public static final int REMOTO_REMOTO=1;
  // casi locale - remoto
  public static final int  UMANO_REMOTO=2;
  public static final int     AI_REMOTO=3;
  // casi locale - locale
  public static final int   UMANO_UMANO=4;
  public static final int      UMANO_AI=5;
  public static final int         AI_AI=6;


  private int mod = 0;

  private boolean vittoria=false;

  private byte num_livelli_1=-1;
  private byte num_livelli_2=-1;

  private Configurazione conf=null;
  private Stato mioStato=null;
  private Stato statoTmp=null;
  private Comunicazione g1=null;
  private Comunicazione g2=null;
  private String coloreG1=new String("");
  private String coloreG2=new String("");

  private LinkedList listaStati=null;
  private boolean turnoGiocatore1;
//quando vale true è il turno del giocatore 1 altrimenti del giocatore 2

  private AlberoN ai1=null;
  private AlberoN ai2=null;
  private int i,j;


  private casellaGUI scacchiera[][]=new casellaGUI[5][6];
  GridLayout gridLayout1 = null;


  private JMenuBar menuGioco = new JMenuBar();
  private JMenu jMenuFile = new JMenu();
  private JMenuItem jMenuExit = new JMenuItem();
  private JMenuItem jMenuNuovoGioco= new JMenuItem();
  private JMenuItem jMenuConfigurazione= new JMenuItem();
  private JMenu jMenuHelp = new JMenu();
  private JMenuItem jMenuHelpAbout = new JMenuItem();


  public GUI() {
    try {
      this.addWindowListener(new WindowAdapter(){
        public void windowClosing(WindowEvent e){
          fine();
        }
      });
      jbInit();
    }catch(Exception ex) {
    }
  }

  private void fine(){
    try{
      g2.writeLine("END");
      g1.writeLine("END");
    }catch(Exception e){    }
    GUI.this.dispose();
    System.exit(0);
  }

  private void rimuoviMouseListeners(){
    try{
      for (i = 0; i < 5; i++)
        for (j = 0; j < 6; j++){
          MouseListener[] arr=scacchiera[i][j].getMouseListeners();
          for(int p=0;p<arr.length;++p)
            scacchiera[i][j].removeMouseListener(arr[p]);
        }
    }catch(Exception e){
      return;
    }
    System.gc();
  }

  private void aggiungiMouseListeners(){
  for (i = 0; i < 5; i++)
      for (j = 0; j < 6; j++) {
        scacchiera[i][j].addMouseListener(
            new MouseAdapter(){
              public void mouseClicked(MouseEvent e) {
                try{
                  onClick( (casellaGUI) e.getSource());
                }catch(MyException ex){
                  JOptionPane.showMessageDialog(null,
                                                ex.message,
                                                ex.title,
                                                JOptionPane.ERROR_MESSAGE);
                }
              }
             public void mouseEntered(MouseEvent e)
               {
                 onMouseOver((casellaGUI)e.getSource());
               }
            });
      }
}

  private void apriCanaleScrittura(Comunicazione g) throws MyException{
      int i=0;
      while (true){
        try{
          i++;
          Thread.sleep(100);
          g.impostaCanaleScrittura();

          break;
        }catch (Exception exx){

          if(i>=30)
              throw new MyException(MyException.msg5,MyException.tit1);
        }
      }
  }

private void leggoMossaRemota() throws MyException{

        String mossa = null;
        mossa = g2.readLine();

        if (mossa==null)
          throw new MyException(MyException.msg1,MyException.tit1);


        mioStato.aggiungiPedina((byte)getX(mossa),(byte)getY(mossa),
                                                     turnoGiocatore1);

        statoTmp=(Stato)mioStato.clone();
        visualizzazione( (byte) getX(mossa), (byte) getY(mossa));

        turnoGiocatore1=!turnoGiocatore1;
}


   private void onMouseOver(casellaGUI c)
     {
        try
        {
       if(turnoGiocatore1)
         {
          if(mioStato.scacchiera[c.getx()][c.gety()].giocatore!=turnoGiocatore1
             && mioStato.scacchiera[c.getx()][c.gety()].numeroPedine>0)
             c.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                   new ImageIcon("mouse"+coloreG1+"NO.GIF").
                                 getImage(),new Point(15,15),"puntatore"));

           else
             c.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
        new ImageIcon("mouse"+coloreG1+".GIF").
                                 getImage(),new Point(15,15),"puntatore"));
         }
       else
         if(mioStato.scacchiera[c.getx()][c.gety()].giocatore!=turnoGiocatore1
            && mioStato.scacchiera[c.getx()][c.gety()].numeroPedine>0)
            c.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                     new ImageIcon("mouse"+coloreG2+"NO.GIF").
                              getImage(),new Point(15,15),"puntatore"));
          else
            c.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                             new ImageIcon("mouse"+coloreG2+".GIF").
                                    getImage(),new Point(15,15),"puntatore"));
        }
        catch (HeadlessException ex1){    }

   }


  private void onClick(casellaGUI c) throws MyException
  {
    if(mod==UMANO_REMOTO){
      if(!mioStato.aggiungiPedina((byte)c.gety(),(byte)c.getx(),
                                            turnoGiocatore1)){
        return;
      }

      statoTmp=(Stato)mioStato.clone();

      visualizzazione( (byte) c.gety(), (byte) c.getx());
      //controlla anche la vittoria

        turnoGiocatore1 = false;
        g2.writeLine(c.gety() + "" + c.getx());
    if(vittoria){
         g2.chiudiCanaleLettura();
         g2.chiudiCanaleScrittura();
         finePartita(conf.partita.giocatore1.nomeGiocatore);
         return;
    }

      leggoMossaRemota();

      if(vittoria){
           g2.chiudiCanaleLettura();
           g2.chiudiCanaleScrittura();
           finePartita(conf.partita.giocatore2.nomeGiocatore);
           return;
      }

    }
    else if(mod==UMANO_UMANO){

       if(!mioStato.aggiungiPedina((byte)c.gety(),(byte)c.getx(),
                                                      turnoGiocatore1))
         return;


        statoTmp=(Stato)mioStato.clone();
        visualizzazione((byte)c.gety(),(byte)c.getx());

        if(vittoria){
          if(turnoGiocatore1)
            finePartita(conf.partita.giocatore1.nomeGiocatore);
          else
            finePartita(conf.partita.giocatore2.nomeGiocatore);
          return;
        }
        turnoGiocatore1 = !turnoGiocatore1;
    }
    else if(mod==UMANO_AI){

      if(!mioStato.aggiungiPedina((byte)c.gety(),(byte)c.getx(),
                                                   turnoGiocatore1)){
        return;
      }

      statoTmp=(Stato)mioStato.clone();
      visualizzazione((byte)c.gety(),(byte)c.getx());

      if(vittoria){
        finePartita(conf.partita.giocatore1.nomeGiocatore);
        return;
      }


      turnoGiocatore1 = false;

      // mossa di ai
      if(ai2==null){//la prima volta entro qui se l'albero inizia per secondo
        ai2 = new AlberoN((Stato)mioStato.clone(),num_livelli_2,false);
      }
      else{
        //aggiungo 2 livelli all'albero
        ai2.aggDueLivelli((Stato)mioStato.clone());
      }

      ai2.eval();
     Stato statoScelto=ai2.getRadice().getBestFiglio().getVal().getSituazione();

      statoTmp = (Stato)mioStato.clone();
      statoTmp.scacchiera
         [statoScelto.ultimaCasella.y][statoScelto.ultimaCasella.x].
                          numeroPedine++;
      statoTmp.scacchiera
          [statoScelto.ultimaCasella.y][statoScelto.ultimaCasella.x].
                          giocatore=ai2.getPrimo();

      try{
        Thread.sleep(conf.partita.sleepTimeAiAi);
      }catch(InterruptedException e){

      }

      visualizzazione(statoScelto.ultimaCasella.x,statoScelto.ultimaCasella.y);
      if(vittoria){
        finePartita(conf.partita.giocatore2.nomeGiocatore);
        return;
      }

      turnoGiocatore1 = true;
    }

  }


  private void inizializzaMenu()
  {
  jMenuFile.setText("File");
  jMenuHelp.setText("Help");
  jMenuNuovoGioco.setText("Nuova partita");
  jMenuConfigurazione.setText("Configurazione Partita");
  jMenuExit.setText("Exit");
  jMenuHelp.setText("Help");
  jMenuHelpAbout.setText("About");

   jMenuHelpAbout.addActionListener(
        new ActionListener() {
             public void actionPerformed(ActionEvent e)
             {
               AboutBox dlg = new AboutBox(GUI.this);
               Dimension dlgSize = dlg.getPreferredSize();
               Dimension frmSize = getSize();
               Point loc = getLocation();
               dlg.setLocation((frmSize.width - dlgSize.width) / 2 +
                         loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
               dlg.setModal(true);
               dlg.pack();
               dlg.show();
             }
          });


  jMenuNuovoGioco.addActionListener(
    new ActionListener() {
         public void actionPerformed(ActionEvent e)
         {
           startPartita();
         }
      });

   jMenuConfigurazione.addActionListener(
     new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          new ConfiguraPartita(GUI.this);
        }
      });


    jMenuExit.addActionListener(
      new ActionListener(){
         public void actionPerformed(ActionEvent e){
           fine();
         }
       });

  }

  void jbInit() throws Exception {

    inizializzaMenu();
    jMenuFile.add(jMenuNuovoGioco);
    jMenuFile.add(jMenuConfigurazione);
    jMenuFile.add(jMenuExit);
    jMenuHelp.add(jMenuHelpAbout);
    menuGioco.add(jMenuFile);
    menuGioco.add(jMenuHelp);
    this.setJMenuBar(menuGioco);

    inizializzaGridLayout();

    this.getContentPane().setBackground(SystemColor.black);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    this.setSize(438,378);
    this.setResizable(false);

    for (int i=0;i<5;i++)
      for(int j=0;j<6;j++){
        scacchiera[i][j]=new casellaGUI(i,j);
        this.getContentPane().add(scacchiera[i][j], null);
      }

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    setLocation((screenSize.width - frameSize.width) / 2,- 100+
                    (screenSize.height - frameSize.height) / 2);

    setTitle("Massa Critica");
    setVisible(true);
//    startPartita();

  }
  private void inizializzaGridLayout(){
    gridLayout1=new GridLayout();
    gridLayout1.setColumns(6);
    gridLayout1.setRows(5);
    gridLayout1.setHgap(2);
    gridLayout1.setVgap(2);
    this.getContentPane().setLayout(gridLayout1);
  }


  private void RemotoRemoto() throws MyException{
   rimuoviMouseListeners();
   try{
     g2 = new Comunicazione(conf.partita.giocatore2.portaInvio,
                            conf.partita.giocatore2.portaRicezione,
                            InetAddress.getByName(
                                          conf.partita.giocatore2.ipAddress));
     g1 = new Comunicazione(conf.partita.giocatore1.portaInvio,
                            conf.partita.giocatore1.portaRicezione,
                            InetAddress.getByName(
                                          conf.partita.giocatore1.ipAddress));
   }catch(Exception e){
     throw new MyException(MyException.msg2,MyException.tit1);
   }

     if (conf.partita.iniziaPerPrimo==1){

       try{
         g2.impostaCanaleLettura();
       }catch(SocketTimeoutException e){
         throw new MyException(MyException.msg3,MyException.tit1);
       }catch (Exception ex){
         throw new MyException(MyException.msg1,MyException.tit1);
       }

        if (g2.readLine().equals("0"))
          {
           apriCanaleScrittura(g1);
           g1.writeLine("0");
         }
         try{
           g1.impostaCanaleLettura();
         }catch(SocketTimeoutException e){
           throw new MyException(MyException.msg3,MyException.tit1);
         }catch (Exception ex){
           throw new MyException(MyException.msg1,MyException.tit1);
         }
         apriCanaleScrittura(g2);

      }
     else
     {

       try{
         g1.impostaCanaleLettura();
       }catch(SocketTimeoutException e){
         throw new MyException(MyException.msg3,MyException.tit1);
       }catch (Exception ex){
         throw new MyException(MyException.msg1,MyException.tit1);
       }

         if (g1.readLine().equals("0"))
           {
            apriCanaleScrittura(g2);
            g2.writeLine("0");

            try{
              g2.impostaCanaleLettura();
            }catch(SocketTimeoutException e){
              throw new MyException(MyException.msg3,MyException.tit1);
            }catch (Exception ex){
              throw new MyException(MyException.msg1,MyException.tit1);
            }

            String mossa=g2.readLine();
            mioStato.aggiungiPedina((byte)getX(mossa),(byte)getY(mossa),
                                                           turnoGiocatore1);
            statoTmp=(Stato)mioStato.clone();
            visualizzazione((byte)getX(mossa),(byte)getY(mossa));
            apriCanaleScrittura(g1);
            g1.writeLine(mossa);
            turnoGiocatore1=!turnoGiocatore1;

          }

     }

     partitaRemota();

  }

  private void impostaUmanoRemoto() throws MyException{

    String mossa=null;


    InetAddress t__=null;
    try{
      t__= InetAddress.getByName(conf.partita.giocatore2.ipAddress);
    }catch(UnknownHostException e){
      throw new MyException(MyException.msg2,MyException.tit1);
    }

    g2 = new Comunicazione(conf.partita.giocatore2.portaInvio,
                           conf.partita.giocatore2.portaRicezione,
                           t__);

    if (conf.partita.iniziaPerPrimo==1){
      try{
        g2.impostaCanaleLettura();
      }catch(SocketTimeoutException e){
        throw new MyException(MyException.msg3,MyException.tit1);
      }catch (Exception ex){
        throw new MyException(MyException.msg1,MyException.tit1);
      }


          mossa = g2.readLine();
          if (mossa==null)
            throw new MyException(MyException.msg1,MyException.tit1);
    if (mossa.equals("0"))
     {

       turnoGiocatore1 = true;
     }
     apriCanaleScrittura(g2);

    }
    else {
      apriCanaleScrittura(g2);
      g2.writeLine("0");

      turnoGiocatore1=false;

      try{
        g2.impostaCanaleLettura();
      }catch(SocketTimeoutException e){
        throw new MyException(MyException.msg3,MyException.tit1);
      }catch (Exception ex){
        throw new MyException(MyException.msg1,MyException.tit1);
      }


      try{
        mossa = g2.readLine();
      }catch(Exception e){
        throw new MyException("Il tuo avversario non e\' piu\' connesso!",
                              "Disconnessione dalla rete!");
      }

      mioStato.aggiungiPedina((byte)getX(mossa),(byte)getY(mossa),
                                                      turnoGiocatore1);

      visualizzaStato(mioStato);
      turnoGiocatore1=true;
    }

  }

  private void impostaAiRemoto() throws MyException{
    num_livelli_1=(byte)conf.partita.giocatore1.livelloDifficolta;
    String mossa=null;


    InetAddress t__=null;
    try{
      t__= InetAddress.getByName(conf.partita.giocatore2.ipAddress);
    }catch(UnknownHostException e){
      throw new MyException(MyException.msg2,MyException.tit1);

    }

    g2 = new Comunicazione(conf.partita.giocatore2.portaInvio,
                           conf.partita.giocatore2.portaRicezione,
                           t__);

    if (conf.partita.iniziaPerPrimo==1){

      try{
        g2.impostaCanaleLettura();
      }catch(SocketTimeoutException e){
        throw new MyException(MyException.msg3,MyException.tit1);
      }catch (Exception ex){
        throw new MyException(MyException.msg1,MyException.tit1);
      }

      mossa = g2.readLine();
      if (mossa==null)
        throw new MyException(MyException.msg1,MyException.tit1);

    if (mossa.equals("0"))
     {

       turnoGiocatore1 = true;

       ai1=new AlberoN(num_livelli_1,turnoGiocatore1);

     }

     apriCanaleScrittura(g2);
    }
    else {
     apriCanaleScrittura(g2);
     g2.writeLine("0");

     turnoGiocatore1=false;

      try{
        g2.impostaCanaleLettura();
      }catch(SocketTimeoutException e){
        throw new MyException(MyException.msg3,MyException.tit1);
      }catch (Exception ex){
        throw new MyException(MyException.msg1,MyException.tit1);
      }

      mossa = g2.readLine();

      mioStato.aggiungiPedina((byte)getX(mossa),(byte)getY(mossa),
                                                         turnoGiocatore1);
      ai1=new AlberoN(mioStato,num_livelli_1,true);
      visualizzaStato(mioStato);
      turnoGiocatore1=true;
    }
  }

  private void UmanoRemoto() throws MyException{
    rimuoviMouseListeners();
    aggiungiMouseListeners();
    impostaUmanoRemoto();
  }
  private void AiRemoto() throws MyException{
    rimuoviMouseListeners();
    impostaAiRemoto();
    while(true){
      // tocca ai
      try{
        Thread.sleep(conf.partita.sleepTimeAiAi);
      }catch(Exception e){
      }
      ai1.eval();
     Stato statoScelto=ai1.getRadice().getBestFiglio().getVal().getSituazione();
      statoTmp = (Stato)mioStato.clone();
      statoTmp.scacchiera
          [statoScelto.ultimaCasella.y][statoScelto.ultimaCasella.x].
                                                            numeroPedine++;
      statoTmp.scacchiera
          [statoScelto.ultimaCasella.y][statoScelto.ultimaCasella.x].
                                                   giocatore=ai1.getPrimo();

      visualizzazione(statoScelto.ultimaCasella.x,statoScelto.ultimaCasella.y);

        turnoGiocatore1 = false;
       g2.writeLine(statoScelto.ultimaCasella.x+""+statoScelto.ultimaCasella.y);

      if(vittoria){
          g2.chiudiCanaleLettura();
          g2.chiudiCanaleScrittura();
          finePartita(conf.partita.giocatore1.nomeGiocatore);
          return;
      }
      leggoMossaRemota();

      if(vittoria){
          g2.chiudiCanaleLettura();
          g2.chiudiCanaleScrittura();
          finePartita(conf.partita.giocatore2.nomeGiocatore);
          return;
      }

      ai1.aggDueLivelli(mioStato);
    }

  }
  private void UmanoUmano(){
    rimuoviMouseListeners();
    aggiungiMouseListeners();
  }
  private void UmanoAi(){
      rimuoviMouseListeners();
      aggiungiMouseListeners();

      num_livelli_2=(byte)(conf.partita.giocatore2.livelloDifficolta);
      if (conf.partita.iniziaPerPrimo==2)
       { ai2=new AlberoN(num_livelli_2,false);

         ai2.eval();
         mioStato=ai2.getRadice().getBestFiglio().getVal().getSituazione();

         statoTmp = (Stato)mioStato.clone();
         visualizzazione(statoTmp.ultimaCasella.x, statoTmp.ultimaCasella.y);
         turnoGiocatore1=true;
       }

    }

  private void AiAi(){
    num_livelli_1=(byte)(conf.partita.giocatore1.livelloDifficolta);
    num_livelli_2=(byte)(conf.partita.giocatore2.livelloDifficolta);

    rimuoviMouseListeners();
    if (conf.partita.iniziaPerPrimo==1)
     {ai1=new AlberoN(num_livelli_1,true);
     }
    else
      {ai2=new AlberoN(num_livelli_2,false);
      }
    partitaAiControAi();
    }


  private void startPartita(){
    try{

      this.vittoria=false;

      listaStati = null;
      ai1 = null;
      ai2 = null;

      System.gc();

      inizializzaStatoVuoto();
      initConf();
      if (conf.partita.iniziaPerPrimo == 1)
      {
        turnoGiocatore1 = true;
      }
      else
      {
        turnoGiocatore1 = false;
      }

      if (mod == REMOTO_REMOTO) RemotoRemoto();
      else if (mod == UMANO_REMOTO) UmanoRemoto();
      else if (mod == AI_REMOTO) AiRemoto();
      else if (mod == UMANO_UMANO) UmanoUmano();
      else if (mod == UMANO_AI) UmanoAi();
      else if (mod == AI_AI) AiAi();
    }catch(MyException e){
      JOptionPane.showMessageDialog(null,
                                    e.message,
                                    e.title,
                                    JOptionPane.ERROR_MESSAGE);
    }
  }

  private void inizializzaStatoVuoto(){

    mioStato=new Stato();
    statoTmp=(Stato)mioStato.clone();

    visualizzaStato(mioStato);

  }


  private void initConf() throws MyException
  {
    try{
      conf = new Configurazione("conf.txt");
    }
    catch(Exception e){
      throw new MyException("Controllare path e contenuto del file!",
                            "Errore in apertura del file di configurazione");


    }

    if (conf.partita.giocatore1.giocatoreLocale){
      if(conf.partita.giocatore2.giocatoreLocale){
        if(conf.partita.giocatore1.umano){
          if(conf.partita.giocatore2.umano)
            mod=UMANO_UMANO;
          else
            mod=UMANO_AI;
        }else{
          if(!conf.partita.giocatore2.umano)
            mod=AI_AI;
          else{
            throw new MyException("File di configurazione settato male",
                                  "Riconfigurare la partita!");
          }
        }
      }
      else{
        if(conf.partita.giocatore1.umano)
          mod=UMANO_REMOTO;
        else
          mod=AI_REMOTO;
      }

    }
    else{
      // giocatore 1 e' remoto
      if(conf.partita.giocatore2.giocatoreLocale){
            throw new MyException("File di configurazione settato male",
                                  "Riconfigurare la partita!");      }
      else{
       // remoto-remoto
       mod=REMOTO_REMOTO;
      }
    }
  //inizializzazione colori giocatori
  if(conf.partita.coloreGiallo == 1)
   {
    coloreG1="b";
    coloreG2="a";
   }
   else
   {
    coloreG2="b";
    coloreG1="a";
   }


  }


public void partitaRemota() throws MyException
  {
     while(true){
        String mossa=g1.readLine();
        mioStato.aggiungiPedina((byte)getX(mossa),(byte)getY(mossa),
                                                         turnoGiocatore1);
        statoTmp=(Stato)mioStato.clone();
        visualizzazione((byte)getX(mossa),(byte)getY(mossa));
        g2.writeLine(mossa);
        turnoGiocatore1=!turnoGiocatore1;
        if(vittoria){
          g1.chiudiCanaleScrittura();
          g2.chiudiCanaleScrittura();
          g1.chiudiCanaleLettura();
          g2.chiudiCanaleLettura();
          finePartita(conf.partita.giocatore1.nomeGiocatore);
          return;
        }

        mossa=g2.readLine();
        mioStato.aggiungiPedina((byte)getX(mossa),(byte)getY(mossa),
                                                          turnoGiocatore1);
        statoTmp=(Stato)mioStato.clone();
        visualizzazione((byte)getX(mossa),(byte)getY(mossa));
        g1.writeLine(mossa);
        turnoGiocatore1=!turnoGiocatore1;
        if(vittoria){
          g1.chiudiCanaleScrittura();
          g2.chiudiCanaleScrittura();
          g1.chiudiCanaleLettura();
          g2.chiudiCanaleLettura();
          finePartita(conf.partita.giocatore2.nomeGiocatore);
          return;
        }

     }
  }

  public void partitaAiControAi()
   {//stato scelto da una delle AI, serve per recuperare le informazioni
    //sulla casella in cui è stata inserita una pedina e visualizzare le
   // esplosioni
     Stato statoScelto;
      //per le prime due mosse non è necessario calcolare le esplosioni
      //multiple, in quanto non si possono
      //verificare.
     if(conf.partita.iniziaPerPrimo==1)
        {ai1.eval();
         mioStato=ai1.getRadice().getBestFiglio().getVal().getSituazione();
         visualizzaStato(mioStato);
         ai2 = new AlberoN((Stato)mioStato.clone(),num_livelli_2,false);
         ai2.eval();
         mioStato=ai2.getRadice().getBestFiglio().getVal().getSituazione();
         visualizzaStato(mioStato);
       }
       else
       {ai2.eval();
        mioStato=ai2.getRadice().getBestFiglio().getVal().getSituazione();
        visualizzaStato(mioStato);
        ai1 = new AlberoN((Stato)mioStato.clone(),num_livelli_1,true);
        ai1.eval();
        mioStato=ai1.getRadice().getBestFiglio().getVal().getSituazione();
        visualizzaStato(mioStato);
       }
     while(!vittoria)
     {


       if(turnoGiocatore1)
        {ai1.aggDueLivelli(mioStato);
         ai1.eval();
         statoScelto=ai1.getRadice().getBestFiglio().getVal().getSituazione();
         //calcolo espl mutliple in base allo stato attuale e alla mossa che
         //ha portato a statoScelto

         statoTmp = (Stato)mioStato.clone();
         statoTmp.scacchiera
             [statoScelto.ultimaCasella.y][statoScelto.ultimaCasella.x].
                                                   numeroPedine++;
         statoTmp.scacchiera
             [statoScelto.ultimaCasella.y][statoScelto.ultimaCasella.x].
                                                   giocatore=ai1.getPrimo();
         try{
           Thread.sleep(conf.partita.sleepTimeAiAi);
         }catch(Exception eg){
         }

           visualizzazione(
                      statoScelto.ultimaCasella.x,statoScelto.ultimaCasella.y);
        try{
          Thread.sleep(conf.partita.sleepTimeAiAi);
        }catch(Exception eg){
        }


         if (vittoria){
           finePartita(conf.partita.giocatore1.nomeGiocatore);
           return;
         }

         ai2.aggDueLivelli(mioStato);
         ai2.eval();
         NodoAlbero fff=ai2.getRadice();
         NodoLista ff=fff.getBestFiglio();
         NodoAlbero f=ff.getVal();
         statoScelto=f.getSituazione();
         //calcolo espl mutliple in base allo stato attuale e alla mossa che
        // ha portato a statoScelto
         statoTmp = (Stato)mioStato.clone();
         statoTmp.scacchiera
             [statoScelto.ultimaCasella.y][statoScelto.ultimaCasella.x].
                                                          numeroPedine++;
         statoTmp.scacchiera
             [statoScelto.ultimaCasella.y][statoScelto.ultimaCasella.x].
                                                   giocatore=ai2.getPrimo();
         try{
           Thread.sleep(conf.partita.sleepTimeAiAi);
         }catch(Exception eg){
         }
         visualizzazione(
                    statoScelto.ultimaCasella.x,statoScelto.ultimaCasella.y);
         try{
           Thread.sleep(conf.partita.sleepTimeAiAi);
         }catch(Exception eg){}

         if (vittoria){
           finePartita(conf.partita.giocatore2.nomeGiocatore);
           return;
         }

       }

       else//copiare da sopra invertendo l'ordine tra ai1 e ai2 - done
        {ai2.aggDueLivelli(mioStato);
         ai2.eval();
         statoScelto=ai2.getRadice().getBestFiglio().getVal().getSituazione();
         //calcolo espl mutliple in base allo stato attuale e alla mossa che
         //ha portato a statoScelto

         statoTmp = (Stato)mioStato.clone();
         statoTmp.scacchiera
                   [statoScelto.ultimaCasella.y][statoScelto.ultimaCasella.x].
                                                             numeroPedine++;
         statoTmp.scacchiera
                  [statoScelto.ultimaCasella.y][statoScelto.ultimaCasella.x].
                                                   giocatore=ai2.getPrimo();
         try{
           Thread.sleep(conf.partita.sleepTimeAiAi);
         }catch(Exception eg){
         }

       visualizzazione(statoScelto.ultimaCasella.x,statoScelto.ultimaCasella.y);

         try{
           Thread.sleep(conf.partita.sleepTimeAiAi);
         }catch(Exception eg){
         }


         if (vittoria){
           finePartita(conf.partita.giocatore2.nomeGiocatore);
           return;
         }

         ai1.aggDueLivelli(mioStato);
         ai1.eval();
         statoScelto=ai1.getRadice().getBestFiglio().getVal().getSituazione();
         //calcolo espl mutliple in base allo stato attuale e alla mossa
        // che ha portato a statoScelto

         statoTmp = (Stato)mioStato.clone();
         statoTmp.scacchiera
                    [statoScelto.ultimaCasella.y][statoScelto.ultimaCasella.x].
                                                         numeroPedine++;
         statoTmp.scacchiera
                   [statoScelto.ultimaCasella.y][statoScelto.ultimaCasella.x].
                                                   giocatore=ai1.getPrimo();

         try{
           Thread.sleep(conf.partita.sleepTimeAiAi);
         }catch(Exception eg){
         }
       visualizzazione(statoScelto.ultimaCasella.x,statoScelto.ultimaCasella.y);
         try{
           Thread.sleep(conf.partita.sleepTimeAiAi);
         }catch(Exception eg){
         }

         if (vittoria){
           finePartita(conf.partita.giocatore1.nomeGiocatore);
           return;
         }

       }
   }
   }

//statoTmp deve già essere stato modificato con la mossa in x,y
public void visualizzazione(byte x, byte y)
{
   //mostro la mossa appena fatta
   visualizzaStato(statoTmp);
   if(statoTmp.scacchiera[y][x].numeroPedine >=
      statoTmp.scacchiera[y][x].getMassaCritica())
   try{
      Thread.sleep(conf.partita.sleepTimeEsplosioni);
    }
    catch(Exception e){}

  //controllo se la mossa porta ad un'esplosione
  listaStati = Arbitro.controllaMossa(statoTmp,x,y);


   if (listaStati.size() != 0)//c'è almeno un'esplosione
    { while(listaStati.size()>0)
      {
       try{
        mioStato=(Stato)((StatoEsploso)listaStati.getLast()).getStato().clone();
       }catch(NoSuchElementException nse){}
        visEsplosioni();

        if (vittoria) {
          return;
        }

       listaStati=Arbitro.getStatiTransitoriEsplosioniMultiple(
                                      listaStati,0,(byte)listaStati.size());
      }
    }
   else
   {
      mioStato=(Stato)statoTmp.clone();
   }

}

public void visualizzaStato(Stato s)
 {
   String app=new String("");
   for(int i=0;i<6;i++)
     for(int j=0;j<5;j++)
      {
        app = "";
        if (s.get( (byte) i, (byte) j).numeroPedine == 0) app = "vuoto.JPG";
        else
        {
          if (s.get( (byte) i, (byte) j).numeroPedine > 4)
           app += "4";
          else
            app += s.get( (byte) i, (byte) j).numeroPedine;
          if (s.get( (byte) i, (byte) j).giocatore == false)
            app += coloreG2+".JPG";
          else
            app +=coloreG1+".JPG";
        }

        try{
          scacchiera[j][i].setIcon(new ImageIcon(app));

         }catch(Exception e){
         }
      }
   this.getContentPane().update(this.getContentPane().getGraphics());//REFRESH


    if (Arbitro.controllaVittoria(s,true)){
      this.vittoria=true;
    }
    else if (Arbitro.controllaVittoria(s,false)){
      this.vittoria=true;
  }
 }

 private void finePartita(String nome){

   int rc=JOptionPane.showConfirmDialog(null,"Vuoi rifare una nuova partita?",
                                        "Il giocatore "+nome+" ha vinto!    ",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.INFORMATION_MESSAGE);

   if(rc==JOptionPane.YES_OPTION || rc==JOptionPane.OK_OPTION){
     startPartita();
   }
   else if(rc==JOptionPane.NO_OPTION ||
           rc==JOptionPane.CANCEL_OPTION ||
           rc==JOptionPane.CLOSED_OPTION){
     fine();
   }
 }

 private void spriteEsplosione(byte x,byte y)
 {

          for(int j=4;j<13;j++)
            {
              scacchiera[y][x].setIcon(new ImageIcon("esplo "+j+".jpg"));
              this.getContentPane().update(this.getContentPane().getGraphics());
              try{
                if(conf.partita.sleepTimeEsplosioni>=9)
                  Thread.sleep(conf.partita.sleepTimeEsplosioni/9);
                else
                  Thread.sleep(1);
              }catch(Exception e){}

            }
            scacchiera[y][x].setIcon(new ImageIcon("vuoto.JPG"));
            this.getContentPane().update(this.getContentPane().getGraphics());
 }

public void visEsplosioni(){
  //if(listaStati==null) return;
  for(int i=0;i<listaStati.size();i++)
  {
    StatoEsploso se=((StatoEsploso)listaStati.get(i));
    visualizzaStato(se.getStato());
    if(vittoria) {
     return;
   }
    spriteEsplosione(se.getX(),se.getY());
  }
}

public int getX(String mess){
  return mess.charAt(0)-'0';
}

public int getY(String mess){
  return mess.charAt(1)-'0';
}

private void svuotaBufferEventi(){
  // Svuoto il buffer degli eventi del sistema operativo
  // Alla fine abbiamo deciso di non utilizzare questo metodo, perche'
  // a volte si pianta
  EventQueue q=Toolkit.getDefaultToolkit().getSystemEventQueue();
  q.push(new EventQueue());
  q.push(new EventQueue());
  q.push(new EventQueue());
}

  public static void main(String[] args) {
    new GUI();
  }
}
