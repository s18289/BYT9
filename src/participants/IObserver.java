package participants;

import java.util.Date;

public interface IObserver {
    void update(Date newLastModified);
    Date getLastModified();
}