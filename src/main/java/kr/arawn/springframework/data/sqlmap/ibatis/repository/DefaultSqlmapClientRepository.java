package kr.arawn.springframework.data.sqlmap.ibatis.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kr.arawn.springframework.data.sqlmap.repository.SqlmapRepository;
import kr.arawn.springframework.data.sqlmap.repository.factory.SqlmapEntityInformation;
import kr.arawn.springframework.data.sqlmap.repository.statement.StatementInformation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;


@Repository
public class DefaultSqlmapClientRepository<T, ID extends Serializable> implements
        SqlmapRepository<T, Serializable> {

    private SqlMapClientTemplate sqlMapClientTemplate;
    private SqlmapEntityInformation<T, ID> entityInformation;
    private StatementInformation statement;
    
    public DefaultSqlmapClientRepository(SqlMapClientTemplate sqlMapClientTemplate, 
            SqlmapEntityInformation<T, ID> entityInformation, StatementInformation statement) {
        Assert.notNull(sqlMapClientTemplate);
        Assert.notNull(entityInformation);
        
        this.sqlMapClientTemplate = sqlMapClientTemplate;
        this.entityInformation = entityInformation;
        this.statement = statement;
    }
    
    protected SqlMapClientTemplate getSqlMapClientTemplate() {
        return sqlMapClientTemplate;
    }

    public T save(T entity) {
        if(entityInformation.isNew(entity)) {
            @SuppressWarnings("unchecked")
            ID id = (ID) getSqlMapClientTemplate().insert(statement.insert(), entity);
            if(id != null)
                entityInformation.setId(entity, id);
        }
        else
            getSqlMapClientTemplate().update(statement.update(), entity);
        
        return entity;
    }

    @SuppressWarnings("unchecked")
    public List<T> save(List<? extends T> entities) {
        for(T entity : entities) {
            save(entity);
        }
        
        return (List<T>) entities;
    }
    
    @SuppressWarnings("unchecked")
    public Iterable<T> save(Iterable<? extends T> entities) {
        for(T entity : entities) {
            save(entity);
        }
        
        return (Iterable<T>) entities;
    }

    public void delete(T entity) {
        getSqlMapClientTemplate().delete(statement.delete(), entity);
    }

    public void delete(List<? extends T> entities) {
        for(T entity : entities) {
            delete(entity);
        }
    }
    
    public void delete(Iterable<? extends T> entities) {
        for(T entity : entities) {
            delete(entity);
        }
    }    

    public void deleteAll() {
        getSqlMapClientTemplate().delete(statement.deleteAll());
    }
    
    @SuppressWarnings("unchecked")
    public T findOne(Serializable id) {
        return (T) getSqlMapClientTemplate().queryForObject(statement.findOne(), id);
    }

    public boolean exists(Serializable id) {
        return getSqlMapClientTemplate().queryForObject(statement.findOne(), id) == null ? false : true;
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return getSqlMapClientTemplate().queryForList(statement.findAll());
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll(final Sort sort) {
        ArrayList<Sort.Order> orders = new ArrayList<Sort.Order>();
        Iterator<Sort.Order> iterable = sort.iterator();
        while(iterable.hasNext()) {
            orders.add(iterable.next());
        }
        
        Map<String, ArrayList<Sort.Order>> params = new HashMap<String, ArrayList<Order>>();
        params.put("orders", orders);
        
        return getSqlMapClientTemplate().queryForList(statement.findAll_Sort(), params);
    }

    @SuppressWarnings("unchecked")
    public Page<T> findAll(Pageable pageable) {
        List<T> results = getSqlMapClientTemplate().queryForList(statement.findAll_Pageable(), pageable);
        return new PageImpl<T>(results, pageable, results.size());
    }
    
    public Long count() {
        return (Long) getSqlMapClientTemplate().queryForObject(statement.count());
    }

}
