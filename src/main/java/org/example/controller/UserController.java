package org.example.controller;

import org.apache.tomcat.util.security.MD5Encoder;
import org.example.controller.viewobject.UserVO;
import org.example.error.BussinessException;
import org.example.error.EnumBussinessError;
import org.example.response.CommonReturnType;
import org.example.service.UserService;
import org.example.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.util.Random;

@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired(required = false)
    private HttpServletRequest httpServletRequest;

    @RequestMapping(value = "/register", method = {RequestMethod.POST}, consumes = {CONSUME_TYPE})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name="telephone")String telephone,
                                     @RequestParam(name="otpCode")String otpCode,
                                     @RequestParam(name="name")String name,
                                     @RequestParam(name="gender")Integer gender,
                                     @RequestParam(name="age")Integer age,
                                     @RequestParam(name="password")String password) throws BussinessException, UnsupportedEncodingException, NoSuchAlgorithmException {

        //todo: otp验证码校验
//        String inSessionotpCode = (String) this.httpServletRequest.getSession().getAttribute(telephone);
//        if (!com.alibaba.druid.util.StringUtils.equals(otpCode, inSessionotpCode)) {
//            throw new BussinessException(EnumBussinessError.PARAMETER_VALIDATION_ERROR, "短信验证码不符合");
//        }

        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setGender(Byte.valueOf(String.valueOf(gender.intValue())));
        userModel.setAge(age);
        userModel.setTelephone(telephone);
        userModel.setRegisterMode("byphone");
        userModel.setEncrptPassword(this.EncodeByMD5(password));

        userService.register(userModel);
        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "/login",method = {RequestMethod.POST},consumes = {CONSUME_TYPE})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name = "telephone")String telephone,
                                  @RequestParam(name = "password")String password) throws BussinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        //入参校验
        if(org.apache.commons.lang3.StringUtils.isEmpty(telephone)|| org.apache.commons.lang3.StringUtils.isEmpty(password))
            throw new BussinessException(EnumBussinessError.PARAMETER_VALIDATION_ERROR);
        UserModel userModel=userService.validateLogin(telephone,this.EncodeByMD5(password));
        //没有任何异常，则加入到用户登录成功的session内。这里不用分布式的处理方式。
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);
        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "/getotp", method = {RequestMethod.POST}, consumes = {CONSUME_TYPE})
    @ResponseBody
    public CommonReturnType getotp(@RequestParam(name="telephone")String telephone) {
        //生成otp验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String otpCode = String.valueOf(randomInt);

        //和手机号关联,使用httpSession
        httpServletRequest.getSession().setAttribute(telephone, otpCode);

        //发送短信，省略
        System.out.println("telephone= " + telephone + " otpcode= " + otpCode);
        return CommonReturnType.create(null);
    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id") Integer id) throws BussinessException {
        //调用service层服务获取对应id的用户对象并返回前端
        UserModel userModel = userService.getUserById(id);


        if (userModel == null) {
            throw new BussinessException(EnumBussinessError.USER_NOT_EXIST);
        }

        //将核心领域模型用户对象转换为可供ui使用的viewobject
        UserVO userVO = convertFromModel(userModel);
        return CommonReturnType.create(userVO);
    }

    private UserVO convertFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }

    private String EncodeByMD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(md5.digest(str.getBytes("utf-8")));
    }


}
