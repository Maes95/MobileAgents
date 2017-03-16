package grupo06.eas06maven.raf.principal;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;


public final class RaAddress implements Serializable{
    
   
    public InetAddress host;
    
   
    public int port;

    
    public String name;
    
   
    public RaAddress(InetAddress host, int port, String name){
        this.host = host;
        this.port = port;
        if (name == null){
            this.name = host.toString() + ":" + port;
        }else{
            this.name = name;
        }
        
    }
    
    
    public RaAddress(String hostname, int port, String name) throws UnknownHostException{
        host = InetAddress.getByName (hostname);
        this.port = port;
        this.name = name;
    }    

  
    public RaAddress (String name) {
        host = null;
        this.port = 0;
        this.name = name;
    }    
    
 
    @Override
    public String toString() {
    	if(host!=null) return host.toString() + ":" + port;
	return "local:"+ name;
     }    
    
}
