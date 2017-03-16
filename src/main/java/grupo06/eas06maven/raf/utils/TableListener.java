package grupo06.eas06maven.raf.utils;


/**
 * Interfaz para eventos que pueden ser lanzados desde una tabla.
 *
 * @author Michel
 */
public interface TableListener extends java.util.EventListener{

    /**
     * Reaccion cuando un agente ha sido seleccionado de una tabla
     * @param e
     */
    public void raSelectedChange(TableEvent e);
    
    public class TableEvent extends java.util.EventObject{
        
        private final String selectedRowValue;
    
        public TableEvent(Object source, String selectedRowValue) {
            super(source);
            this.selectedRowValue = selectedRowValue;
        }
        
        public String getValue(){
            return this.selectedRowValue;
        }
    
    }

}