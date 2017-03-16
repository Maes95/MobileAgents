package grupo06.eas06maven.raf.agentes;

import java.util.Collection;
import java.util.Iterator;
import grupo06.eas06maven.raf.principal.Ra;
import grupo06.eas06maven.raf.principal.RaAddress;

/**
 * Utility agent that prints out a list of all servers connected to the domain.
 * Note how easy it is to extend an existing program with agents.
 * Future versions of kaariboga will probably contain special agents that
 * are automatically integrated into the menu structure.
 */
public class ServerLister extends Ra
{
    /**
     * Just initialize the super class.
     *
     * @param name The name of the agent. This name has to be
     * unique. Normally the KaaribogaBase class provides some
     * method to generate a unique name.
     */
    public ServerLister(String name){
        super("ServerLister_" + name);
        this.description = "Muestra por consola la lista de todos los servidores disponibles en el sistema";
    }

    /**
     * Prints out the names of all servers connected to the domain.
     */
    @Override
    public void run(){
        RaAddress address;
        Collection elements = agency.getServers(this).values();
        Iterator it = elements.iterator();

        System.out.println("---------------------------------------------");
        System.out.println("Servers connected to the domain:");
        while (it.hasNext()){
            address = (RaAddress) it.next();
            System.out.println (address.host.toString() + ":" + Integer.toString(address.port));
        }
        System.out.println("---------------------------------------------");
        fireDestroyRequest();
    }

}
