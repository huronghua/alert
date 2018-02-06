package com.banmatrip.alert.dao;

import com.banmatrip.alert.domain.AdminAccount;

import java.util.List;

public interface AdminAccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AdminAccount record);

    int insertSelective(AdminAccount record);

    AdminAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdminAccount record);

    int updateByPrimaryKey(AdminAccount record);

    List<AdminAccount> getContactList();

    Integer selectUserIdByName(String name);
}