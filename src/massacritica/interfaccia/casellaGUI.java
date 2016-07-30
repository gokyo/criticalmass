package massacritica.interfaccia;

import javax.swing.*;

public class casellaGUI extends JLabel{
  private int i,j;

  public casellaGUI(int i,int j)
    {
      this.i=i;
      this.j=j;
    }

  public int getx()
    {
      return i;
    }

  public int gety()
    {
      return j;
    }
}
