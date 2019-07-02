package fun.billon.member.controller;

import fun.billon.common.constant.CommonStatusCode;
import fun.billon.common.exception.ParamException;
import fun.billon.common.model.ResultModel;
import fun.billon.common.util.StringUtils;
import fun.billon.member.api.model.UserModel;
import fun.billon.member.service.IMemberService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户Controller
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IMemberService memberService;

    /**
     * 注册
     *
     * @param paramMap paramMap.account   账号(邮箱,最长32位)    必填
     *                 paramMap.password  密码(md5编码后的数据)  必填
     * @return
     */
    @PostMapping
    public ResultModel<Integer> register(@RequestParam Map<String, String> paramMap) {
        ResultModel<Integer> resultModel = new ResultModel<>();
        String[] paramArray = new String[]{"account", "password"};
        boolean[] requiredArray = new boolean[]{true, true};
        Class[] classArray = new Class[]{String.class, String.class};
        try {
            StringUtils.checkParam(paramMap, paramArray, requiredArray, classArray, null);
        } catch (ParamException e) {
            resultModel.setFailed(CommonStatusCode.PARAM_INVALID, e.getMessage());
            return resultModel;
        }

        UserModel userModel = new UserModel();
        userModel.setAccount(paramMap.get("account"));
        userModel.setPassword(paramMap.get("password"));
        return memberService.addUser(userModel);
    }

    /**
     * 登陆
     *
     * @param paramMap paramMap.account   账号(邮箱,最长32位)    必填
     *                 paramMap.password  密码(md5编码后的数据)  必填
     * @return
     */
    @GetMapping
    public ResultModel<Integer> login(@RequestParam Map<String, String> paramMap) {
        ResultModel<Integer> resultModel = new ResultModel<>();
        String[] paramArray = new String[]{"account", "password"};
        boolean[] requiredArray = new boolean[]{true, true};
        Class[] classArray = new Class[]{String.class, String.class};
        try {
            StringUtils.checkParam(paramMap, paramArray, requiredArray, classArray, null);
        } catch (ParamException e) {
            resultModel.setFailed(CommonStatusCode.PARAM_INVALID, e.getMessage());
            return resultModel;
        }

        UserModel userModel = new UserModel();
        userModel.setAccount(paramMap.get("account"));
        userModel.setPassword(paramMap.get("password"));

        // 账号密码校验
        return memberService.checkPassword(userModel);
    }

    /**
     * 根据条件获取用户数
     *
     * @param paramMap paramMap.keywords      搜索关键字    选填
     * @return
     */
    @GetMapping("/count")
    public ResultModel<Integer> count(@RequestParam Map<String, String> paramMap) {
        ResultModel<Integer> resultModel = new ResultModel<>();
        String[] paramArray = new String[]{"query"};
        boolean[] requiredArray = new boolean[]{false};
        Class[] classArray = new Class[]{String.class};
        try {
            StringUtils.checkParam(paramMap, paramArray, requiredArray, classArray, null);
        } catch (ParamException e) {
            resultModel.setFailed(CommonStatusCode.PARAM_INVALID, e.getMessage());
            return resultModel;
        }

        UserModel userModel = new UserModel();
        userModel.setKeywords(paramMap.get("keywords"));
        return memberService.userCount(userModel);
    }

    /**
     * 搜索用户
     *
     * @param paramMap paramMap.keywords   搜索关键字  选填
     *                 paramMap.pageSize   分页大小    选填
     *                 paramMap.pageIndex  分页页码    选填
     * @return
     */
    @GetMapping("/search")
    public ResultModel<List<UserModel>> search(@RequestParam Map<String, String> paramMap) {
        ResultModel<List<UserModel>> resultModel = new ResultModel<>();
        String[] paramArray = new String[]{"query", "pageSize", "pageIndex"};
        boolean[] requiredArray = new boolean[]{false, false, false};
        Class[] classArray = new Class[]{String.class, Integer.class, Integer.class};
        try {
            StringUtils.checkParam(paramMap, paramArray, requiredArray, classArray, null);
        } catch (ParamException e) {
            resultModel.setFailed(CommonStatusCode.PARAM_INVALID, e.getMessage());
            return resultModel;
        }

        UserModel userModel = new UserModel();
        userModel.setKeywords(paramMap.get("keywords"));
        if (!StringUtils.isEmpty(paramMap.get("pageSize"))) {
            userModel.setPageSize(Integer.parseInt(paramMap.get("pageSize")));
        }
        if (!StringUtils.isEmpty(paramMap.get("pageIndex"))) {
            userModel.setPageIndex(Integer.parseInt(paramMap.get("pageIndex")));
        }

        // 搜索用户
        return memberService.searchUser(userModel);
    }

    /**
     * 更新用户
     *
     * @param uid      用户id
     * @param paramMap 用户信息
     * @return
     */
    @PutMapping(value = "/id/{uid}")
    public ResultModel<UserModel> updateUser(@PathVariable(value = "uid") Integer uid,
                                             @RequestParam Map<String, String> paramMap) {
        ResultModel<UserModel> resultModel = new ResultModel<>();
        String[] paramArray = new String[]{"password", "nickname", "sex", "headImgUrl"};
        boolean[] requiredArray = new boolean[]{false, false, false, false};
        Class[] classArray = new Class[]{String.class, String.class, Integer.class, String.class};
        try {
            StringUtils.checkParam(paramMap, paramArray, requiredArray, classArray, null);
        } catch (ParamException e) {
            resultModel.setFailed(CommonStatusCode.PARAM_INVALID, e.getMessage());
            return resultModel;
        }

        UserModel userModel = new UserModel();
        userModel.setId(uid);
        userModel.setPassword(paramMap.get("password"));
        userModel.setNickname(paramMap.get("nickname"));
        userModel.setHeadImgUrl(paramMap.get("headImgUrl"));
        if (!StringUtils.isEmpty(paramMap.get("sex"))) {
            userModel.setSex(Integer.parseInt(paramMap.get("sex")));
        }
        memberService.updateUser(userModel);
        return memberService.getUser(userModel);
    }


    /**
     * 获取用户信息
     *
     * @param uid 用户id
     * @return
     */
    @GetMapping(value = "/id/{uid}")
    public ResultModel<UserModel> getUserById(@PathVariable(value = "uid") Integer uid) {
        UserModel userModel = new UserModel();
        userModel.setId(uid);
        return memberService.getUser(userModel);
    }

    /**
     * 获取用户信息
     *
     * @param uids 用户id列表
     * @return
     */
    @GetMapping(value = "/ids/{uids}")
    public ResultModel<Map<Integer, UserModel>> getUserByIds(@PathVariable(value = "uids") String uids) {
        ResultModel<Map<Integer, UserModel>> result = new ResultModel<>();
        String[] uidArray = uids.split(",");
        if (uidArray.length > 0) {
            List<Integer> uidList = new ArrayList<>();
            for (String uid : uidArray) {
                try {
                    Integer.parseInt(uid);
                } catch (Exception e) {
                    result.setFailed(CommonStatusCode.PARAM_INVALID, "参数[uids]类型不合法");
                    return result;
                }
                uidList.add(Integer.parseInt(uid));
            }
            result = memberService.getUserList(uidList);
        }
        return result;
    }

}
