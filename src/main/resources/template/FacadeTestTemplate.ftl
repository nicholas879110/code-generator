import com.zlw.mine.common.entity.ResultData;
#if(${entityPackage})
import ${entityPackage}.entity.${className};
#else
import com.zlw.mine.platform.${moduleName}.entity.${className};
#end
#if(${entityPackage})
import ${entityPackage}.facade.${className}Facade;
#else
import com.zlw.mine.platform.${moduleName}.facade.${className}Facade;
#end
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * ${descn}
 * @Description
 * @Author ${author}
 * @Date ${date}
 * @Copyright(c) PwC.普华永道
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-context.xml")
public class ${className}Test {

    @Autowired
    private ${className}Facade ${lowerName}Facade;

}
