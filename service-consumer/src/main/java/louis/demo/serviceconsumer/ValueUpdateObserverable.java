package louis.demo.serviceconsumer;

import java.util.Map;

public interface ValueUpdateObserverable {
    void addListener(ValueUpdateListener valueUpdateListener);
    void removeListener(ValueUpdateListener valueUpdateListener);
    void notifyObserver(Map<String,String> updateValues);
}
