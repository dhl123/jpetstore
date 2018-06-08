package com.software.jpetstore.service.impl;

import com.software.jpetstore.persistence.LogMapper;
import com.software.jpetstore.service.LogService;
import org.springframework.stereotype.Service;
@Service
public class LogServiceImpl implements LogService{
    private LogMapper logMapper;
    public LogServiceImpl(LogMapper logMapper) {
        this.logMapper = logMapper;
    }
    public void insertLog(String username,String url,String cartitem){
        logMapper.insertLog(username,url,cartitem);
    }
}
