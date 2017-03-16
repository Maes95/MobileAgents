package grupo06.eas06maven.raf.principal;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.SecureClassLoader;




public class RaClassLoader extends SecureClassLoader{

   
    ClassManager classManager;


    RaAddress agency;

   
    RaAddress sourceHost;

    
    public RaClassLoader(ClassManager clManager, RaAddress agency, RaAddress sourceHost){
        this.sourceHost = sourceHost;
        this.agency = agency;
        this.classManager = clManager;
    }


 
    protected byte loadClassData(String name)[]
    throws ClassNotFoundException{
        byte result[] = null;
        Socket socket;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        System.out.println("RaClassLoader.loadClassData() ha sido llamado por: " + name);

        try{
            socket = new Socket(sourceHost.host, sourceHost.port);

            System.out.println("el cargador ha establecido la conexión");

            try (ObjectOutputStream outStream = new ObjectOutputStream(
                    socket.getOutputStream()); ObjectInputStream inStream = new ObjectInputStream(
                            new BufferedInputStream(
                                    socket.getInputStream()))) {
                
                RaMessage message = new RaMessage(agency,
                        sourceHost,
                        "GET_CLASS",
                        name, null);
                
                outStream.writeObject(message);
                outStream.flush();
                
                RaMessage inMessage = (RaMessage) inStream.readObject();
                result = inMessage.binary;
                
            }
            socket.close();
        }
        catch (UnknownHostException e) {
            System.err.println("Funcion enviar: Host desconocido!");
        }
        catch (IOException e){
            System.err.println ("Funcion enviar: IOException!");
        }
        return result;
    }

   
    @Override
    public Class findClass(String name)
    throws ClassNotFoundException {
        
        name = name.split(".java")[0];
        System.out.println("Ha sido llamado findClass en RaClassLoader!! " + name);

        /**
         *  Nuevo ClassLoader
         *  - Implementamos nuestro propio ClassLoader (el basico de java)
         *  - Desventaja: No hay caché de clases ya cargadas
         * 
         */
        
        ClassLoader classLoader = RaClassLoader.class.getClassLoader();
        Class c = classLoader.loadClass("grupo06.eas06maven."+name);
        //Class c = classLoader.loadClass(name);
        System.out.println("Nombre de la clase = " + c.getName());
        return c;
    }

}
