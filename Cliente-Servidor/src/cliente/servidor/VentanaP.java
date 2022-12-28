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

public class VentanaP extends JFrame{
  private JPanel p1;
  private JButton btnCliente;
  private JLabel jlServidor;
  private ArrayList<PrintWriter> clienteOS;
  private int cont = 0;
  private String nombreCliente;
  public static Dialogo pideNombre;
  private ArrayList<String> clientes= new ArrayList<String>();

  public VentanaP(){

    //new Servidor().go();
    setTitle("Modelo Cliente - Servidor");
    setSize(500, 250);
    setLayout(new BorderLayout());
    initComponents();
    this.setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);

  }

  public void initComponents(){
    p1 = new JPanel();
    p1.setBackground(new Color(247, 220, 111));
    p1.setLayout(null);

    btnCliente = new JButton("Nuevo");
    btnCliente.setToolTipText("Nuevo Cliente");
    btnCliente.setBounds(260, 30, 150, 150);
    btnCliente.setBackground(new Color(247, 220, 111));
    btnCliente.setIcon(new ImageIcon(getClass().getResource("/Iconos/Cliente.png")));
    btnCliente.setBorder(null);

    jlServidor = new JLabel("BS");
    jlServidor.setBounds(40,30,150,150);
    jlServidor.setToolTipText("Esta ventana es el servidor (No la cierre mientras envia mensajes)");
    jlServidor.setIcon(new ImageIcon(getClass().getResource("/Iconos/Server.png")));

    EventoBotones ess = new EventoBotones();
    btnCliente.addActionListener(ess);
    //btnServidor.addActionListener(ess);

    p1.add(btnCliente);
    p1.add(jlServidor);
    add(p1);
  }//initComponents

  public class EventoBotones implements ActionListener{
    public void actionPerformed(ActionEvent ev){
      String accion =ev.getActionCommand();
      switch (accion){
        case "Nuevo":
          pideNombre = new Dialogo(new JFrame(), true, "Nombre del cliente", "Nombre");
          pideNombre.setVisible(true);
          String nombre = pideNombre.getNombre();
          nombreCliente = nombre;
          System.out.println("NOMBRE PUEBA "+nombreCliente);
          break;
        case "Aceptar":
          System.out.println("ok");
          break;
      }
    }
  }//Action listener
  //ManejaClientes establece la conexion con los clientes
  public class ManejaClientes implements Runnable{
    BufferedReader lectura; //le los mensajes que le llegan 
    Socket sock; //socket de conexión del cliente

    public ManejaClientes(Socket clienteSock){
      System.out.println("ManejaClient");
      try{
        sock = clienteSock;
        InputStreamReader esLectura = new InputStreamReader(sock.getInputStream());
        lectura = new BufferedReader(esLectura);
      }catch( Exception e){
        e.printStackTrace();
      }
    }//constructor

    public void run(){

      String mensaje;
      String receptor="";
      try{
        //while (mensaje! = null && receptor != null)
        while((mensaje = lectura.readLine())!= null && (receptor = lectura.readLine())!=null){
          // hola como te va->/null
          //mensaje  =
          System.out.println("leer: " +mensaje); //cliente -> mensajito  hola
          System.out.println("receptor: "+ receptor); // cliente -> nc  5 receptor  = 5
//lee los mensajes y avisa a las personas indicadas segun el receptor y quien manda le mensaje
          avisaATodos(mensaje, receptor);

        }
      }catch(Exception e){
        e.printStackTrace();
      }
    }//run
  }//ManejaClientes


  public void go(){ 
    //Crea un arraylis de los clientes que van entrando
    clienteOS = new ArrayList<PrintWriter>();


    try{
      ServerSocket servS = new ServerSocket(5000);
      while(true){//ejecutandose siempre
        Socket clienteS = servS.accept();
        PrintWriter escribir = new PrintWriter(clienteS.getOutputStream());
        clienteOS.add(escribir);
        //crea un nuevo hilo para cada cliente 
        Thread t = new Thread(new ManejaClientes(clienteS));
        cont++;
        //llama el nombre que se le da en la clase dialogo y se lo pone al hilo, ademas
        //que guarda en un arraylist el nombre de cada cliente
        System.out.println(pideNombre.getNombre());
        String nuevoC = pideNombre.getNombre();
        clientes.add(nuevoC);
        t.setName(nuevoC);

        t.start(); //inicia el hilo del cliente
        System.out.println("Conexion");
      }
    }catch(Exception e){
      e.printStackTrace();
    }

  }

  public void avisaATodos(String mensaje, String receptor){
    System.out.println("Avisar");
    //si el cliente no le da un receptor al mensaje que quiere enviar se la za un mensaje de error
    if(receptor.equals("")){
      JOptionPane.showMessageDialog(null, "Error", "Indique un cliente", JOptionPane.ERROR_MESSAGE);
     //de lo contrario se manda a la persona que se encvio obteniendo la posicion 
     //del socket en el arraylist para escribirlo al receptor correspondiente
     //tambien se verifica que el receptor exista
    }else{
      int r =-1;
      for (int i = 0; i<clientes.size(); i++){
        String c = clientes.get(i);
        if(receptor.equalsIgnoreCase(c)){
          System.out.println("Cliente existe");
          r = i;

          break;
        }
      }

      if(r <0){
        JOptionPane.showMessageDialog(null, "Error", "Ese cliente no existe", JOptionPane.ERROR_MESSAGE);
      }else{
        try{

          String c2= Thread.currentThread().getName();
          int c = clientes.indexOf(c2);

          clienteOS.get(c).println("tu:"+mensaje);//mensaje para quien escribe
          clienteOS.get(c).flush();

          clienteOS.get(r).println(Thread.currentThread().getName()+":"+mensaje);
          clienteOS.get(r).flush();//mensaje para quien recibe
        }catch(Exception e){
          e.printStackTrace();
        }
      }
    }
  }//avisaATodos
}
