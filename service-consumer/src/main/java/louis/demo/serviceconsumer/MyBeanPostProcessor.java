package louis.demo.serviceconsumer;

import louis.demo.serviceconsumer.controller.Given;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    public MyBeanPostProcessor(){
        System.out.println("=================I'm MyBeanPostProcessor================");
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName){
        Field[] fields = bean.getClass().getDeclaredFields();
        for(int i=0;i<fields.length;i++){
            Annotation annotation = fields[i].getAnnotation(Given.class);
            if(annotation!=null){
                try {
                    String value = ((Given)annotation).value();
                    if(value != null) {
                        fields[i].setAccessible(true);
                        fields[i].set(bean, value);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}
