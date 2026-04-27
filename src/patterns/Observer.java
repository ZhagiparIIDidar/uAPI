package patterns;

/**
 * Observer interface for the Observer design pattern.
 * Implements the observer pattern to notify observers of changes in the system.
 */
public interface Observer {
    /**
     * Update method called when the observable object changes.
     *
     * @param event the event description
     */
    void update(String event);
}
