package com.zengg.miaosha.service;

import com.zengg.miaosha.dao.UserDao;
import com.zengg.miaosha.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: miaosha
 * @description: 用户相关过程类
 * @author: ZengGuangfu
 * @create 2019-02-11 10:19
 */

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User getUserById(int id){
        User user = userDao.getById(id);
        return user;
    }

    @Transactional
    public boolean tx() {
        User user1 = new User();
        user1.setId(2);
        user1.setName("name2");
        userDao.insert(user1);

        User user2 = new User();
        user2.setId(1);
        user2.setName("name1");
        userDao.insert(user2);


        return true;
    }
}
