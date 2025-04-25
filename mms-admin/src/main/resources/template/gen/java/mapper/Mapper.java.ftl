package ${package}.${moduleName}.mapper;

import com.sxpcwlkj.datasource.mapper.BaseMapperPlus;
import ${package}.${moduleName}.entity.${ClassName};
import ${package}.${moduleName}.entity.vo.${ClassName}Vo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* ${tableComment}-Mapper
*
* @author ${author}
* @Doc ${website}
*/
@Mapper
@Repository
public interface ${ClassName}Mapper extends BaseMapperPlus<${ClassName}, ${ClassName}Vo> {

}
