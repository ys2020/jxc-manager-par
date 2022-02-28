package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzj.admin.pojo.User;
import com.lzj.admin.mapper.UserMapper;
import com.lzj.admin.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.utils.AssertUtil;
import com.lzj.admin.utils.StringUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 老李
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    PasswordEncoder passwordEncoder;
    @Override
    public User login(String userName, String password) {
        AssertUtil.isTrue(StringUtil.isEmpty(userName),"用户名不能为空!");
        AssertUtil.isTrue(StringUtil.isEmpty(password),"密码不能为空!");
        User user = this.findUserByUserName(userName);
        AssertUtil.isTrue(null == user,"该用户记录不存在或已注销!");
        /**
         * 后续引入SpringSecurity 使用框架处理密码
         */
        AssertUtil.isTrue(!(user.getPassword().equals(password)),"密码错误!");
        return user;
    }

    @Override
    public User findUserByUserName(String userName) {
        return this.baseMapper.selectOne(new QueryWrapper<User>().eq("is_del",0).eq("user_name",userName));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateUserInfo(User user) {
        /**
         * 用户名
         *    非空
         *    唯一
         */
        AssertUtil.isTrue(StringUtil.isEmpty(user.getUsername()),"用户名不能为空!");
        User temp = this.findUserByUserName(user.getUsername());
        AssertUtil.isTrue(null !=temp && !(temp.getId().equals(user.getId())),"用户名已存在!");
        AssertUtil.isTrue(!(this.updateById(user)),"用户信息更新失败!");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateUserPassword(String userName, String oldPassword, String newPassword, String confirmPassword) {
        /**
         * 用户名非空 必须存在
         * 原始密码 新密码 确认密码 均不能为空
         * 原始密码必须正确
         * 新密码 与 确认密码必须一致  并且不能与原始密码相同
         */
        User user=null;
        user = this.findUserByUserName(userName);
        AssertUtil.isTrue(null==user,"用户不存在或未登录!");
        AssertUtil.isTrue(StringUtil.isEmpty(oldPassword),"请输入原始密码!");
        AssertUtil.isTrue(StringUtil.isEmpty(newPassword),"请输入新密码!");
        AssertUtil.isTrue(StringUtil.isEmpty(confirmPassword),"请输入确认密码!");
        AssertUtil.isTrue(!passwordEncoder.matches(oldPassword,user.getPassword()),"原始密码输入错误!");
        AssertUtil.isTrue(!(newPassword.equals(confirmPassword)),"新密码输入不一致!");
        AssertUtil.isTrue(newPassword.equals(oldPassword),"新密码与原始密码不能一致!");
        user.setPassword(passwordEncoder.encode(newPassword));
        AssertUtil.isTrue(!(this.updateById(user)),"用户密码更新失败!");

    }


}
