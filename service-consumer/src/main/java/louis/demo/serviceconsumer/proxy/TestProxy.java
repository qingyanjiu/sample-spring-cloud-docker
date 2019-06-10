package louis.demo.serviceconsumer.proxy;

import java.lang.reflect.Proxy;

public class TestProxy {

    public static void main(String[] args){
        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles","true");
        IPrinter printer = new Printer();
        PrinterProxy handler = new PrinterProxy(printer);
        IPrinter proxy = (IPrinter) Proxy.newProxyInstance(printer.getClass().getClassLoader(),printer.getClass().getInterfaces(),handler);
        proxy.print("test print!");
    }
}
