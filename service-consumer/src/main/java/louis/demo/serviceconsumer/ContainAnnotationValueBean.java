package louis.demo.serviceconsumer;

public class ContainAnnotationValueBean {
    private Class classWithValueAnnotation;
    private String valueAnnotaionValue;
    private String valueAnnotationFieldName;

    public Class getClassWithValueAnnotation() {
        return classWithValueAnnotation;
    }

    public void setClassWithValueAnnotation(Class classWithValueAnnotation) {
        this.classWithValueAnnotation = classWithValueAnnotation;
    }

    public String getValueAnnotaionValue() {
        return valueAnnotaionValue;
    }

    public void setValueAnnotaionValue(String valueAnnotaionValue) {
        this.valueAnnotaionValue = valueAnnotaionValue;
    }

    public String getValueAnnotationFieldName() {
        return valueAnnotationFieldName;
    }

    public void setValueAnnotationFieldName(String valueAnnotationFieldName) {
        this.valueAnnotationFieldName = valueAnnotationFieldName;
    }
}
