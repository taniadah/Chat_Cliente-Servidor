/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente.servidor;

/**
 *
 * @author tania
 */

//Esta ventana es para pedir el nombre del cliente se extiene de la clase JDialog
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class Dialogo extends JDialog{
  private JTextField jtfNombre;
  private JLabel jLNombre;
  private JPanel p1;
  private JButton aceptar;
  public static String label, nombre="";


  public Dialogo(Frame parent, boolean modal, String accion, String la){
    super(parent, modal);

    this.label = la;
    setTitle(accion);
    setSize(300, 200);
    setLayout(new BorderLayout());
    this.setLocationRelativeTo(null);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    initComponents();
  }
  //getNombre retorna el nombre del cliente para la creación de la ventana del cliente
  public String getNombre(){
    return this.nombre;
  }

  public void initComponents() {
    p1 = new JPanel();
    p1.setBackground(new Color(88, 214, 141));
    p1.setLayout(null);

    aceptar = new JButton("Aceptar");
    aceptar.setBounds(151,100,90,25);
    EventoAceptar ev = new EventoAceptar();
    aceptar.addActionListener(ev);

    jtfNombre = new JTextField();
    jtfNombre.setBounds(80,50,170,28);

    jLNombre = new JLabel(label);
    jLNombre.setBounds(30,50, 80,28);


    p1.add(aceptar);
    p1.add(jtfNombre);
    p1.add(jLNombre);
    add(p1);
  }
  public class EventoAceptar implements ActionListener{
    public void actionPerformed(ActionEvent ev){
      //System.out.println("Sip");
      String name = jtfNombre.getText();
      nombre = name;
      //Si la persona no ingresa el nombre manda un error y no deja crear una ventana con un nombre vacio
      if (nombre.equals("")){
        JOptionPane.showMessageDialog(null, "Debe de escribir un nombre", "Error", JOptionPane.ERROR_MESSAGE);
       //de lo contrario crea al nuevo cliente con la classe cliente
      }else{
        System.out.println(nombre);
        new Cliente().go(nombre);
        setVisible(false);
      }
    }
  }//Action listener
}
