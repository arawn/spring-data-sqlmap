package kr.arawn.springframework.data.sqlmap.repository.sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public class SpringSproutCondition implements Pageable {
    
    private int pageNumber;
    private int pageSize;
    private String name;
    private String nickName;
    private Sort sort;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getPageNumber() {
        return pageNumber;
    }
    
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
    
    public int getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
    public int getOffset() {
        return (pageNumber-1) * pageSize;
    }
    
    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public Sort getSort() {
        return sort;
    }
    
    public List<Order> getOrders() {
        if(sort == null || sort.iterator() == null)
            return Collections.emptyList();
        
        ArrayList<Sort.Order> orders = new ArrayList<Sort.Order>();
        Iterator<Sort.Order> iterable = sort.iterator();
        while(iterable.hasNext()) {
            orders.add(iterable.next());
        }
        
        return orders;
    }

}
