package kr.arawn.springframework.data.sqlmap.repository.statement;

import java.util.Map;

public class StatementInformation {
    
    public static enum StatementIdType {
        
        insert,
        update,
        delete,
        deleteAll,
        findOne,
        findAll,
        findAll_Sort,
        findAll_Pageable,
        count

    }
    
    private Map<String, String> statementMap;
    
    public StatementInformation(Map<String, String> statementMap) {
        this.statementMap = statementMap;
    }
    
    public String insert() {
        return getStatementId(StatementIdType.insert.name());
    }
    
    public String update() {
        return getStatementId(StatementIdType.update.name());
    }
    
    public String delete() {
        return getStatementId(StatementIdType.delete.name());
    }
    
    public String deleteAll() {
        return getStatementId(StatementIdType.deleteAll.name());
    }
    
    public String findOne() {
        return getStatementId(StatementIdType.findOne.name());
    }
    
    public String findAll() {
        return getStatementId(StatementIdType.findAll.name());
    }
    
    public String findAll_Sort() {
        return getStatementId(StatementIdType.findAll_Sort.name());
    }
    
    public String findAll_Pageable() {
        return getStatementId(StatementIdType.findAll_Pageable.name());
    }
    
    public String count() {
        return getStatementId(StatementIdType.count.name());
    }
    
    public String getStatementId(String methodName) {
        if(!statementMap.containsKey(methodName))
            throw new IllegalArgumentException(methodName + " 으로 정의된 statement-id 가 없습니다.");
        
        return statementMap.get(methodName);
    }

}
