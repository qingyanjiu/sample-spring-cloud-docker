package louis.demo.serviceconsumer.proxy;

public class Printer implements IPrinter {
    @Override
    public void print(String message) {
        System.out.println("******* print--"+message+"********");
    }
}
