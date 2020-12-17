package memento;

public class WebMonitorMemento implements IMemento {

    private WebMonitorState _state;

    public WebMonitorMemento(WebMonitorState state) {
        this._state = state;
    }

    @Override
    public WebMonitorState getState() {
        return _state;
    }

    @Override
    public void setState(WebMonitorState state) {
        _state = state;
    }
}
