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
public class ClienteServidor {

    /**
     * @param args the command line arguments
     */
  public static void main(String[] args) {
    //new Servidor().go();
    //clase principal que inicia la VentanaP la cual se comporta como servidor
    VentanaP vp = new VentanaP();
    //Cliente c = new Cliente();
    vp.go();
  }

    
}
