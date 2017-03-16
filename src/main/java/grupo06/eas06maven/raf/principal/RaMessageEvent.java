package grupo06.eas06maven.raf.principal;


public class RaMessageEvent extends java.util.EventObject
{
   
    private final RaMessage m;

    public RaMessageEvent(Object obj, RaMessage m){
        super(obj);
        this.m = m;
    }

   
    public RaMessage getMessage(){
        return m;
    }
}