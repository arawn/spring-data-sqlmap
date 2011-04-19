package kr.arawn.springframework.data.sqlmap.repository.factory;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.springframework.data.repository.support.AbstractEntityInformation;
import org.springframework.data.repository.support.EntityInformation;
import org.springframework.util.ReflectionUtils;

public class SqlmapEntityInformation<T, ID extends Serializable> extends
        AbstractEntityInformation<T, ID> implements EntityInformation<T, ID> {

    private Field id;
    
    public SqlmapEntityInformation(Class<T> domainClass) {
        super(domainClass);
        
        this.id = ReflectionUtils.findField(getJavaType(), "id");
        if(this.id == null)
            throw new IllegalArgumentException("entity 에 id 가 없습니다.");
        
        ReflectionUtils.makeAccessible(this.id);
    }

    @SuppressWarnings("unchecked")
    public ID getId(T entity) {
        return (ID) ReflectionUtils.getField(id, entity);
    }
    
    public void setId(T entity, ID idValue) {
        ReflectionUtils.setField(id, entity, idValue);
    }

    @SuppressWarnings("unchecked")
    public Class<ID> getIdType() {
        return (Class<ID>) id.getType();
    }

}
