package louis.demo.serviceconsumer;

import louis.demo.serviceconsumer.controller.Given;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Component
public class MyBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    public MyBeanPostProcessor(){
        System.out.println("=================I'm MyBeanPostProcessor================");
    }

    private ApplicationContext applicationContext;

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

            //获取含有@Value注解的成员变量
            Annotation valueAnnotation = fields[i].getAnnotation(Value.class);
            if(valueAnnotation!=null){
                ContainAnnotationValueBean containAnnotationValueBean =  new ContainAnnotationValueBean();
                containAnnotationValueBean.setClassWithValueAnnotation(bean.getClass());
                containAnnotationValueBean.setValueAnnotationFieldName(fields[i].getName());
                containAnnotationValueBean.setValueAnnotationBeanName(beanName);
                String value = ((Value)valueAnnotation).value();
                if(value != null) {
                    containAnnotationValueBean.setValueAnnotationValue(value);
                }
                //获取已经手动注册的更新配置，并进行通知的bean
                ValueUpdateNotifier valueUpdateNotifier = (ValueUpdateNotifier) applicationContext.getBean("valueUpdateNotifier");
                //添加listener到通知bean
                valueUpdateNotifier.addListener(containAnnotationValueBean);
                //获取保存所有含有@Value注释的对象ContainAnnotationValue列表的bean
                ContainAnnotationValueList containAnnotationValueList = (ContainAnnotationValueList) applicationContext.getBean("containAnnotationValueList");
                //将包含@Value注解的信息存入bean中
                containAnnotationValueList.addContainAnnotationValueBean(containAnnotationValueBean);
            }
        }
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
