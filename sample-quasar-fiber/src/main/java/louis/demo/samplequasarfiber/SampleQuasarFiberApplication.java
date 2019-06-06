package louis.demo.samplequasarfiber;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.SuspendableCallable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ExecutionException;

@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@RestController
public class SampleQuasarFiberApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleQuasarFiberApplication.class, args);
    }


    @RequestMapping("test")
    public List test(@RequestParam(value = "siteUrl", required = true)String siteUrl) {

        Fiber<List> list1 = new Fiber("list1", new SuspendableCallable() {
            @Override
            public Object run() throws SuspendExecution, InterruptedException {
                List list = new ArrayList();
                list.add("list1");
                return list;
            }
        });

        Fiber<List> list2 = new Fiber("list2", new SuspendableCallable() {
            @Override
            public Object run() throws SuspendExecution, InterruptedException {
                List list = new ArrayList();
                list.add("list2");
                return list;
            }
        });

        Fiber<List> list3 = new Fiber("list3", new SuspendableCallable() {
            @Override
            public Object run() throws SuspendExecution, InterruptedException {
                List list = new ArrayList();
                list.add("list3");
                return list;
            }
        });

        Fiber<List> list4 = new Fiber("list4", new SuspendableCallable() {
            @Override
            public Object run() throws SuspendExecution, InterruptedException {
                List list = new ArrayList();
                list.add("list4");
                return list;
            }
        });

        Fiber<List> list5 = new Fiber("list5", new SuspendableCallable() {
            @Override
            public Object run() throws SuspendExecution, InterruptedException {
                List list = new ArrayList();
                list.add("list5");
                return list;
            }
        });

        List restult = new ArrayList();

        try {
            list1.start();
            list2.start();
            list3.start();
            list4.start();
            list5.start();

            restult.addAll(list1.get());
            restult.addAll(list2.get());
            restult.addAll(list3.get());
            restult.addAll(list4.get());
            restult.addAll(list5.get());

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return restult;
    }

}
