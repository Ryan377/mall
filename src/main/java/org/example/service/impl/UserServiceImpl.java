package org.example.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.example.dao.UserDoMapper;
import org.example.dao.UserPassMapper;
import org.example.dataobject.UserDo;
import org.example.dataobject.UserPass;
import org.example.error.BussinessException;
import org.example.error.EnumBussinessError;
import org.example.service.UserService;
import org.example.service.model.UserModel;
import org.example.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired(required = false)
    private UserDoMapper userDoMapper;

    @Autowired(required = false)
    private UserPassMapper userPassMapper;

    @Autowired
    private ValidatorImpl validator;

    @Override
    public UserModel getUserById(Integer id) {
        UserDo userDo = userDoMapper.selectByPrimaryKey(id);

        if (userDo == null) {
            return null;
        }

        UserPass userPass = userPassMapper.selectByUserId(userDo.getId());

        return convertFromDataObject(userDo, userPass);
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BussinessException {
        if (userModel == null) {
            throw new BussinessException(EnumBussinessError.PARAMETER_VALIDATION_ERROR);
        }
        if (StringUtils.isEmpty(userModel.getName())
            || userModel.getGender() == null
            || userModel.getAge() == null
            || StringUtils.isEmpty(userModel.getTelephone())) {
            throw new BussinessException(EnumBussinessError.PARAMETER_VALIDATION_ERROR);
        }

        UserDo userDo = convertFromModel(userModel);

        try {
            userDoMapper.insertSelective(userDo);
        } catch (DuplicateKeyException e) {
            throw new BussinessException(EnumBussinessError.PARAMETER_VALIDATION_ERROR, "手机号已重复注册");
        }
        userModel.setId(userDo.getId());

        UserPass userPass = convertPassFromModel(userModel);
        userPassMapper.insertSelective(userPass);

    }

    @Override
    public UserModel validateLogin(String telphone, String encrptPassword) throws BussinessException {
        UserDo userDo = userDoMapper.selectByTelephone(telphone);
        if(userDo == null){
            throw new BussinessException(EnumBussinessError.USER_LOGIN_FAIL);
        }
        UserPass userPasswordDO = userPassMapper.selectByUserId(userDo.getId());
        UserModel userModel = convertFromDataObject(userDo, userPasswordDO);
        //比对用户信息的密码与传输进来的密码是否匹配
        if(!StringUtils.equals(encrptPassword,userModel.getEncrptPassword())){
            throw new BussinessException(EnumBussinessError.USER_LOGIN_FAIL);
        }
        return userModel;
    }

    private UserPass convertPassFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserPass userPass = new UserPass();
        userPass.setEncrptPassword(userModel.getEncrptPassword());
        userPass.setUserId(userModel.getId());
        return userPass;
    }

    private UserDo convertFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserDo userDo = new UserDo();
        BeanUtils.copyProperties(userModel, userDo);
        return userDo;
    }

    private UserModel convertFromDataObject(UserDo userDo, UserPass userPass) {
        if (userDo == null) {
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDo, userModel);

        if (userPass != null) {
            userModel.setEncrptPassword(userPass.getEncrptPassword());
        }

        return userModel;
    }
}
