package grupo06.eas06maven.raf.raservidor;

/**
 * Interfaz para eventos que pueden ser lanzados por el ramodel.
 * Esos eventos son lanzados cuando un servidor se conecta o deconecta del dominio
 *
 * @author RMN
 */
public interface RaModelListener extends java.util.EventListener{

    /**
     * Un servidor se ha conectado al dominio.
     * @param e
     */
    public void raModelAgencyOnline (RaModelEvent e);

    /**
     * Un servidor se ha desconectado del dominio.
     * @param e
     */
    public void raModelAgencyOffline (RaModelEvent e);

}
