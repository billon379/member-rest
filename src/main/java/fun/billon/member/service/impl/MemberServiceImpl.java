package fun.billon.member.service.impl;

import fun.billon.common.model.ResultModel;
import fun.billon.common.util.MD5;
import fun.billon.common.util.StringUtils;
import fun.billon.member.api.constant.MemberStatusCode;
import fun.billon.member.api.model.AdminModel;
import fun.billon.member.api.model.UserModel;
import fun.billon.member.dao.IAdminDAO;
import fun.billon.member.dao.IUserDAO;
import fun.billon.member.service.IMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户服务接口实现
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class MemberServiceImpl implements IMemberService {

    @Autowired
    private IUserDAO userDAO;

    @Autowired
    private IAdminDAO adminDAO;

    /**
     * 新增用户,返回用户id
     *
     * @param userModel userModel.account   账号(邮箱,最长32位)    必填
     *                  userModel.password  密码(md5编码后的数据)  必填
     * @return resultModel
     */
    @Override
    public ResultModel<Integer> addUser(UserModel userModel) {
        ResultModel result = new ResultModel();

        /**
         * 用户已存在则给出提示
         */
        UserModel userInfo = queryUser(userModel);
        if (null != userInfo) {
            result.setFailed(MemberStatusCode.ACCOUNT_EXISTS, "该账号已被注册");
            return result;
        }

        /**
         * 用户不存在则插入
         */
        if (StringUtils.isEmpty(userModel.getNickname())) {
            userModel.setNickname("黑客_" + StringUtils.random(8, false));
        }
        //密码加盐
        userModel.setSalt(StringUtils.random(32, false));
        userModel.setPassword(MD5.encode(userModel.getPassword() + userModel.getSalt()));
        if (userDAO.insertUser(userModel) < 1) {
            result.setFailed(MemberStatusCode.ACCOUNT_EXISTS, "该账号已被注册");
            return result;
        }

        result.setData(userModel.getId());
        return result;
    }

    /**
     * 更新用户信息
     *
     * @param userModel userModel.id 必填
     * @return resultModel
     */
    @Override
    public ResultModel updateUser(UserModel userModel) {
        ResultModel result = new ResultModel();
        /**
         * 如果更新密码,则盐值也要重新生成
         */
        if (null != userModel.getPassword()) {
            //密码加盐
            userModel.setSalt(StringUtils.random(32, false));
            //设置密码
            userModel.setPassword(MD5.encode(userModel.getPassword() + userModel.getSalt()));
        }
        userDAO.updateUserByPK(userModel);
        return result;
    }


    /**
     * 根据条件获取用户信息
     * 三个条件的优先级为id > account > wxUid
     *
     * @param userModel userModel.id     用户id
     *                  userModel.account 账号
     *                  userModel.wxUid   微信用户id
     * @return
     */
    @Override
    public ResultModel<UserModel> getUser(UserModel userModel) {
        ResultModel<UserModel> result = new ResultModel<>();
        result.setData(queryUser(userModel));
        return result;
    }

    /**
     * 根据条件获取用户数
     *
     * @param userModel userModel.query 查询条件
     * @return
     */
    @Override
    public ResultModel<Integer> userCount(UserModel userModel) {
        ResultModel<Integer> result = new ResultModel<>();
        result.setData(userDAO.queryPKListCountByCriteria(userModel));
        return result;
    }

    /**
     * 搜索用户
     *
     * @param userModel userModel.query 查询条件
     * @return
     */
    @Override
    public ResultModel<List<UserModel>> searchUser(UserModel userModel) {
        ResultModel<List<UserModel>> result = new ResultModel<>();
        List<Integer> uidList = userDAO.queryPKListByCriteria(userModel);
        if (null != uidList && uidList.size() > 0) {
            List<UserModel> list = new ArrayList<>();
            UserModel um = new UserModel();
            for (Integer uid : uidList) {
                um.setId(uid);
                UserModel userInfo = queryUser(um);
                list.add(userInfo);
            }
            result.setData(list);
        }
        return result;
    }

    /**
     * 账号密码校验
     *
     * @param userModel userModel.account    账号
     *                  userModel.password   密码
     * @return
     */
    @Override
    public ResultModel<Integer> checkPassword(UserModel userModel) {
        ResultModel<Integer> result = new ResultModel<>();

        // 根据账号查询用户信息
        UserModel dbUserModel = queryUser(userModel);

        /**
         * 未获取到用户信息
         */
        if (null == dbUserModel) {
            result.setFailed(MemberStatusCode.ACCOUNT_NOT_EXISTS, "账号不存在");
            return result;
        }

        /**
         * 获取到用户信息后进行密码校验
         */
        String password = MD5.encode(userModel.getPassword() + dbUserModel.getSalt());
        if (!dbUserModel.getPassword().equals(password)) {
            // 密码不正确
            result.setFailed(MemberStatusCode.ACCOUNT_OR_PASSWORD_INCORRECT, "账号或密码不正确");
            return result;
        }

        // 校验通过返回用户信息
        result.setData(dbUserModel.getId());
        return result;
    }

    /**
     * 获取多用户信息
     *
     * @param uidList
     * @return
     */
    @Override
    public ResultModel<Map<Integer, UserModel>> getUserList(List<Integer> uidList) {
        ResultModel<Map<Integer, UserModel>> result = new ResultModel<>();
        if (null != uidList && uidList.size() > 0) {
            Map<Integer, UserModel> map = new HashMap<>();
            UserModel um = new UserModel();
            for (Integer uid : uidList) {
                um.setId(uid);
                UserModel userInfo = queryUser(um);
                map.put(uid, userInfo);
            }
            result.setData(map);
        }
        return result;
    }

    /**
     * 根据条件获取用户信息,优先级为id > account > wxUid
     *
     * @param userModel userModel.id     用户id
     *                  userModel.account 账号
     *                  userModel.wxUid   微信用户id
     * @return
     */
    private UserModel queryUser(UserModel userModel) {
        //拷贝属性到新对象
        UserModel userModelCopy = new UserModel();
        BeanUtils.copyProperties(userModel, userModelCopy);

        if (null == userModelCopy.getId()) {
            Integer id = userDAO.queryPKByCriteria(userModelCopy);
            userModelCopy.setId(id);
        }

        return userDAO.queryUserByPK(userModelCopy);
    }

    /**************************系统管理员*****************************/
    /**
     * 系统管理员账号密码校验
     *
     * @param adminModel adminModel.account    系统管理员账号
     *                   adminModel.password   系统管理员密码
     * @return
     */
    @Override
    public ResultModel<Integer> checkPassword(AdminModel adminModel) {
        ResultModel<Integer> result = new ResultModel<>();

        // 根据账号查询系统管理员信息
        AdminModel dbAdminModel = searchAdmin(adminModel);

        /**
         * 未获取到系统管理员信息
         */
        if (null == dbAdminModel) {
            result.setFailed(MemberStatusCode.ACCOUNT_NOT_EXISTS, "账号不存在");
            return result;
        }

        /**
         * 获取到系统管理员信息后进行密码校验
         */
        String password = MD5.encode(adminModel.getPassword() + dbAdminModel.getSalt());
        if (!dbAdminModel.getPassword().equals(password)) {
            // 密码不正确
            result.setFailed(MemberStatusCode.ACCOUNT_OR_PASSWORD_INCORRECT, "账号或密码不正确");
            return result;
        }

        // 校验通过返回用户信息
        result.setData(dbAdminModel.getId());
        return result;
    }

    /**
     * 根据条件获取系统管理员信息,优先级为id > account
     *
     * @param adminModel adminModel.id      系统管理员id
     *                   adminModel.account 系统管理员账号
     * @return
     */
    @Override
    public ResultModel<AdminModel> getAdmin(AdminModel adminModel) {
        ResultModel<AdminModel> result = new ResultModel<>();
        result.setData(searchAdmin(adminModel));
        return result;
    }

    /**
     * 根据条件获取系统管理员信息,优先级为id > account
     *
     * @param adminModel adminModel.id      系统管理员id
     *                   adminModel.account 系统管理员账号
     * @return
     */
    private AdminModel searchAdmin(AdminModel adminModel) {
        //拷贝属性到新对象
        AdminModel adminModelCopy = new AdminModel();
        BeanUtils.copyProperties(adminModel, adminModelCopy);

        if (null == adminModelCopy.getId()) {
            Integer id = adminDAO.queryPKByCriteria(adminModelCopy);
            adminModelCopy.setId(id);
        }

        return adminDAO.queryAdminByPK(adminModelCopy);
    }

}