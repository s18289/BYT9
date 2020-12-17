package tests.memento;

import tests.common.TestUtils;
import memento.WebMonitorMemento;
import memento.WebMonitorState;
import org.junit.Assert;
import org.junit.Test;
import java.io.*;
import java.util.HashMap;

public class WebMonitorMementoTest extends TestUtils {

    @Test
    public void setMementoTest() {
        WebMonitorState beforeState = monitor.getMemento().getState();
        Assert.assertNotEquals(0, beforeState.getLastModifiedDates().size());
        Assert.assertNotEquals(0, beforeState.getObservers().size());

        WebMonitorState newState = new WebMonitorState(new HashMap<>(), new HashMap<>());
        monitor.setMemento(new WebMonitorMemento(newState));
        Assert.assertEquals(0, monitor.getMemento().getState().getLastModifiedDates().size());
        Assert.assertEquals(0, monitor.getMemento().getState().getObservers().size());
    }

    @Test
    public void saveStateTest() throws Exception {
        File file = new File("./serialized/state.ser");
        ObjectOutputStream outStream = new ObjectOutputStream(new FileOutputStream(file));
        WebMonitorState oldState = monitor.getMemento().getState();
        outStream.writeObject(oldState);
        outStream.close();

        ObjectInputStream inStream = new ObjectInputStream(new FileInputStream(file));
        WebMonitorState newState = (WebMonitorState) inStream.readObject();

        Assert.assertEquals(oldState.getObservers().size(), newState.getObservers().size());
        Assert.assertEquals(oldState.getLastModifiedDates().size(), newState.getLastModifiedDates().size());
    }
}