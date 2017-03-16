package grupo06.eas06maven.raf.principal;


/**
 * Interfaz para eventos que pueden ser lanzados desde la agencia.
 *
 * @author RMN
 */
public interface AgencyListener extends java.util.EventListener{

    /**
     * Reaccion cuando un agente ra ha sido puesto en la agencia y
     * la agncia invoca el metodo onCreate.
     * @param e
     */
    public void agencyRaCreated (AgencyEvent e);

    /**
     * Reaccion cuando ha llegado un agente a la agencia o
     * ha sido añadido en creacion.
     * @param e
     */
    public void agencyRaArrived (AgencyEvent e);

    /**
     * Reaccion cuando un agente deja la agencia.
     * @param e
     */
    public void agencyRaLeft (AgencyEvent e);

    /**
     * Reaccion cuando un agente ha sido eliminado.
     * @param e
     */
    public void agencyRaDestroyed (AgencyEvent e);

}