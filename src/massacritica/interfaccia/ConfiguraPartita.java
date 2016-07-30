package massacritica.interfaccia;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;

public class ConfiguraPartita extends Dialog{

  public static final int FACILE=1;
  public static final int MEDIO=2;
  public static final int DIFFICILE=3;
  // DAL COMBOBOX PARTE DA INDEX=0 : AGGIUNGO 1

  int coloreBlu=0;
  int coloreGiallo=0;
  int iniziaPerPrimo=0;

  int localeRemoto1=0;
  int localeRemoto2=0;
  int umanoMacchina1=0;
  int umanoMacchina2=0;

  int sleepAI = 0;
  int sleepEsplosioni = 0;

  String nome1="";
  String nome2="";

  String ip1="";
  String ip2="";

  int portRicez1=-1;
  int portRicez2=-1;
  int portInvio1=-1;
  int portInvio2=-1;

  int livelloDifficolta1=-1;
  int livelloDifficolta2=-1;

  ButtonGroup gruppoLocaleRemoto1 = new ButtonGroup();
  ButtonGroup gruppoLocaleRemoto2 = new ButtonGroup();

  ButtonGroup gruppoUmanoMacchina1 = new ButtonGroup();
  ButtonGroup gruppoUmanoMacchina2 = new ButtonGroup();

  ButtonGroup gruppoIniziaPerPrimo = new ButtonGroup();
  ButtonGroup gruppoColoreGiallo = new ButtonGroup();
  ButtonGroup gruppoColoreBlu = new ButtonGroup();

  JButton button_configura = new JButton();
  JButton button_annulla = new JButton();

  JSlider slider_esplosioni = new JSlider(JSlider.HORIZONTAL,0,10000,1000);
  JSlider slider_ai = new JSlider(JSlider.HORIZONTAL,0,10000,1000);

  JRadioButton radio_inizia1 = new JRadioButton();
  JRadioButton radio_inizia2 = new JRadioButton();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JLabel label_sleep_esplosioni = new JLabel();
  JLabel label_sleep_ai = new JLabel();
  JLabel jLabel5 = new JLabel();
  JLabel jLabel6 = new JLabel();
  JLabel jLabel7 = new JLabel();
  JRadioButton radio_blu1 = new JRadioButton();
  JRadioButton radio_giallo1 = new JRadioButton();
  JRadioButton radio_blu2 = new JRadioButton();
  JRadioButton radio_giallo2 = new JRadioButton();
  JRadioButton radio_locale1 = new JRadioButton();
  JRadioButton radio_remoto1 = new JRadioButton();
  JRadioButton radio_locale2 = new JRadioButton();
  JRadioButton radio_remoto2 = new JRadioButton();
  JLabel jLabel8 = new JLabel();
  JLabel jLabel9 = new JLabel();
  JTextField textfield_nome1 = new JTextField();
  JTextField textfield_nome2 = new JTextField();
  JRadioButton radio_umano1 = new JRadioButton();
  JRadioButton radio_macchina1 = new JRadioButton();
  JRadioButton radio_umano2 = new JRadioButton();
  JRadioButton radio_macchina2 = new JRadioButton();
  JLabel jLabel10 = new JLabel();
  JLabel jLabel11 = new JLabel();
  JTextField textfield_ip1 = new JTextField();

  JTextField textfield_port_ricez1 = new JTextField();
  JTextField textfield_port_invio1 = new JTextField();
  JTextField textfield_port_ricez2 = new JTextField();
  JTextField textfield_port_invio2 = new JTextField();


  JLabel jLabel12 = new JLabel();
  JTextField textfield_ip2 = new JTextField();

  JLabel jLabel13 = new JLabel();

  JLabel jLabel14 = new JLabel();
  JLabel jLabel15 = new JLabel();

  JLabel label_difficolta1 = new JLabel();
  JLabel label_difficolta2 = new JLabel();
  JComboBox combo_difficolta1 = new JComboBox();
  JComboBox combo_difficolta2 = new JComboBox();

