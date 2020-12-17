package participants;

import java.io.Serializable;
import java.util.Date;

public class WebPageObserver implements IObserver, Serializable {

    private String id;
    private Date lastModified;

    public WebPageObserver(String id) {
        this.id = id;
    }

    @Override
    public void update(Date newLastModified) {
        lastModified = newLastModified;
        System.out.printf("observer #%-3s| new date: %25s%n", id, lastModified.toString());
    }

    @Override
    public Date getLastModified() {
        return lastModified;
    }
}