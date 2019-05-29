package louis.demo.serviceconsumer;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ValueUpdateNotifier implements ValueUpdateObserverable{
    private List<ValueUpdateListener> valueUpdateListeners = new ArrayList();

    @Override
    public void addListener(ValueUpdateListener valueUpdateListener) {
        this.valueUpdateListeners.add(valueUpdateListener);
    }

    @Override
    public void removeListener(ValueUpdateListener valueUpdateListener) {
        this.valueUpdateListeners.remove(valueUpdateListener);
    }

    @Override
    public void notifyObserver(Map<String,String> updateValues) {
        for(int i = 0; i < valueUpdateListeners.size(); i++) {
            ValueUpdateListener valueUpdateListener = valueUpdateListeners.get(i);
            valueUpdateListener.update(updateValues);
        }
    }

//    @Scheduled(cron="0 */1 * * * ?")
    public void getAndUpdateNewValues() {
        Map<String,String> updateValues = new HashMap();
        updateValues.put("test.value","newnewnewnewn");
        notifyObserver(updateValues);
    }
}
