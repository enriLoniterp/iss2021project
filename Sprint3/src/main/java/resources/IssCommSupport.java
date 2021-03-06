/**
 IssCommSupport.java
 ==========================================================================

 ==========================================================================
 */
package resources;
import it.unibo.interaction.IssObserver;
import it.unibo.interaction.IssOperations;

public interface IssCommSupport extends IssOperations {
    void registerObserver( IssObserver obs );
    void removeObserver( IssObserver obs );
    void close();
}
