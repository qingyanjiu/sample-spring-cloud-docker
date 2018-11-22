package louis.demo.serviceconsumer;

import java.util.Map;

public class ContainAnnotationValueBean implements ValueUpdateListener {
    //包含value属性的bean的类
    private Class classWithValueAnnotation;
    //@Value注释的value，是个通配符，如"${xxx.xxx}"
    private String valueAnnotationValue;
    //@Value注释所在的成员变量名称
    private String valueAnnotationFieldName;
    //bean在spring容器中注册的名字
    private String valueAnnotationBeanName;

    public Class getClassWithValueAnnotation() {
        return classWithValueAnnotation;
    }

    public void setClassWithValueAnnotation(Class classWithValueAnnotation) {
        this.classWithValueAnnotation = classWithValueAnnotation;
    }

    public String getValueAnnotationValue() {
        return valueAnnotationValue;
    }

    public void setValueAnnotationValue(String valueAnnotationValue) {
        this.valueAnnotationValue = valueAnnotationValue;
    }

    public String getValueAnnotationFieldName() {
        return valueAnnotationFieldName;
    }

    public void setValueAnnotationFieldName(String valueAnnotationFieldName) {
        this.valueAnnotationFieldName = valueAnnotationFieldName;
    }

    public String getValueAnnotationBeanName() {
        return valueAnnotationBeanName;
    }

    public void setValueAnnotationBeanName(String valueAnnotationBeanName) {
        this.valueAnnotationBeanName = valueAnnotationBeanName;
    }

    @Override
    public void update(Map<String,String> updateValues) {
        if(updateValues != null) {
            for (String key : updateValues.keySet()) {
                String value = updateValues.get(key);
                if (valueAnnotationValue.equals("${" + key + "}")) {
                    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                    System.out.println("update bean value [ " + key + " ] of Class [ " + this.classWithValueAnnotation + " ]");
//                    System.out.println(", value from [ " + valueAnnotationValue + " ] to [ " + value + " ]");
                    System.out.println(", new value is [ " + value + " ]");
                    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                }
            }
        }
    }
}
