package FIT_8201_Sviridov_Vect.state_history;

import FIT_8201_Sviridov_Vect.utils.Region;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author admin
 */
final public class StateHistoryModel<T> {

    private List<T> states = new ArrayList<T>();
    private int index;
    private List<StateHistoryListener> listeners = new ArrayList<StateHistoryListener>();

    public StateHistoryModel() {
        clear();
    }

    public void addListener(StateHistoryListener l) {
        listeners.add(l);
    }

    public void removeListener(StateHistoryListener l) {
        listeners.remove(l);
    }

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

    public T current() {
        int size = states.size();
        if (index < 0 || index > size - 1) {
            return null;
        }
        return states.get(index);
    }

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

    public boolean hasNext() {
        return index != states.size() - 1;
    }

    public boolean hasPrev() {
        return index != 0;
    }

    public void clear() {
        states.clear();
        index = -1;

        for (StateHistoryListener l : listeners) {
            l.historyStateChanged();
        }
    }

    public void clearListeners() {
        listeners.clear();
    }
}
