package grupo06.eas06maven.raf.principal;

import grupo06.eas06maven.raf.utils.EncryptionUtils;
import java.io.*;
import java.net.*;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.crypto.Cipher;
import org.apache.commons.lang.SerializationUtils;

public class RaAgency
    implements Serializable, RaListener, RaMessageListener
{
    /**
     * No usado.
     */
    public final int version = 0;

  
    private final Object parent;

  
    private HashMap<String, RaAddress> agencys;

    

    private RaAddress raServer;

  
    private final List<AgencyListener> agencyListeners;

   
    long delay = 100000;


    private int port;

    
    private RaAddress agencyAddress;

   
    private long counter = 0;

   
    private volatile Thread listenThread = null;

   
    protected ClassManager classManager;

   
    HashMap<String, RaBox> boxes;

    
    ServerSocket serverSocket = null;
    
    final PrivateKey privateKey;
    final PublicKey publicKey;

   
    class ReceiveMessageThread extends Thread implements Serializable{
        private final Socket socket;
        private final RaAgency agency;
        private RaMessage message;
        private RaMessage outMessage;
        private InetAddress address;
        private Ra agent;

       
        public ReceiveMessageThread(RaAgency b, Socket socket){
            this.socket = socket;
            this.agency = b;
        }

     
        @Override
        public void run(){
            ObjectOutputStream outStream = null;
            ObjectInputStream inStream = null;

            try{
                inStream = new ObjectInputStream(
                                new BufferedInputStream(
                                    socket.getInputStream()));
                outStream = new ObjectOutputStream(
                                    socket.getOutputStream());
                address = socket.getInetAddress();
            }
            catch (IOException e){
                System.err.println("! ReceiveMessageThread.run: " + e);
            }

            try{
                
                // AQUI LLEGAN LOS MENSAJE
                // DESENCRIPTAR
                //message = (RaMessage) SerializationUtils.deserialize(EncryptionUtils.decrypt((byte[]) inStream.readObject(), privateKey));
                
                byte[][] dataAndKey = (byte[][]) inStream.readObject();
                byte[] data = dataAndKey[0];
                byte[] key = dataAndKey[1];
                
                String decryptedKey = (String) SerializationUtils.deserialize(EncryptionUtils.decrypt(key, privateKey));
                
                message = (RaMessage) SerializationUtils.deserialize(EncryptionUtils.doCrypto(Cipher.DECRYPT_MODE, decryptedKey, data));
                
                if ( !message.recipient.host.equals(agencyAddress.host) ){
                    new SendMessageThread(message).start();
                }
                else
                if ( message.kind.equals("RA") ){
                    System.out.println ("ReceiveMessageThread: ha llegado un mensage RA.");
                    ByteArrayInputStream bInStream = new ByteArrayInputStream(message.binary);
                    RaInputStream mis = new RaInputStream(classManager, agencyAddress, bInStream, message.sender);
                    agent = (Ra) mis.readObject();
                    //agent.onArrival();
                    addRaOnArrival(agent, address);
                }
                else if ( message.kind.equals("AGENCYS") ){
                    System.out.println ("ReceiveMessageThread: ha llegado un mensaje AGENCYS.");
                    ByteArrayInputStream bis = new ByteArrayInputStream(message.binary);
                    ObjectInputStream ois = new ObjectInputStream (bis);
                    synchronized (this){
                        agencys = (HashMap<String, RaAddress>) ois.readObject();
                    }
                }
                else if ( message.kind.equals("GET_CLASS") ){
                    System.out.println ("ReceiveMessageThread: Ha llegado un mensaje GET_CLASS: " + message.content + ".class");
                    byte[] source = classManager.getByteCode (message.content);
                    outMessage = new RaMessage(agencyAddress,
                                                      message.sender,
                                                      "CLASS",
                                                       message.content,
                                                       source);
                    outStream.writeObject(outMessage);
                    outStream.flush();
                }
                else if (message.kind.equals("GET")){
                       RaBox target = (RaBox) boxes.get(message.content);
                    if (target != null){
                        target.ra.onDispatch();
                        target.thread = null;

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(bos);
                        oos.writeObject (target.ra);

                        outMessage = new RaMessage(agencyAddress,
                                                      message.sender,
                                                      "RA",
                                                       message.content,
                                                       bos.toByteArray());
                        outStream.writeObject(outMessage);
                        outStream.flush();


                        boxes.remove(message.content);
                        classManager.dec(message.content);
                    }
                    else {
            
                        outMessage = new RaMessage(agencyAddress,
                                                       message.sender,
                                                      "ERROR",
                                                       "Agente no encontrado!",
                                                       null);
                        outStream.writeObject(outMessage);
                        outStream.flush();
                    }
                }
                else if (message.recipient.name!=null) {

                    System.out.println ("Intentando devolver un mensaje al agente local" + message.recipient.name);
                    RaBox box = (RaBox) boxes.get(message.recipient.name);
                    if (box != null){
                        box.ra.handleMessage (message);
                    }
                }
                else {
                    
                    agency.handleMessage(message);
                }
	    }
            catch (IOException e){
                System.err.println("ReceiveMessageThread: IOException en la transferencia de datos!");
                System.err.println (e.getMessage());
            }
            catch (ClassNotFoundException e){
                System.err.println ("ReceiveMessageThread: ClassNotFoundException al recibir el objeto!");
                System.err.println (e.getMessage());
            }

            try{
                if (inStream != null) inStream.close();
                if (outStream != null) outStream.close();
                if (socket != null) socket.close();
            }
            catch (IOException e){

                System.err.println("ReceiveMessageThread: IOException en el limpiado!");
                System.err.println (e.getMessage());
            }
        }
    } // ReceiveMessageThread


    class ListenThread extends Thread implements Serializable
    {
        private final RaAgency parent;
        private Socket socket = null;

        public ListenThread (RaAgency parent){
            this.parent = parent;
        }

        @Override
        public void run(){
            Thread shouldLive = listenThread;
            try{
                while (shouldLive == listenThread){
                    socket = serverSocket.accept();
                    if(shouldLive != listenThread) return;
		    System.out.println ("ListenThread: tomando un mensaje");
                    new ReceiveMessageThread (parent, socket).start();
                    yield();
                }
            }
            catch (IOException e){
                System.err.println("! ListenThread.run: " + e);
            }
        }
    } // Listen Thread


    protected class SendMessageThread extends Thread implements Serializable{
        RaMessage msg;

        
        public SendMessageThread(RaMessage msg){
            this.msg = msg;
        }

       
        @Override
        public void run(){
            Socket socket = null;
            ObjectOutputStream outStream = null;

            try {
                // AQUI LO MANDA
                // ENCRIPTAR
                 //Mensaje encriptado
                //byte[] encrypted = EncryptionUtils.encrypt(SerializationUtils.serialize(msg), publicKey);
                
                //Mensaje encriptado
                Properties props = new Properties ();
                try (FileInputStream in = new FileInputStream ("src\\main\\java\\grupo06\\eas06maven\\raf\\config\\symmetric_pass.config")){
                    props.load (in);
                }
                catch (FileNotFoundException e){
                    System.err.println ("RaDomain: No se puede abrir el fichero de propiedades!");
                }
                catch (IOException e){
                    System.err.println ("RaDomain: Ha fallado la lectura del fichero!");
                }
                String symmetric_key = props.getProperty("pass");
                byte[] encryptedData = EncryptionUtils.doCrypto(Cipher.ENCRYPT_MODE, symmetric_key, SerializationUtils.serialize(msg));
                byte[] encryptedKey = EncryptionUtils.encrypt(SerializationUtils.serialize(symmetric_key), publicKey);
                
                System.out.println(encryptedData  + " - " + encryptedKey);
                
                //byte[] encrypted = EncryptionUtils.encrypt(SerializationUtils.serialize(msg), publicKey);
                
                byte[][] dataAndKey = new byte[encryptedData.length][encryptedKey.length];
                dataAndKey[0] = encryptedData;
                dataAndKey[1] = encryptedKey;
                
                socket = new Socket(msg.recipient.host, msg.recipient.port);
                System.out.println ("SendMessageThread: socket created to: " + msg.recipient.host + " " + msg.recipient.port);
                outStream = new ObjectOutputStream(
                                    socket.getOutputStream());
                outStream.writeObject (dataAndKey);
                outStream.flush();
                System.out.println ("SendMessageThread: Wrote message to socket.");
            }
            catch (IOException e){
                System.err.println("! SendMessageThread.run,1: " + e + ": " + msg.recipient);
            }

            try{
                try { sleep(10000); } catch(Exception e) {} 
                if (outStream != null) outStream.close();
                if (socket != null) socket.close();
            }
            catch (IOException e){

                System.err.println("! SendMessageThread.run,2: " + e);
            }
        }
    } // SendMessageThread


 
    public RaAgency (Object parent, ClassManager clManager){
        this.parent = parent;
        agencyListeners = new ArrayList();
        boxes = new HashMap<>();
        classManager = clManager;
        
        this.privateKey = EncryptionUtils.getPrivateKey();
        this.publicKey = EncryptionUtils.getPublicKey();
    }

   
    public void handleMessage (RaMessage msg){
        System.out.println ("Message version: " + msg.version);
        System.out.println ("Message kind: " + msg.kind);
        System.out.println ("Message content: " + msg.content);
    }

  
    protected void dispatch (Ra ra, RaAddress address){
        RaMessage msg;
        ra.onDispatch();
        RaBox box = (RaBox) boxes.get(ra.getName());
        if (box.thread.isAlive()) box.thread = null;

        try {
            RaAddress msgSender = new RaAddress(
                                         InetAddress.getLocalHost(),
                                         port, ra.getName());

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            RaOutputStream mos = new RaOutputStream(bos);
            mos.writeObject (ra);

            msg = new RaMessage(msgSender, address, "RA", "", bos.toByteArray());
            new SendMessageThread (msg).start();
        }
        catch (UnknownHostException e){
            System.err.println ("! RaAgency.dispatchRequest: " + e);
        }
        catch (IOException e){
            System.err.println ("! RaAgency.dispatchRequest: " + e );
        }

        ra.onDestroy();
        fireRaLeft (box.getRaInfo());
        boxes.remove (ra.getName());
        classManager.dec(ra.getName());
    }


    public void dispose(){
        if (listenThread != null) stopAgency(parent);
    }

    public synchronized void addRaOnArrival(Ra ra, InetAddress sender){
        ra.setAgency(this);
        ra.addRaListener(this);
        ra.addRaMessageListener(this);
        Thread thread = new Thread (ra);
        java.util.Date time = new java.util.Date();
        RaBox box = new RaBox(ra, thread, time, sender);
        boxes.put(ra.getName(), box);
        ra.onArrival();
        thread.start();
        fireRaArrived (box.getRaInfo());
    }

    
    public synchronized void addRaOnCreation(Ra ra, InetAddress sender){
        ra.setAgency(this);
        ra.addRaListener(this);
        ra.addRaMessageListener(this);
        Thread thread = new Thread (ra);
        java.util.Date time = new java.util.Date();
        RaBox box = new RaBox(ra, thread, time, sender);
        boxes.put(ra.getName(), box);
        ra.creator = this.getIdentity();
        ra.onCreate();
        thread.start();
        System.out.println("Nombre del que viene: "+ra.getName());
        fireRaCreated (box.getRaInfo());
    }

    @Override
    public void raDispatchRequest(RaEvent e){
        Ra ra = (Ra) e.getSource();
        RaAddress destination = ra.getDestination();
        System.out.println ("Destino: " + destination.host.toString());
        dispatch (ra, destination);
    }

    
    @Override
    public void raSleepRequest(RaEvent e){
        Ra ra = (Ra) e.getSource();
        ra.onSleep();
    }

  
    @Override
    public void raDestroyRequest(RaEvent e){
        Ra ra = (Ra) e.getSource();
        destroyRa(this, ra.getName());
    }

  
    public Set<String> getRaNames(Object sender){
        return boxes.keySet();
    }

    public String generateName(){
        String localHost;
        try{
            localHost = InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e){
            System.err.println ("! RaAgency.generateName: La agencia no puede determinar el host local!" + e);
            localHost = "Host desconocido";
        }
        return ++counter + " " + localHost + " " + new Date().toString();
    }

    
    public RaAddress getAgencyAddress(){
        try
        {
                agencyAddress.host = InetAddress.getLocalHost();
        }
        catch (UnknownHostException e){
                System.err.println ("! RaAgency.getAgencyAddress :" + e );
                System.exit(1);
        }
        return agencyAddress;
    }
    
    public String getAdress(){
        return agencyAddress.toString();
    }

    

    public void startAgency(Object sender, int portNo, RaAddress raServer){
        
        if (sender != parent) return;
        
        port = portNo;
        this.raServer = raServer;
        try{
             agencyAddress = new RaAddress(InetAddress.getLocalHost(), port, null);

             serverSocket = new ServerSocket(port);
             System.out.println ("Escuchando en el puerto: " + port);
        }
        catch (UnknownHostException e){
            System.err.println ("No se ha podido determinar la direccion del host local!");
            System.exit(1);
        }
        catch (IOException e){
            System.err.println ("! No se ha podido crear el ServerSocket!" + e);
            System.exit(1);
        }
        listenThread = new ListenThread(this);
        listenThread.start();

        if (raServer != null){
            RaMessage msg = new RaMessage (agencyAddress,
                                                         raServer,
                                                         "AGENCY_ONLINE",
                                                         null,
                                                         null);
            new SendMessageThread (msg).start();
        }
    }

    public void stopAgency (Object sender){
        if (sender != parent) return;


        if (raServer != null){
            RaMessage msg = new RaMessage (agencyAddress,
                                                         raServer,
                                                         "AGENCY_OFFLINE",
                                                         null,
                                                         null);
            try {
                Thread thread = new SendMessageThread (msg);
                thread.start();
                thread.join();
            }
            catch (InterruptedException e){
                System.err.println ("! RaAgency: La desconexion del raServer ha fallado!" + e);
            }
        }

        listenThread = null;
        try{
             serverSocket.close();
             serverSocket = null;
             System.out.println ("Server socket cerrado");
        }
        catch (IOException e){
            System.err.println ("! No se puede cerrar el ServerSocket " + e);
	    System.exit(1);
        }
    }

    public void destroyRa (Object sender, String name){
        RaBox box = (RaBox) boxes.get(name);
        if (box != null){
            box.ra.onDestroy();
            fireRaDestroyed (box.getRaInfo());
            boxes.remove(name);
            System.out.println("Se destruyo el agente actual");
        }
    }

    public void dispatchRa (Object sender, String name, RaAddress destination){
        RaBox box = (RaBox) boxes.get(name);
        if (box != null){
            System.out.println ("Destino: " + destination.host.toString());
            if(destination != null) box.ra.destination = destination;
            dispatch (box.ra, destination);
        }else{
            System.out.println("No se mando el agente (Agente no entonctrado)");
        }
    }

    public HashMap<String, RaAddress> getServers (Object sender){
        HashMap<String, RaAddress> result = null;
        synchronized (this){
            if (agencys != null) result = (HashMap<String, RaAddress>) agencys.clone();
        }
        return result;
    }

    public synchronized void addAgencyListener (AgencyListener l){
        if (agencyListeners.contains(l)) return;

        agencyListeners.add(l);
    }


    public synchronized void removeAgencyListener (AgencyListener l){
        agencyListeners.remove(l);
    }

   
    protected void fireRaCreated (RaInfo info){
        List<AgencyListener> listeners;
        synchronized (this){
            listeners = cloneAgencyListeners();
        }
        int size = listeners.size();

        if (size == 0) return;

        AgencyEvent e = new AgencyEvent (this, info);
        for (int i = 0; i < size; ++i) {
            ( (AgencyListener) agencyListeners.get(i) ).agencyRaCreated(e);
        }
    }

   
    protected void fireRaArrived (RaInfo info){
        List<AgencyListener> listeners;
        synchronized (this){
            listeners = cloneAgencyListeners();
        }
        int size = listeners.size();

        if (size == 0) return;

        AgencyEvent e = new AgencyEvent (this, info);
        for (int i = 0; i < size; ++i) {
            ( (AgencyListener) agencyListeners.get(i) ).agencyRaArrived(e);
        }
    }

   

    protected void fireRaDestroyed (RaInfo info){
        List<AgencyListener> listeners;
        synchronized (this){
            listeners = cloneAgencyListeners();
        }
        int size = listeners.size();

        if (size == 0) return;

        AgencyEvent e = new AgencyEvent (this, info);
        for (int i = 0; i < size; ++i) {
            ( (AgencyListener) agencyListeners.get(i) ).agencyRaDestroyed(e);
        }
    }

   
    protected void fireRaLeft (RaInfo info){
        List<AgencyListener> listeners;
        synchronized (this){
            listeners = cloneAgencyListeners();
        }
        int size = listeners.size();

        if (size == 0) return;

        AgencyEvent e = new AgencyEvent (this, info);
        for (int i = 0; i < size; ++i) {
            ( (AgencyListener) agencyListeners.get(i) ).agencyRaLeft(e);
        }
    }

    @Override
    public void raMessage (RaMessageEvent e){
        RaMessage message = e.getMessage();
        Ra receiver;

        if (message != null)
	{
            if (message.recipient != null)
	    {
                if (message.recipient.host == null)
		{ // localhost
		    RaBox box = null;
	            if (message.recipient.name != null) 
		    	box = (RaBox) boxes.get (message.recipient.name); 
                    if( box != null )
		    {
		    	receiver = (Ra) box.ra;
                    	if (receiver != null) receiver.handleMessage (message); 
			
		    }
		    else System.err.println("No es un agente local " + message.recipient.name);
                }
                else {
                    new SendMessageThread(message).start();
                }
            }
        }
    } 
    
    public String getIdentity(){
        return this.agencyAddress.toString();
    }
    
    /**
     * Método clone sustituyente del clone de Vector
     * 
     * @return copia de los listeners
     */
    
    private List<AgencyListener> cloneAgencyListeners(){
        AgencyListener[] clonedData = 
                Arrays.copyOf(agencyListeners.toArray(new AgencyListener[agencyListeners.size()]), agencyListeners.size());
        List<AgencyListener> listeners = Arrays.asList(clonedData);
        return listeners;
    }

}