  public ConfiguraPartita(Frame frame){
    super(frame,"Configura Partita", true);
    try{
      jbInit();
      load();
      setVisible(true);
    }catch(Exception ex){

    }
  }

  protected void processWindowEvent(WindowEvent e) {
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      dispose();
    }
    super.processWindowEvent(e);
  }


  private void load() throws Exception{
    // carico da file la attuale configurazione
    Configurazione conf=new Configurazione("conf.txt");

    if(conf.partita.iniziaPerPrimo==1)
      radio_inizia1.setSelected(true);
    else if(conf.partita.iniziaPerPrimo==2)
      radio_inizia2.setSelected(true);

    if(conf.partita.coloreBlu==1)
      radio_blu1.setSelected(true);
    else if(conf.partita.coloreBlu==2)
      radio_blu2.setSelected(true);

    if(conf.partita.coloreGiallo==1)
      radio_giallo1.setSelected(true);
    else if(conf.partita.coloreGiallo==2)
      radio_giallo2.setSelected(true);

    slider_ai.setValue(conf.partita.sleepTimeAiAi);
    slider_esplosioni.setValue(conf.partita.sleepTimeEsplosioni);


    // carico gioc 1
    textfield_nome1.setText(conf.partita.giocatore1.nomeGiocatore);
    if(conf.partita.giocatore1.giocatoreLocale) {
      radio_locale1.setSelected(true);
      radio_umano1.setEnabled(true);
      radio_macchina1.setEnabled(true);

      textfield_ip1.setEnabled(false);
      textfield_port_invio1.setEnabled(false);
      textfield_port_ricez1.setEnabled(false);

      if(conf.partita.giocatore1.umano){
        radio_umano1.setSelected(true);
        combo_difficolta1.setEnabled(false);
      }
      else{
        radio_macchina1.setSelected(true);
        combo_difficolta1.setEnabled(true);
        combo_difficolta1.setSelectedIndex(
                                conf.partita.giocatore1.livelloDifficolta-1);
      }
    }
    else{
      radio_remoto1.setSelected(true);
      radio_umano1.setEnabled(false);
      radio_macchina1.setEnabled(false);

      textfield_ip1.setEnabled(true);
      textfield_port_invio1.setEnabled(true);
      textfield_port_ricez1.setEnabled(true);

      textfield_ip1.setText(conf.partita.giocatore1.ipAddress);
      textfield_port_invio1.setText(conf.partita.giocatore1.portaInvio+"");
      textfield_port_ricez1.setText(conf.partita.giocatore1.portaRicezione+"");
    }


    // carico gioc 2
    textfield_nome2.setText(conf.partita.giocatore2.nomeGiocatore);
    if(conf.partita.giocatore2.giocatoreLocale) {
      radio_locale2.setSelected(true);
      radio_umano2.setEnabled(true);
      radio_macchina2.setEnabled(true);

      textfield_ip2.setEnabled(false);
      textfield_port_invio2.setEnabled(false);
      textfield_port_ricez2.setEnabled(false);

      if(conf.partita.giocatore2.umano){
        radio_umano2.setSelected(true);
        combo_difficolta2.setEnabled(false);
      }
      else{
        radio_macchina2.setSelected(true);
        combo_difficolta2.setEnabled(true);
        combo_difficolta2.setSelectedIndex(
                                conf.partita.giocatore2.livelloDifficolta-1);
      }
    }
    else{
      radio_remoto2.setSelected(true);
      radio_umano2.setEnabled(false);
      radio_macchina2.setEnabled(false);

      textfield_ip2.setEnabled(true);
      textfield_port_invio2.setEnabled(true);
      textfield_port_ricez2.setEnabled(true);

      textfield_ip2.setText(conf.partita.giocatore2.ipAddress);
      textfield_port_invio2.setText(conf.partita.giocatore2.portaInvio+"");
      textfield_port_ricez2.setText(conf.partita.giocatore2.portaRicezione+"");
    }



  }


  private void jbInit() throws Exception{

    this.setLayout(null);

    button_configura.setBounds(new Rectangle(160, 620, 115, 40));
    button_configura.setText("Configura");
    button_annulla.setBounds(new Rectangle(380, 620, 115, 40));
    button_annulla.setText("Annulla");

    slider_esplosioni.setBounds(new Rectangle(280, 480, 300, 55));
    slider_ai.setBounds(new Rectangle(280, 540, 300, 55));


    slider_esplosioni.setMajorTickSpacing(2000);
    slider_esplosioni.setMinorTickSpacing(1000);
    slider_esplosioni.setPaintTicks(true);
    slider_esplosioni.setPaintLabels(true);


    slider_ai.setMajorTickSpacing(2000);
    slider_ai.setMinorTickSpacing(1000);
    slider_ai.setPaintTicks(true);
    slider_ai.setPaintLabels(true);


    radio_inizia1.setText("Giocatore 1");
    radio_inizia1.setBounds(new Rectangle(295, 343, 140, 30));
    radio_inizia2.setText("Giocatore 2");
    radio_inizia2.setBounds(new Rectangle(485, 342, 140, 30));
    jLabel1.setText("Giocatore 1");
    jLabel1.setBounds(new Rectangle(90, 35, 110, 25));
    jLabel2.setText("Giocatore 2");
    jLabel2.setBounds(new Rectangle(490, 35, 110, 25));
    label_sleep_esplosioni.setBounds(new Rectangle(40, 480, 200, 25));
    label_sleep_esplosioni.setText("Tempo sleep esplosioni [msec]");
    label_sleep_ai.setBounds(new Rectangle(40, 540, 200, 25));
    label_sleep_ai.setText("Tempo sleep mossa AI [msec]");
    jLabel5.setText("Colore Giallo");
    jLabel5.setBounds(new Rectangle(38, 427, 130, 25));
    jLabel6.setBounds(new Rectangle(41, 386, 130, 25));
    jLabel6.setText("Colore Blu");
    jLabel7.setBounds(new Rectangle(38, 346, 130, 25));
    jLabel7.setText("Inizia per primo");
    radio_blu1.setBounds(new Rectangle(295, 384, 140, 30));
    radio_blu1.setText("Giocatore 1");
    radio_giallo1.setBounds(new Rectangle(295, 427, 140, 30));
    radio_giallo1.setText("Giocatore 1");
    radio_blu2.setBounds(new Rectangle(485, 384, 140, 30));
    radio_blu2.setText("Giocatore 2");
    radio_giallo2.setBounds(new Rectangle(485, 431, 140, 30));
    radio_giallo2.setText("Giocatore 2");
    radio_locale1.setBounds(new Rectangle(30, 90, 100, 30));
    radio_locale1.setText("Locale");
    radio_remoto1.setBounds(new Rectangle(135, 91, 100, 30));
    radio_remoto1.setText("Remoto");
    radio_locale2.setText("Locale");
    radio_locale2.setBounds(new Rectangle(420, 90, 100, 30));
    radio_remoto2.setText("Remoto");
    radio_remoto2.setBounds(new Rectangle(530, 90, 100, 30));
    jLabel8.setText("Nome");
    jLabel8.setBounds(new Rectangle(40, 60, 50, 25));
    jLabel9.setText("Nome");
    jLabel9.setBounds(new Rectangle(430, 60, 50, 25));
    textfield_nome1.setText("");
    textfield_nome1.setBounds(new Rectangle(119, 61, 100, 25));
    textfield_nome2.setText("");
    textfield_nome2.setBounds(new Rectangle(520, 60, 100, 25));
    radio_umano1.setText("Umano");
    radio_umano1.setBounds(new Rectangle(30, 125, 90, 30));
    radio_macchina1.setText("Macchina");
    radio_macchina1.setBounds(new Rectangle(135, 125, 90, 30));
    radio_umano2.setBounds(new Rectangle(420, 125, 90, 30));
    radio_umano2.setText("Umano");
    radio_macchina2.setBounds(new Rectangle(530, 125, 90, 30));
    radio_macchina2.setText("Macchina");
    jLabel10.setBounds(new Rectangle(15, 181, 140, 25));
    jLabel10.setText("Ip (o Nome Macchina)");

    jLabel11.setText("Porta Ricezione");
    textfield_ip1.setBounds(new Rectangle(170, 180, 140, 25));
    textfield_ip1.setText("");


    textfield_port_ricez1.setBounds(new Rectangle(170, 220, 140, 25));
    textfield_port_invio1.setBounds(new Rectangle(170, 260, 140, 25));


    textfield_port_ricez2.setBounds(new Rectangle(530, 220, 140, 25));
    textfield_port_invio2.setBounds(new Rectangle(530, 260, 140, 25));


    textfield_port_invio1.setText("");
    textfield_port_invio2.setText("");
    textfield_port_ricez1.setText("");
    textfield_port_ricez2.setText("");

    jLabel12.setText("Ip (o Nome Macchina)");
    jLabel12.setBounds(new Rectangle(370, 180, 140, 25));
    textfield_ip2.setText("");
    textfield_ip2.setBounds(new Rectangle(530, 180, 140, 25));
    jLabel13.setText("Porta Ricezione");
    jLabel14.setText("Porta Invio");
    jLabel15.setText("Porta Invio");


    jLabel11.setBounds(new Rectangle(15, 220, 140, 25));
    jLabel13.setBounds(new Rectangle(370, 220, 140, 25));
    jLabel14.setBounds(new Rectangle(15, 260, 140, 25));
    jLabel15.setBounds(new Rectangle(370, 260, 140, 25));

    label_difficolta1.setText("Livello difficolta\'");
    label_difficolta2.setText("Livello difficolta\'");

    label_difficolta1.setBounds(new Rectangle(15, 300, 115, 25));
    label_difficolta2.setBounds(new Rectangle(370, 300, 115, 25));

    combo_difficolta1.setBounds(170, 300, 130, 25);
    combo_difficolta2.setBounds(530, 300, 130, 25);

    combo_difficolta1.addItem("Facile");
    combo_difficolta1.addItem("Medio");
    combo_difficolta1.addItem("Difficile");

    combo_difficolta2.addItem("Facile");
    combo_difficolta2.addItem("Medio");
    combo_difficolta2.addItem("Difficile");

    gruppoColoreBlu.add(radio_blu1);
    gruppoColoreBlu.add(radio_blu2);
    gruppoColoreGiallo.add(radio_giallo1);
    gruppoColoreGiallo.add(radio_giallo2);
    gruppoIniziaPerPrimo.add(radio_inizia1);
    gruppoIniziaPerPrimo.add(radio_inizia2);
    gruppoLocaleRemoto1.add(radio_locale1);
    gruppoLocaleRemoto1.add(radio_remoto1);
    gruppoLocaleRemoto2.add(radio_locale2);
    gruppoLocaleRemoto2.add(radio_remoto2);



    gruppoUmanoMacchina1.add(radio_umano1);
    gruppoUmanoMacchina1.add(radio_macchina1);
    gruppoUmanoMacchina2.add(radio_umano2);
    gruppoUmanoMacchina2.add(radio_macchina2);

//  NON SERVIREBBE PIU' (CARICO DA FILE!)

    radio_umano1.setEnabled(false);
    radio_macchina1.setEnabled(false);
    textfield_ip1.setEnabled(false);

    combo_difficolta1.setEnabled(false);

    radio_umano2.setEnabled(false);
    radio_macchina2.setEnabled(false);
    textfield_ip2.setEnabled(false);

    combo_difficolta2.setEnabled(false);

    textfield_port_ricez1.setEnabled(false);
    textfield_port_invio1.setEnabled(false);
    textfield_port_ricez2.setEnabled(false);
    textfield_port_invio2.setEnabled(false);



    add(jLabel2, null);
    add(jLabel1, null);
    add(button_configura, null);
    add(button_annulla, null);
    add(jLabel8, null);
    add(jLabel9, null);
    add(textfield_nome2, null);
    add(radio_locale1, null);
    add(radio_umano1, null);
    add(jLabel11, null);
    add(textfield_ip1, null);
    add(radio_umano2, null);
    add(radio_locale2, null);
    add(radio_remoto2, null);
    add(jLabel12, null);
    add(textfield_ip2, null);
    add(jLabel13, null);

    add(textfield_port_ricez1, null);
    add(textfield_port_ricez2, null);
    add(textfield_port_invio1, null);
    add(textfield_port_invio2, null);

    add(label_sleep_ai, null);
    add(label_sleep_esplosioni, null);
    add(jLabel10, null);
    add(radio_remoto1, null);
    add(radio_macchina1, null);
    add(radio_macchina2, null);
    add(radio_giallo2, null);
    add(radio_blu2, null);
    add(radio_inizia2, null);
    add(radio_inizia1, null);
    add(radio_giallo1, null);
    add(jLabel6, null);
    add(jLabel7, null);
    add(jLabel5, null);
    add(label_difficolta1, null);
    add(label_difficolta2, null);
    add(combo_difficolta1, null);
    add(combo_difficolta2, null);
    add(slider_ai, null);
    add(slider_esplosioni, null);
    add(radio_blu1, null);
    add(textfield_nome1, null);
    add(jLabel14,null);
    add(jLabel15,null);

    button_annulla.setBackground(Color.LIGHT_GRAY);
    button_configura.setBackground(Color.LIGHT_GRAY);
    slider_ai.setBackground(Color.LIGHT_GRAY);
    slider_esplosioni.setBackground(Color.LIGHT_GRAY);
    combo_difficolta1.setBackground(Color.LIGHT_GRAY);
    combo_difficolta2.setBackground(Color.LIGHT_GRAY);
    radio_blu1.setBackground(Color.LIGHT_GRAY);
    radio_blu2.setBackground(Color.LIGHT_GRAY);
    radio_giallo1.setBackground(Color.LIGHT_GRAY);
    radio_giallo2.setBackground(Color.LIGHT_GRAY);
    radio_inizia1.setBackground(Color.LIGHT_GRAY);
    radio_inizia2.setBackground(Color.LIGHT_GRAY);
    radio_remoto1.setBackground(Color.LIGHT_GRAY);
    radio_remoto2.setBackground(Color.LIGHT_GRAY);
    radio_locale1.setBackground(Color.LIGHT_GRAY);
    radio_locale2.setBackground(Color.LIGHT_GRAY);
    radio_umano1.setBackground(Color.LIGHT_GRAY);
    radio_umano2.setBackground(Color.LIGHT_GRAY);
    radio_macchina1.setBackground(Color.LIGHT_GRAY);
    radio_macchina2.setBackground(Color.LIGHT_GRAY);




    this.setBackground(Color.LIGHT_GRAY);
    setSize(700,700);
    setLocation(170,20);
    setResizable(false);


    button_annulla.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        ConfiguraPartita.this.dispose();
      }
    });

    button_configura.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        try{
          checkInput();
        }catch(Exception ex){

          JOptionPane.showMessageDialog(null,
                                        ex.getMessage(),
                                        "Configurazione non valida",
                                        JOptionPane.ERROR_MESSAGE);
          return;
        }

          JOptionPane.showMessageDialog(null,
                                        "E\' stata salvata la configurazione"+
                                        " per le prossime partite",
                                        "Configurazione salvata",
                                        JOptionPane.INFORMATION_MESSAGE);

          ConfiguraPartita.this.dispose();

      }
    });

    radio_umano1.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        radio_locale1.setEnabled(true);
        combo_difficolta1.setEnabled(false);
        textfield_ip1.setEnabled(false);
        textfield_port_ricez1.setEnabled(false);
        textfield_port_invio1.setEnabled(false);
      }
    });
    radio_macchina1.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        radio_locale1.setEnabled(true);
        combo_difficolta1.setEnabled(true);
        textfield_ip1.setEnabled(false);
        textfield_port_ricez1.setEnabled(false);
        textfield_port_invio1.setEnabled(false);
      }
    });
    radio_locale1.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        textfield_ip1.setEnabled(false);
        textfield_port_ricez1.setEnabled(false);
        textfield_port_invio1.setEnabled(false);
        radio_macchina1.setEnabled(true);
        radio_umano1.setEnabled(true);
        combo_difficolta1.setEnabled(true);
      }
    });
    radio_remoto1.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        textfield_ip1.setEnabled(true);
        textfield_port_ricez1.setEnabled(true);
        textfield_port_invio1.setEnabled(true);
        radio_macchina1.setEnabled(false);
        radio_umano1.setEnabled(false);
        combo_difficolta1.setEnabled(false);
      }
    });



    radio_umano2.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        radio_locale2.setEnabled(true);
        combo_difficolta2.setEnabled(false);
        textfield_ip2.setEnabled(false);
        textfield_port_ricez2.setEnabled(false);
        textfield_port_invio2.setEnabled(false);
      }
    });
    radio_macchina2.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        radio_locale2.setEnabled(true);
        combo_difficolta2.setEnabled(true);
        textfield_ip2.setEnabled(false);
        textfield_port_ricez2.setEnabled(false);
        textfield_port_invio2.setEnabled(false);
      }
    });
    radio_locale2.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        textfield_ip2.setEnabled(false);
        textfield_port_ricez2.setEnabled(false);
        textfield_port_invio2.setEnabled(false);
        radio_macchina2.setEnabled(true);
        radio_umano2.setEnabled(true);
        combo_difficolta2.setEnabled(true);
      }
    });
    radio_remoto2.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        textfield_ip2.setEnabled(true);
        textfield_port_ricez2.setEnabled(true);
        textfield_port_invio2.setEnabled(true);
        radio_macchina2.setEnabled(false);
        radio_umano2.setEnabled(false);
        combo_difficolta2.setEnabled(false);
      }
    });




    radio_giallo1.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        radio_blu2.setSelected(true);
      }
    });
    radio_giallo2.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        radio_blu1.setSelected(true);
      }
    });
    radio_blu1.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        radio_giallo2.setSelected(true);
      }
    });
    radio_blu2.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        radio_giallo1.setSelected(true);
      }
    });

    this.addWindowListener(new WindowAdapter(){
       public void windowClosing(){
         ConfiguraPartita.this.dispose();

       }

    });

  }

  private void checkInput() throws Exception {

    sleepAI =slider_ai.getValue();
    sleepEsplosioni =slider_esplosioni.getValue();

    nome1=textfield_nome1.getText();
    nome2=textfield_nome2.getText();



    if(radio_blu1.isSelected())
      coloreBlu=1;
    else if(radio_blu2.isSelected())
      coloreBlu=2;
    else coloreBlu=0;


    if(radio_giallo1.isSelected())
      coloreGiallo=1;
    else if(radio_giallo2.isSelected())
      coloreGiallo=2;
    else coloreGiallo=0;

    if(radio_inizia1.isSelected())
      iniziaPerPrimo=1;
    else if(radio_inizia2.isSelected())
      iniziaPerPrimo=2;
    else iniziaPerPrimo=0;

    if(radio_locale1.isSelected())
      localeRemoto1=1;
    else if(radio_remoto1.isSelected())
      localeRemoto1=2;
    else localeRemoto1=0;

    if(radio_locale2.isSelected())
      localeRemoto2=1;
    else if(radio_remoto2.isSelected())
      localeRemoto2=2;
    else localeRemoto2=0;

    if(radio_umano1.isSelected())
      umanoMacchina1=1;
    else if(radio_macchina1.isSelected())
      umanoMacchina1=2;
    else umanoMacchina1=0;

    if(radio_umano2.isSelected())
      umanoMacchina2=1;
    else if(radio_macchina2.isSelected())
      umanoMacchina2=2;
    else umanoMacchina2=0;


    if(coloreBlu==0)
      throw new Exception("Selezionare il giocatore con il colore blu!");
    if(coloreGiallo==0)
      throw new Exception("Selezionare il giocatore con il colore giallo!");
    if(iniziaPerPrimo==0)
      throw new Exception("Selezionare il giocatore che inizia per primo!");


    if(localeRemoto1==0)
      throw new Exception("Decidere se il giocatore1 e\' locale o remoto !");
    if(localeRemoto2==0)
      throw new Exception("Decidere se il giocatore2 e\' locale o remoto !");


    if(localeRemoto1==1){

      // gioc1 locale
      if(umanoMacchina1==0)
       throw new Exception("Decidere se il giocatore1 e\' umano o macchina !");

      if(umanoMacchina1==1){
        ;// gioc1 umano

      }
      else if(umanoMacchina1==2){
        // gioc1 macchina
        String level=(String)combo_difficolta1.getSelectedItem();
        if(level.equalsIgnoreCase("FACILE"))
          livelloDifficolta1=FACILE;
        else if(level.equalsIgnoreCase("MEDIO"))
          livelloDifficolta1=MEDIO;
        else// if(level.equalsIgnoreCase("DIFFICILE"))
          livelloDifficolta1=DIFFICILE;
      }


    }
    else if(localeRemoto1==2){
      // gioc1 remoto
      try{
        portInvio1=Integer.parseInt(textfield_port_invio1.getText());
        portRicez1=Integer.parseInt(textfield_port_ricez1.getText());

      }catch(Exception e){
        throw new Exception("Inserire un numero intero"
                            +" per la porta del giocatore1!");
      }
      if (portRicez1<=1024)
        throw new Exception("Inserire un numero > 1024"+
                            " per la porta del giocatore1!");
      if (portInvio1<=1024)
        throw new Exception("Inserire un numero > 1024"+
                            " per la porta del giocatore1!");


      ip1=textfield_ip1.getText();
      try{
        if(ip1.equalsIgnoreCase("127.0.0.1") ||
           ip1.equalsIgnoreCase("localhost"))
          throw new Exception("Indirizzo IP o "+
                              "Nome Macchina non valido per il giocatore1!");
        InetAddress aa=InetAddress.getByName(ip1);
      }catch(Exception e){
        throw new Exception("Indirizzo IP o "+
                            "Nome Macchina non valido per il giocatore1!");
      }

    }


       // controllare gioc 2

       if(localeRemoto2==1){

         // gioc2 locale
         if(umanoMacchina2==0)
           throw new Exception("Decidere se il giocatore2 e\' "+
                               "umano o macchina !");

         if(umanoMacchina2==1){
           ;// gioc2 umano

         }
         else if(umanoMacchina2==2){
           // gioc2 macchina
           String level=(String)combo_difficolta2.getSelectedItem();
           if(level.equalsIgnoreCase("FACILE"))
             livelloDifficolta2=FACILE;
           else if(level.equalsIgnoreCase("MEDIO"))
             livelloDifficolta2=MEDIO;
           else// if(level.equalsIgnoreCase("DIFFICILE"))
             livelloDifficolta2=DIFFICILE;
         }


       }
       else if(localeRemoto2==2){
         // gioc2 remoto
         try{
           portInvio2=Integer.parseInt(textfield_port_invio2.getText());
           portRicez2=Integer.parseInt(textfield_port_ricez2.getText());

         }catch(Exception e){
           throw new Exception("Inserire un numero intero per"+
                               " la porta del giocatore2!");
         }
         if (portInvio2<=1024)
           throw new Exception("Inserire un numero > 1024 "+
                               "per la porta del giocatore2!");
         if (portRicez2<=1024)
           throw new Exception("Inserire un numero > 1024 "+
                               "per la porta del giocatore2!");

         ip2=textfield_ip2.getText();
         try{
           if(ip2.equalsIgnoreCase("127.0.0.1") ||
              ip2.equalsIgnoreCase("localhost"))
             throw new Exception("Indirizzo IP o Nome Macchina "+
                                 "non valido per il giocatore2!");
           InetAddress.getByName(ip2);
         }catch(Exception e){
           throw new Exception("Indirizzo IP o Nome Macchina "+
                               "non valido per il giocatore2!");
         }

       }

    if(localeRemoto1==2 && localeRemoto2==1){
      String end=System.getProperty("line.separator");
      throw new Exception("Giocatore1 Remoto, Giocatore2 Locale: "+
                          "configurazione non valida!"+end+
                          "Selezionare Giocatore1 Locale, Giocatore2 Remoto!");
    }

    if(localeRemoto1==1 && localeRemoto2==1 &&
       umanoMacchina1==2 && umanoMacchina2==1){
      String end=System.getProperty("line.separator");
      throw new Exception("Giocatore1 Macchina, Giocatore2 Umano: "+
                          "configurazione non valida!"+end+
                         "Selezionare Giocatore1 Umano, Giocatore2 Macchina!");
    }

    save();
    // salvo su file

  }

  private void save() throws Exception{


    File f=new File("conf.txt");
    if (!f.exists()) throw new FileNotFoundException(
                           "File di configurazione non trovato!");

    BufferedWriter buff=new BufferedWriter(new FileWriter(f));
    String end=System.getProperty("line.separator");
    String separatorToken=" ";

    buff.write(end);

    buff.write("TEMPO_SLEEP_ESPLOSIONI"+separatorToken+
                                         this.sleepEsplosioni+end);
    buff.write("TEMPO_SLEEP_AI_AI"+separatorToken+this.sleepAI+end);
    buff.write("INIZIA_PER_PRIMO"+separatorToken+this.iniziaPerPrimo+end);
    buff.write("COLORE_GIALLO"+separatorToken+this.coloreGiallo+end);
    buff.write("COLORE_BLU"+separatorToken+this.coloreBlu+end);

    buff.write(end);

    buff.write("GIOCATORE1_NOME"+separatorToken+this.nome1+end);
    if(this.localeRemoto1==1)
      buff.write("GIOCATORE1_HOST"+separatorToken+"LOCALE"+end);
    else //if(this.localeRemoto1==2)
      buff.write("GIOCATORE1_HOST"+separatorToken+"REMOTO"+end);

    if(this.umanoMacchina1==1)
      buff.write("GIOCATORE1_TIPO"+separatorToken+"UMANO"+end);
    else{ //if(this.umanoMacchina1==2)
      buff.write("GIOCATORE1_TIPO"+separatorToken+"MACCHINA"+end);
      buff.write("GIOCATORE1_DIFFICOLTA"+separatorToken+livelloDifficolta1+end);
    }

    buff.write("GIOCATORE1_IP_ADDRESS"+separatorToken+this.ip1+end);
    buff.write("GIOCATORE1_PORTA_INVIO"+separatorToken+this.portInvio1+end);
    buff.write("GIOCATORE1_PORTA_RICEZIONE"+separatorToken+this.portRicez1+end);

    buff.write(end);

    buff.write("GIOCATORE2_NOME"+separatorToken+this.nome2+end);
    if(this.localeRemoto2==1)
      buff.write("GIOCATORE2_HOST"+separatorToken+"LOCALE"+end);
    else //if(this.localeRemoto2==2)
      buff.write("GIOCATORE2_HOST"+separatorToken+"REMOTO"+end);

    if(this.umanoMacchina2==1)
      buff.write("GIOCATORE2_TIPO"+separatorToken+"UMANO"+end);
    else{ //if(this.umanoMacchina2==2)
      buff.write("GIOCATORE2_TIPO"+separatorToken+"MACCHINA"+end);
      buff.write("GIOCATORE2_DIFFICOLTA"+separatorToken+livelloDifficolta2+end);
    }

    buff.write("GIOCATORE2_IP_ADDRESS"+separatorToken+this.ip2+end);
    buff.write("GIOCATORE2_PORTA_INVIO"+separatorToken+this.portInvio2+end);
    buff.write("GIOCATORE2_PORTA_RICEZIONE"+separatorToken+this.portRicez2+end);

    buff.write(end);

    buff.flush();
    buff.close();
  }
}
