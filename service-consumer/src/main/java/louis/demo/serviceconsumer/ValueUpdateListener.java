package louis.demo.serviceconsumer;

import java.util.Map;

public interface ValueUpdateListener {
    void update(Map<String,String> updateValues);
}
