package grupo06.eas06maven.raf.agentes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import grupo06.eas06maven.raf.principal.Ra;
import grupo06.eas06maven.raf.principal.RaAddress;

public class Chat extends Ra {
    /**
     * List of all the servers in the domain.
     */
    private ArrayList<RaAddress> v;
    String s; // cadena de conversacion

    private boolean iniciado;
    
    private RaAddress chateador1;
    private RaAddress chateador2;
  
    public Chat(String name){
        super("Chat_" + name);
        this.description = "Agente que viaja entre dos agencias recogiendo las conversaciones de los usuarios";
        this.iniciado = false;
    }

    
    @Override
    public void onCreate(){
        System.out.println("Me cree");
        v = new ArrayList<>();
        RaAddress address;
        
        if (agency.getServers(this) == null){
            System.out.println("Muere CHat");
            JOptionPane.showMessageDialog (null, "Chat requiere 2 agencias");
            fireDestroyRequest();
            return;
        }
        
        Collection elements = agency.getServers(this).values();
        Iterator it = elements.iterator();
        while (it.hasNext()){
            address = (RaAddress) it.next();
            v.add(address);
        }
        
        if (v.size() != 2){
            System.out.println("Muere CHat");
            JOptionPane.showMessageDialog (null, "Chat requiere 2 agencias");
            fireDestroyRequest();
            return;
        }
        
        // Added
        this.chateador1 = v.get(0);
        this.chateador2 = v.get(1);
        this.destination = this.agency.getAgencyAddress();
        
        s = "Chat iniciado por "+this.agency.getIdentity()
        +"\n ------------------------------------------------------------------";
    }

    /**
     * Shows a window.
     */
    @Override
    public void onArrival(){
        // Muestra la ventan de chat
        System.out.println("Llegue");
      
        final TextComponent frame = new TextComponent();
        
        WindowListener l = new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
		frame.textPane.requestFocus();
            }
        };
        frame.addWindowListener(l);

        frame.pack();
        frame.setVisible(true);  
    }

    /**
     * This is automically called if the agent arrives on
     * a base.
     */
    @Override
    public void run(){
        
        if (!iniciado){
            iniciado=true;
            // La primera direccion sera quien inicio el chat
            // Esta direccion se inicializa en el onCreate del Chat
            System.out.println("Inicio este chat");
            // Se enviaron el agente a esa direccion
            fireDispatchRequest();
        }
        
    }
    /**
     * Re-envia el agente con el mensaje modificado
     * 
     */
    
    public void reSend(){
        changeDestination();
        fireDispatchRequest();
    }
    
    /**
     * Cambia el destino del agente
     * 
     */
    
    private void changeDestination(){
        System.out.println("-----------------------------------------");
        System.out.println("Destino antes: "+destination);
        System.out.println("Chateador 1: "+chateador1);
        System.out.println("Chateador 2: "+chateador2);
        if(this.destination.toString().equals(this.chateador1.toString())){
            this.destination = this.chateador2;
        }else{
            this.destination = this.chateador1;
        }
        System.out.println("Destino despues: "+destination);
        System.out.println("-----------------------------------------");
    }
    
    /**
     * Devuelve el usuario actual del chat
     * 
     * @return 
     */
    
    public RaAddress currentChateador(){
        return destination;
    }

   public class TextComponent extends JFrame implements java.awt.event.ActionListener{
        JTextArea textPane;
        JTextArea changeLog;
        String newline;

        JButton aceptar;
        public TextComponent () {
            //Some initial setup
            super("Chatero");
            System.out.println("Se creo ala pantalla chat");
            newline = System.getProperty("line.separator");

            //Create the text area and configure it
            //textPane = new JTextArea();
            textPane = new JTextArea(5, 40);

            textPane.setEditable(true);
            JScrollPane scrollPane = new JScrollPane(textPane);

            //Create the text area for the status log and configure it
            changeLog = new JTextArea(5, 30);
            changeLog.setText (s);
            changeLog.setEditable(false);
            JScrollPane scrollPaneForLog = new JScrollPane(changeLog);

            //Create a split pane for the change log and the text area
            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                                                  scrollPane, scrollPaneForLog);
            splitPane.setOneTouchExpandable(true);
            aceptar = new JButton ("Aceptar");
            aceptar.addActionListener(this);
            aceptar.setActionCommand("enable");
            //Create the status area
            JPanel statusPane = new JPanel(new GridLayout(1, 1));

            //Add the components to the frame 
            BorderLayout borderLayout = new BorderLayout();
            JPanel contentPane = new JPanel();
            contentPane.setLayout(borderLayout);
            contentPane.add(splitPane, BorderLayout.NORTH);
            contentPane.add(statusPane, BorderLayout.CENTER);
            contentPane.add(aceptar,JButton.CENTER);
            setContentPane(contentPane);
            setLocation();
        }

        @Override
        public void actionPerformed (java.awt.event.ActionEvent e){
            if (aceptar.getActionCommand().equals("enable")){
               textPane.selectAll();
               // Nuevo
               s = s + '\n' 
                    + currentChateador().port+" says : "+textPane.getSelectedText();
               System.out.println("Intentando disparar de vuelta");
               changeLog.setText(s);
               // Enviar el agente de vuelta
               reSend();
               // Evento para cerrar la ventana
               this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }
        }

        public void setLocation(){
            Dimension windowSize = getSize();
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Point centerPoint = ge.getCenterPoint();
            int dx = centerPoint.x + (windowSize.width / 2);
            int dy = centerPoint.y + (windowSize.height / 2);    
            setLocation(dx, dy);
        }
        
    }
}
