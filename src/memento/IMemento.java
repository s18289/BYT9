package memento;

public interface IMemento {
    WebMonitorState getState();
    void setState(WebMonitorState state);
}
