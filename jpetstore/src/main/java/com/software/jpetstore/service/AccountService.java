package com.software.jpetstore.service;

import com.software.jpetstore.domain.Account;
import com.software.jpetstore.domain.Account;

public interface AccountService {
    Account getAccount(String username);
    Account getAccount(String username, String password);
    void insertAccount(Account account);
    void updateAccount(Account account);
}
