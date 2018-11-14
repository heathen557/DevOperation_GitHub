package onenet.DevOperation.log;

//import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import onenet.DevOperation.constant.OperateModule;
import onenet.DevOperation.constant.OperateType;
import onenet.DevOperation.dao.AttrDao;
import onenet.DevOperation.entity.DevAttr;

import onenet.DevOperation.entity.OperationLog;
import onenet.DevOperation.entity.Orgnization;
import onenet.DevOperation.sqlservice.OperateLogService;

import onenet.DevOperation.utils.ByteConvertUtils;

import javax.annotation.Resource;
import javax.persistence.Id;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangmy on 2017/7/25.
 */
@Aspect
@Component
public class WebLogAspect {

	@Resource
	private OperateLogService operateLogService;
	@Resource
	AttrDao attrDao;
	// @Autowired
	// private AdminUserService adminUserService;
	//
	// @Value("${jwt.header}")
	// private String tokenHeader;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 获取class的主键字段名 主键值
	 */
	public static Object getPrimaryKeyName(Class<?> clazz) throws Exception {
		Object param = null;
		// 递归获取父子类所有的field
		Class tempClass = clazz;
		// 当父类为null的时候说明到达了最上层的父类(Object类
		while (tempClass != null && !StringUtils.equals(tempClass.getName().toLowerCase(), "java.lang.object")) {
			Field[] fields = tempClass.getDeclaredFields();
			for (Field field : fields) {
				String fieldName = field.getName();
				// boolean类型不必判断，因实体里包含boolean类型的属性，getter方法是以is开头，不是get开头
				if (field.getType().equals(Boolean.class) || field.getType().getName().equals("boolean")) {
					continue;
				}
				if ((field.getModifiers() & Modifier.FINAL) == Modifier.FINAL) {
					continue;
				}
				String getterMethod = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				Method method = tempClass.getDeclaredMethod(getterMethod);

				// 字段上是否存在@Id注解
				Id primaryAnnotation = field.getAnnotation(Id.class);
				// getter方法上是否存在@Id注解
				if (primaryAnnotation == null) {
					primaryAnnotation = method.getAnnotation(Id.class);
				}
				// 存在@Id注解，则说明该字段为主键
				if (primaryAnnotation != null) {
					/* String primaryKeyName = field.getName(); */
					param = field.getName();
					break;
				}
			}
			if (param != null && StringUtils.isNotBlank(param.toString())) {
				break;
			}
			// 得到父类赋值给tempClass
			tempClass = tempClass.getSuperclass();
		}
		if (param == null) {
			// throw new ServiceException(clazz.getName() + "实体，未设置主键");
		}
		return param;
	}

