package com.software.jpetstore.service.impl;

import com.software.jpetstore.domain.Account;
import com.software.jpetstore.persistence.AccountMapper;
import com.software.jpetstore.persistence.CategoryMapper;
import com.software.jpetstore.persistence.ItemMapper;
import com.software.jpetstore.persistence.ProductMapper;
import com.software.jpetstore.service.AccountService;
import com.software.jpetstore.domain.Account;
import com.software.jpetstore.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Override
    public Account getAccount(String username) {
        return accountMapper.getAccountByUsername(username);
    }

    @Override
    public Account getAccount(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        return accountMapper.getAccountByUsernameAndPassword(account);
    }

    @Override
    public void insertAccount(Account account) {
        accountMapper.insertAccount(account);
        accountMapper.insertProfile(account);
        accountMapper.insertSignon(account);

    }

    @Override
    public void updateAccount(Account account) {
            accountMapper.updateAccount(account);
            accountMapper.updateProfile(account);

            if (account.getPassword() != null && account.getPassword().length() > 0) {
                accountMapper.updateSignon(account);
            }
        }
}
