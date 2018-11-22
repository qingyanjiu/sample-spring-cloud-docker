package louis.demo.serviceconsumer;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        //保存所有含有@Value注释的对象ContainAnnotationValue列表的bean
        BeanDefinition annotationProcessor = BeanDefinitionBuilder.genericBeanDefinition(ContainAnnotationValueList.class).getBeanDefinition();
        //自动更新配置，并进行通知的bean
        BeanDefinition valueUpdateNotifierDefinition = BeanDefinitionBuilder.genericBeanDefinition(ValueUpdateNotifier.class).getBeanDefinition();
        beanDefinitionRegistry.registerBeanDefinition("containAnnotationValueList", annotationProcessor);
        beanDefinitionRegistry.registerBeanDefinition("valueUpdateNotifier", valueUpdateNotifierDefinition);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        System.out.println("***********************************************");
        System.out.println(configurableListableBeanFactory.getBeanDefinition("containAnnotationValueList"));
        System.out.println(configurableListableBeanFactory.getBeanDefinition("valueUpdateNotifier"));
    }
}
