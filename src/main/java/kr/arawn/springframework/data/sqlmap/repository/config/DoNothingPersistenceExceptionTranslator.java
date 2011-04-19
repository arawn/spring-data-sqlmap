package kr.arawn.springframework.data.sqlmap.repository.config;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;

public class DoNothingPersistenceExceptionTranslator implements PersistenceExceptionTranslator {
    
    public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
        throw ex;
    }

}
