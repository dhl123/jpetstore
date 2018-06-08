package com.software.jpetstore.persistence;

import org.springframework.stereotype.Repository;

@Repository
public interface LogMapper {
    void insertLog(String username,String url,String cartitem);
}
