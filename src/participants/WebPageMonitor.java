package participants;

import exceptions.WebPageMonitorException;
import memento.WebMonitorMemento;
import memento.WebMonitorState;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class WebPageMonitor {

    private Map<URL, Date> _lastModifiedDates;
    private Map<URL, Set<IObserver>> _observers;

    {
        _lastModifiedDates = new HashMap<>();
        _observers = new HashMap<>();
    }

    /**
     * Regularly updates information about the modification dates
     *
     * @param updatePeriod period between updating
     */
    public void monitor(int updatePeriod) {
        while (true) {
            try {
                Thread.sleep(updatePeriod);
                _lastModifiedDates.keySet().forEach(this::updateLastModified);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    /**
     * Updates last modified date for the passed url and notifies attached observers
     *
     * @param url url for which last modified date will be updated
     */
    private void updateLastModified(URL url) {
        Date currLastModified = null;
        try {
            currLastModified = getLastModified(url);
        } catch (IOException e) {
            throw new WebPageMonitorException("couldn't load last updated date", e);
        }
        if (!_lastModifiedDates.get(url).equals(currLastModified)) {
            _lastModifiedDates.put(url, currLastModified);
            notifyObservers(url, currLastModified);
        }
    }

    /**
     * @param url url for which last modified date will be checked
     * @return date when the page under passed url was modified
     */
    private Date getLastModified(URL url) throws IOException {
        return new Date(url.openConnection().getLastModified());
    }

    /**
     * @param url             url observers of which will be notified
     * @param newLastModified new date of last modification that will be passed to observers
     */
    private void notifyObservers(URL url, Date newLastModified) {
        _observers.get(url).forEach(o -> o.update(newLastModified));
    }

    /**
     * Adds the url to the set of monitored url-s
     *
     * @param url url of the page which will be added to the set of monitored
     */
    public void addMonitoredPage(URL url) {
        try {
            _lastModifiedDates.putIfAbsent(url, getLastModified(url));
            _observers.putIfAbsent(url, new HashSet<IObserver>());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes the url from the set of monitored url-s
     *
     * @param url url of the page which will be removed from the set of monitored
     */
    public void removeMonitoredPage(URL url) {
        _lastModifiedDates.remove(url);
        _observers.remove(url);
    }

    /**
     * Attaches the observer to the url and updates date in observer.
     * Adds url to the monitored in case it is not.
     *
     * @param url      url to which the observer will be attached
     * @param observer observer that will be attached to the url
     */
    public void addObserver(URL url, IObserver observer) {
        addMonitoredPage(url);
        _observers.get(url).add(observer);
        observer.update(_lastModifiedDates.get(url));
    }

    /**
     * @param url      url from which the observer will be dettached
     * @param observer observer that will be detached from the url
     */
    public void removeObserver(URL url, IObserver observer) {
        if (_observers.containsKey(url)) {
            _observers.get(url).remove(observer);
        }
    }

    public WebMonitorMemento getMemento() {
        return new WebMonitorMemento(new WebMonitorState(_lastModifiedDates, _observers));
    }

    public void setMemento(WebMonitorMemento memento) {
        _observers = memento.getState().getObservers();
        _lastModifiedDates = memento.getState().getLastModifiedDates();
    }
}