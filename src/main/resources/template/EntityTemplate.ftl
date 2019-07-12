#if(${entityPackage})
package $!{entityPackage}.entity;
#end

import com.zlw.mine.commons.entity.BaseEntity;
import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;

/**
 * ${descn}
 * @Description
 * @Author ${author}
 * @Date ${date}
 * @Copyright(c) PwC.普华永道
 */
public class ${className} extends BaseEntity<${className}> {

	private static final long serialVersionUID = 1L;
	${feilds}
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("${className} [");
        #foreach($item in $!{columnDatas})
        #if($velocityCount == 1)
        builder.append("$!item.fieldName=");
        #else
        builder.append(", $!item.fieldName=");
        #end
        builder.append($!item.fieldName);
        #end
        builder.append("]");
        return builder.toString();
    }
}

