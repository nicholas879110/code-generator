#if(!${entityPackage})
package ${bussPackage}.openApi;
#else
package ${bussPackage}.controller.${entityPackage};
#end
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.rop.annotation.ServiceMethodBean;
#if(!${entityPackage})
import ${bussPackage}.facade.${className}Facade;
#else
import ${bussPackage}.entity.${entityPackage}.${className};
import ${bussPackage}.facade.${entityPackage}.${className}Facade;
#end
 

@ServiceMethodBean
public class ${className}OpenApi {
	
	private final static Logger logger = LoggerFactory.getLogger(${className}OpenApi.class);
	
	@Autowired
	private ${className}Facade ${lowerName}Facade; 
	


}