	/**
	 * 获取@controllerLog 注解上信息
	 *
	 * @param joinPoint
	 * @return map
	 * @throws Exception
	 */
	public static Map<String, Object> getControllerAnnotationValue(JoinPoint joinPoint) throws Exception {
		String targetName = joinPoint.getTarget().getClass().getName();
		String methodName = joinPoint.getSignature().getName();
		Object[] arguments = joinPoint.getArgs();
		Class targetClass = Class.forName(targetName);
		Method[] methods = targetClass.getMethods();
		Map<String, Object> map = new HashMap<>();
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				Class[] classes = method.getParameterTypes();
				if (classes.length == arguments.length) {
					// 取入参数据
					String description = method.getAnnotation(MyLog.class).description();
					int module = method.getAnnotation(MyLog.class).module().ordinal();
					int opType = method.getAnnotation(MyLog.class).opType().ordinal();
					String primaryKeyName = method.getAnnotation(MyLog.class).primaryKeyName();
					int primaryKeySort = method.getAnnotation(MyLog.class).primaryKeySort();
					Class<?> clazz = method.getAnnotation(MyLog.class).primaryKeyBelongClass();
					map.put("module", module);
					map.put("opType", opType);
					map.put("description", description);
					map.put("primaryKeyName", primaryKeyName);
					map.put("primaryKeySort", primaryKeySort);
					map.put("primaryKeyBelongClass", clazz);
					break;
				}
			}
		}
		return map;
	}

	/**
	 * 定义一个切入点. ("execution(public * com.kfit.*.web..*.*(..))") 解释下： 第一个 *
	 * 代表任意修饰符及任意返回值. 第二个 * 任意包名 第三个 * 代表任意方法. 第四个 * 定义在web包或者子包 第五个 * 任意方法 ..
	 * 匹配任意数量的参数.
	 */
	// @Pointcut("execution(public * onenet.DevOperation.sqlservice.*(..)) &&
	// @annotation(onenet.DevOperation.log.MyLog)")
	// public void webLog() {
	// }

	@AfterReturning(returning = "var", pointcut = "execution(public * onenet.DevOperation.sqlservice.*.*(..))&& @annotation(onenet.DevOperation.log.MyLog)")
	public void doafterReturning(JoinPoint joinPoint, Object var) throws Throwable {

		logger.info("成功返回通知开始........");

		// 获取注解入参
		Map<String, Object> values = getControllerAnnotationValue(joinPoint);
		int opType = Integer.parseInt(values.get("opType").toString());
		int module = Integer.parseInt(values.get("module").toString());
		String business = values.get("description").toString();
		String primaryKeyName = values.get("primaryKeyName").toString();
		int primaryKeySort = Integer.parseInt(values.get("primaryKeySort").toString());
		// Class<?> primaryKeyBelongClass = (Class<?>)
		// values.get("primaryKeyBelongClass");

		// 入参 value
		Object[] args = joinPoint.getArgs();
		// 入参名称
		String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
		Map<String, Object> params = new HashMap<>();
		// 获取所有参数对象
		for (int i = 0; i < args.length; i++) {
			if (null != args[i]) {

				params.put(paramNames[i], args[i]);

			} else {
				params.put(paramNames[i], "无");
			}
		}

		Object primaryKeyValue = null;
		if (StringUtils.isNotBlank(primaryKeyName)) {
			primaryKeyValue = args[primaryKeySort];
		}
		Map<String, Object> content = new HashMap<>();
		// 对操作内容进行拼接
		if (module == OperateModule.DevparaSetting.ordinal() || module == OperateModule.AppUpdating.ordinal()) {
			if (opType == OperateType.modify.ordinal()) {
				if (var != null) {
					content.put("object", (String) var);
					content.put("opType", opType);
					content.put("value", JSON.toJSONString(params));
				}
			}
			if (opType == OperateType.load.ordinal()) {
				if (var != null) {
					content.put("object", (String) var); // -1\0
					content.put("opType", opType);
					content.put("value", JSON.toJSONString(params));
				}
			}
		}

		if (module == OperateModule.OrgSetting.ordinal()) {
			if (primaryKeyName.equals("attrid")) {
				if (opType == OperateType.delete.ordinal()) {

					DevAttr attr = (DevAttr) var;
					content.put("object", attr.getDevpostion() + ":" + attr.getImei());

				}
				if (opType == OperateType.create.ordinal()) {

					content.put("object", ((DevAttr) var).getDevpostion() + ":" + ((DevAttr) var).getImei());

				}
				if (opType == OperateType.modify.ordinal()) {
					content.put("object", params.get(args[3]) + ":" + params.get(args[4]));

				}
				content.put("opType", values.get("opType").toString());
				content.put("value", JSON.toJSONString(params));
			}
			if (primaryKeyName.equals("rowid")) {
				if (opType == OperateType.create.ordinal()) {

					content.put("object", ((Orgnization) var).getNodename());

				}
				if (opType == OperateType.delete.ordinal()) {

					// Orgnization attr =(Orgnization)var;
					content.put("object", ((Orgnization) var).getNodename());

				}
				if (opType == OperateType.modify.ordinal()) {

					// Orgnization attr =(Orgnization)var;
					content.put("object", ((Orgnization) var).getNodename());

				}
				content.put("opType", values.get("opType").toString());
			}

		}
		if (module == OperateModule.SysSettings.ordinal()) {

			// content.put("object", params.get(args[3])+":"+params.get(args[4]));
			content.put("opType", values.get("opType").toString());
			content.put("value", JSON.toJSONString(params));

		}
		OperationLog operateLog = new OperationLog(module, JSON.toJSONString(content), "admin",
				ByteConvertUtils.sysFormatStrTime(), business);

//		System.out.println("操作日志的实体已经构成  = " + operateLog);
		
		operateLogService.saveOperateLog(operateLog);    //  11-14

		/*
		 * } catch (Throwable throwable) { throwable.printStackTrace();
		 * logger.error("日志切面异常", throwable.getMessage());
		 * 
		 * }
		 */

	}

	// @AfterReturning("webLog()")
	// public void doAfterReturning(JoinPoint joinPoint) {
	// // 处理完请求，返回内容
	// logger.info("WebLogAspect.doAfterReturning()");
	// }

}
