package grupo06.eas06maven.raf.raservidor;

import grupo06.eas06maven.raf.principal.RaAddress;

/**
 * Eventos that son lanzados por el ramodel para notifcar a los listeners si
 * los servidores se conectan o desconectan desde el dominio.
 */
public class RaModelEvent extends java.util.EventObject{

    /**
     * Direccion de la agencia que se desconecta o desconecta del dominio.
     */
    RaAddress agency;

    /**
     * @param sender Object que lanza el evento.
     * @param agency El servidor de agentes que se ha conectado o desconectado del dominio.
     */
    public RaModelEvent (Object sender, RaAddress agency){
        super (sender);
        this.agency = agency;
   }

    /**
     * Devuelve la direccion del servidor que se ha conectado o deconectado del dominio.
     * @return 
     */
    public RaAddress getAgency(){
        return agency;
    }

}