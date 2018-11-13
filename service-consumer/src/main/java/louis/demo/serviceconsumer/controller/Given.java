package louis.demo.serviceconsumer.controller;

import java.lang.annotation.*;

@Documented
@Inherited
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Given {
    public String value() default "";
}
