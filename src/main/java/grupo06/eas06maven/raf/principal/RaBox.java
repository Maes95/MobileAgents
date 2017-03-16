package grupo06.eas06maven.raf.principal;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RaBox
{
   
    public Ra ra;
      
    public Thread thread;

    public Date timeOfArrival;

    public InetAddress sendingHost;
 
    public RaBox(Ra ra, Thread thread,
                      Date timeOfArrival, InetAddress sendingHost){
        this.ra = ra;
        this.thread = thread;
        this.timeOfArrival = timeOfArrival;
        this.sendingHost = sendingHost; 
    }
    
    public RaInfo getRaInfo(){
        String destination;
        if(ra.getDestination() == null){
            destination = "Sin destino fijado";
        }else{
            destination = ra.getDestination().toString();
        }
        
        return new RaInfo(ra.getName(), 
            ra.getDescription(),
            ra.getCreatedDate(),
            ra.getCreator(),
            destination,
            new SimpleDateFormat("H:mm:ss:SSS").format(timeOfArrival));
    }
}