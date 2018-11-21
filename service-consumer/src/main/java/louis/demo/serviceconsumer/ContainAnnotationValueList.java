package louis.demo.serviceconsumer;

import java.util.ArrayList;
import java.util.List;

public class ContainAnnotationValueList {
    private List<ContainAnnotationValueBean> containAnnotationValueBeans = new ArrayList();

    public List<ContainAnnotationValueBean> getContainAnnotationValueBeans() {
        return containAnnotationValueBeans;
    }

    private void setContainAnnotationValueBeans(List<ContainAnnotationValueBean> containAnnotationValueBeans) {
        this.containAnnotationValueBeans = containAnnotationValueBeans;
    }

    public void addContainAnnotationValueBean(ContainAnnotationValueBean containAnnotationValueBean) {
        this.containAnnotationValueBeans.add(containAnnotationValueBean);
    }
}
