package onenet.DevOperation.log;


import java.lang.annotation.*;
import onenet.DevOperation.constant.*;

@Target(ElementType.METHOD) //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented //生成文档
public @interface MyLog {
	
    String description() default "";
    
    OperateModule module();
    
    OperateType opType();
    
    String primaryKeyName() default "";
    
    int primaryKeySort() default 0;
    
    Class<?> primaryKeyBelongClass() ;
}

