package fun.billon.member.service;

import fun.billon.common.model.ResultModel;
import fun.billon.member.api.model.AdminModel;
import fun.billon.member.api.model.UserModel;

import java.util.List;
import java.util.Map;


/**
 * 用户服务接口
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IMemberService {

    /**
     * 新增用户,返回用户id
     *
     * @param userModel
     * @return ResultModel<用户uid>
     */
    ResultModel<Integer> addUser(UserModel userModel);

    /**
     * 更新用户信息
     *
     * @param userModel userModel.id 必填
     * @return ResultModel
     */
    ResultModel updateUser(UserModel userModel);

    /**
     * 账号密码校验
     *
     * @param userModel userModel.account    账号
     *                  userModel.password   密码
     * @return ResultModel<用户uid>
     */
    ResultModel<Integer> checkPassword(UserModel userModel);


    /**
     * 根据条件获取用户信息
     * 三个条件的优先级为uid > account > wxUid
     *
     * @param userModel userModel.uid     用户id
     *                  userModel.account 账号
     *                  userModel.wxUid   微信用户id
     * @return ResultModel<用户信息>
     */
    ResultModel<UserModel> getUser(UserModel userModel);

    /**
     * 根据条件获取用户数
     *
     * @param userModel userModel.keywords 搜索条件
     * @return ResultModel<用户数>
     */
    ResultModel<Integer> userCount(UserModel userModel);

    /**
     * 搜索用户
     *
     * @param userModel userModel.keywords 搜索条件
     * @return ResultModel<用户列表>
     */
    ResultModel<List<UserModel>> searchUser(UserModel userModel);

    /**
     * 获取多用户信息
     *
     * @param uidList 用户id列表
     * @return ResultModel<用户uid, 用户信息>
     */
    ResultModel<Map<Integer, UserModel>> getUserList(List<Integer> uidList);


    /**************************系统管理员*****************************/
    /**
     * 系统管理员账号密码校验
     *
     * @param adminModel adminModel.account    系统管理员账号
     *                   adminModel.password   系统管理员密码
     * @return ResultModel<管理员id>
     */
    ResultModel<Integer> checkPassword(AdminModel adminModel);

    /**
     * 根据条件获取系统管理员信息
     *
     * @param adminModel adminModel.id     系统管理员id
     * @return ResultModel<管理员信息>
     */
    ResultModel<AdminModel> getAdmin(AdminModel adminModel);

}
