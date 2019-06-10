package louis.demo.serviceconsumer.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class PrinterProxy implements InvocationHandler {

    private Object target;

    public PrinterProxy(Object obj){
        this.target = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before proxy");
        Object result = method.invoke(target,args);
        System.out.println("after proxy");
        return result;
    }
}
