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
        BeanDefinition annotationProcessor = BeanDefinitionBuilder.genericBeanDefinition(ContainAnnotationValueList.class).getBeanDefinition();
        beanDefinitionRegistry.registerBeanDefinition("containAnnotationValueList", annotationProcessor);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        System.out.println("***********************************************");
        System.out.println(configurableListableBeanFactory.getBeanDefinition("containAnnotationValueList"));
    }
}
