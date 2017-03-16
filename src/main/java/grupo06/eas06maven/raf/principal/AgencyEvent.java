package grupo06.eas06maven.raf.principal;

/**
 * Los eventos que pueden ser lanzados por la agencia ra.
 * Esos eventos son lanzados cuando un agente Ra es añadido o
 * borrado de una agencia.
 *
 * @author RMN
 */
public class AgencyEvent extends java.util.EventObject
{
    /**
     * Nombre del agente que ha sido  añadido o borrado de la agencia.
     */
    private final RaInfo info;

    /**
     * Crea un nuevo evento de agencia.
     *
     * @param obj El objeto que creo el evento.
     * @param info La información de agente
     */
    public AgencyEvent(Object obj, RaInfo info){
        super(obj);
        this.info = info;
    }

    /**
     * Devuelve el nombre del agente que ha sido añadido o borrado de la agencia.
     * @return 
     */
    public RaInfo getInfo(){
        return info;
    }
}