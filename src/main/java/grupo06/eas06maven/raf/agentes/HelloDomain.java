package grupo06.eas06maven.raf.agentes;

import java.io.Serializable;
import javax.swing.*;
import grupo06.eas06maven.raf.principal.Ra;
import grupo06.eas06maven.raf.principal.RaAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Pops up a hello Window on every server in the domain when
 * a domain server is installed.
 */
public class HelloDomain extends Ra
{
    /**
     * List of all the servers in the domain.
     */
    private ArrayList<RaAddress> v;

    /**
     * Points to the next destination in v.
     */
    int i;

    /**
     * Just initialize the super class.
     *
     * @param name The name of the agent. This name has to be
     * unique. Normally the KaaribogaBase class provides some
     * method to generate a unique name.
     */
    public HelloDomain(String name){
        super("HelloDomain_" + name);
        this.description = "Agente que lanza un pop-up en broadcast a todos los agentes conectados";
    }

    /**
     * Initializes v with all servers connected to the domain.
     */
    @Override
    public void onCreate(){
        i = 0;
        v = new ArrayList<>();
        RaAddress address;
        Collection elements = agency.getServers(this).values();
        Iterator it = elements.iterator();
        while (it.hasNext()){
            address = (RaAddress) it.next();
            v.add(address);
        }
       
    }

    /**
     * Shows a window.
     */
    @Override
    public void onArrival(){
        new Popup().start();
    }

    /**
     * This is automically called if the agent arrives on
     * a base.
     */
    @Override
    public void run(){
        try{
            if (i < v.size()){
                destination = (RaAddress) v.get(i);
                ++i;
                System.out.println("Try to dispatch");
                fireDispatchRequest();
            }
            else fireDestroyRequest();
        }
        catch (ArrayIndexOutOfBoundsException e){
            System.err.println ("HelloDomain: Index out of bounds!");
            fireDestroyRequest();
        }
    }

    /**
     * Use a thread to let a window pop up.
     */
    public class Popup extends Thread implements Serializable{

        /**
         * Pop up window.
         */
        @Override
        public void run(){
            JOptionPane.showMessageDialog (null, "Hi there!");
        }
    }

}