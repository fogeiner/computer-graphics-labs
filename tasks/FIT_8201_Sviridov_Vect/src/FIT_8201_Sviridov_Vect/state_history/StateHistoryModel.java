package FIT_8201_Sviridov_Vect.state_history;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic class to encapsulate history of state changes 
 *
 * @author admin
 */
final public class StateHistoryModel<T> {

    private List<T> states = new ArrayList<T>();
    private int index;
    private List<StateHistoryListener> listeners = new ArrayList<StateHistoryListener>();

    /**
     * Defaukt constructor
     */
    public StateHistoryModel() {
        clear();
    }

    /**
     * Add listner
     * @param l new listener
     */
    public void addListener(StateHistoryListener l) {
        listeners.add(l);
    }

    /**
     * Remove listner
     * @param l listener to remove
     */
    public void removeListener(StateHistoryListener l) {
        listeners.remove(l);
    }

    /**
     * Adds new state to the history
     * @param t state
     */
    public void add(T t) {
        int size = states.size();
        if (index < size - 1) {
            states.subList(index + 1, size).clear();
        }
        states.add(t);
        index++;
        for (StateHistoryListener l : listeners) {
            l.historyStateChanged();
        }
    }

    /**
     * Returns current state
     * @return current state
     */
    public T current() {
        int size = states.size();
        if (index < 0 || index > size - 1) {
            return null;
        }
        return states.get(index);
    }

    /**
     * Returns next state
     * @return next state
     */
    public T next() {
        int size = states.size();
        if (index == size - 1) {
            return null;
        }
        index++;


        for (StateHistoryListener l : listeners) {
            l.historyStateChanged();
        }

        return current();
    }

    /**
     * Returns previous state
     * @return previous state
     */
    public T prev() {
        if (index == 0) {
            return null;
        }
        index--;


        for (StateHistoryListener l : listeners) {
            l.historyStateChanged();
        }

        return current();
    }

    /**
     * Returns true if there is next state
     * @return true of there is next state
     */
    public boolean hasNext() {
        return index != states.size() - 1;
    }

    /**
     * Returns true if there is previous state
     * @return true of there is previous state
     */
    public boolean hasPrev() {
        return index != 0;
    }

    /**
     * Clears states history
     */
    public void clear() {
        states.clear();
        index = -1;

        for (StateHistoryListener l : listeners) {
            l.historyStateChanged();
        }
    }

    /**
     * Removes all listeners
     */
    public void clearListeners() {
        listeners.clear();
    }
}
