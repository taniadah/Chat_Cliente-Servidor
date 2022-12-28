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
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
//clase del cliente
public class Cliente{
  private JTextArea mensajero;
  private JTextField mensajito;
  public  JTextField nC;
  private BufferedReader lectura;
  private PrintWriter escritura;
  private Socket sock;
  Random r = new Random();
  private String nombre;
  private TextPrompt txtMensaje, txtReceptor;

  //ArrayList<BufferedReader> clienteIS;
  //ArrayList<PrintWriter> clienteOS;

//inicia los componentes del cliente y el hilo de conexion para cada cliente que entra
  public void go(String nombre){
    this.nombre = nombre;
    int x = r.nextInt(255);
    int y = r.nextInt(255);
    int z = r.nextInt(255);

    JFrame frame = new JFrame(nombre);
    frame.setResizable(false);
    frame.setLayout(new BorderLayout());
    frame.setLocationRelativeTo(null);
    JPanel panel = new JPanel();
    panel.setLayout(null);
    mensajero = new JTextArea(10, 20);
    mensajero.setLineWrap(true);
    mensajero.setWrapStyleWord(true);
    mensajero.setEditable(false);
    JScrollPane qScroller = new JScrollPane(mensajero);
    qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    mensajito = new JTextField(15);
    nC = new JTextField(10);
    JButton btnEnviar = new JButton("Env");
    btnEnviar.setBounds(250,185,50,45);

    txtMensaje= new TextPrompt("Escriba un mensaje", mensajito);
    txtMensaje.changeStyle(Font.ITALIC);

    txtReceptor= new TextPrompt("Receptor(ej:David)", nC);
    txtReceptor.changeStyle(Font.ITALIC);

    btnEnviar.addActionListener(new BtnEnviarListener());

    btnEnviar.setIcon(new ImageIcon(getClass().getResource("/Iconos/Enviar.png")));
    btnEnviar.setBackground(new Color(x,y,z));
    btnEnviar.setToolTipText("Enviar");
    btnEnviar.setBorder(null);
    mensajito.setBounds(50,200,190,18);


    JButton btnEliminar = new JButton("B");
    btnEliminar.addActionListener(new BtnEnviarListener());
    btnEliminar.setIcon(new ImageIcon(getClass().getResource("/Iconos/Eliminar.png")));

    btnEliminar.setBackground(new Color(x,y,z));
    btnEliminar.setToolTipText("Vaciar chat");
    btnEliminar.setBorder(null);

    btnEliminar.setBounds(150,255,50,40);

    panel.add(btnEliminar);
    panel.add(qScroller);
    panel.add(mensajito);
    panel.add(btnEnviar);
    panel.add(nC);
    panel.setBackground(new Color(x,y,z));
    frame.add(panel);
    preparaRed();
    mensajero.setBounds(30,20,200,200);
    nC.setBounds(50,230,190,18);
    qScroller.setBounds(30,20,250,150);


    Thread readerThread = new Thread(new LecturaMensajero());
    readerThread.start(); //este hilo inicia la conexion con el servidor

    frame.setSize(350, 350);
    frame.setVisible(true);
  }

  private void preparaRed(){// preparaRed crea el socket del cliente y lo envia
    try {
      sock = new Socket("127.0.0.1", 5000);
      InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
      lectura = new BufferedReader(streamReader); //bufferen para la lectura de mensajes
      escritura = new PrintWriter(sock.getOutputStream());
      System.out.println("red establecida"); //manda un mensaje de que se ha establecido la red
    }catch(IOException e){
      e.printStackTrace();
    }
  }//preparaRed

  public class BtnEnviarListener implements ActionListener{
    public void actionPerformed(ActionEvent eve){
      String e = eve.getActionCommand();
      if(e.equals("Env")){
        try{
          escritura.println(mensajito.getText());//- servidor -> mensaje
          escritura.println(nC.getText());
          escritura.flush();
          System.out.println("Si");// cuando se envia un mensaje se ayuda la funcion flush para que llegue al servidor

        }catch(Exception exception){
          exception.printStackTrace();
        }
        mensajito.setText(null);
        nC.setText(null); // limpia las casillas de donde se envia el mensaje y el receptor
      //mensajito.requestFocus();
      //nC.requestFocus();
      }
      else{
        mensajero.setText(""); //limpia el mensajero en caso de limpiar el chat
      }
    }
  }//btnEnviarListener

  class LecturaMensajero implements Runnable{
    public void run(){
      String mensaje;
      try {
        while((mensaje = lectura.readLine())!= null){
          System.out.println("lectura cliente: "+mensaje); //pone el mensaje que le llega
          mensajero.append(mensaje+"\n");
        }
      }catch(IOException e){
        e.printStackTrace(); // lanza una excepcion si no hay conexion
      }
    }
  }
  // public static void main(String[] args) {
  //   new Cliente().go();
  // }
}
