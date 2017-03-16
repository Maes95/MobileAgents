package grupo06.eas06maven.raf.principal;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Michel
 * 
 * Captura la información de un Agente en un determinado instante de tiempo
 * 
 */
public class RaInfo {
    
    public final String raName;
    public final String raFullName;
    public final String raDescription;
    
    public final String createdAt;
    public final String creator;
    public final String destination;
    
    public final String arrivalDate;
    
    public String state;
    public String infoDate;
    
    public RaInfo(String raName, String raDescription,
            String createdAt,String creator,String destination,String arrivalDate){
        this.raName = raName.split(" ")[0];
        this.raFullName = raName;
        this.raDescription = raDescription;  
        this.createdAt = createdAt;
        this.creator = creator;
        this.destination= destination;
        this.arrivalDate= arrivalDate;
        this.state = " Cargado";
        this.infoDate = new SimpleDateFormat("H:mm:ss:SSS").format(new Date());
    }
    
    public RaInfo (String raName, String raDescription,String creator){
        this(raName,raDescription,"No se ha creado aún",creator,"Sin destino", "No fue enviado aun"); 
    }
    
}
