package fun.billon.member.controller;

import fun.billon.common.constant.CommonStatusCode;
import fun.billon.common.exception.ParamException;
import fun.billon.common.model.ResultModel;
import fun.billon.common.util.StringUtils;
import fun.billon.member.api.model.AdminModel;
import fun.billon.member.service.IMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 系统管理员Controller
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @Resource
    private IMemberService memberService;

    /**
     * 系统管理员登陆
     *
     * @param paramMap paramMap.account   系统管理员账号(最长32位)        必填
     *                 paramMap.password  系统管理员密码(md5编码后的数据)  必填
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

        AdminModel adminModel = new AdminModel();
        adminModel.setAccount(paramMap.get("account"));
        adminModel.setPassword(paramMap.get("password"));

        // 账号密码校验
        return memberService.checkPassword(adminModel);
    }

    /**
     * 获取系统管理员信息
     *
     * @param adminUid 系统管理员id
     * @return
     */
    @GetMapping(value = "/id/{adminUid}")
    public ResultModel<AdminModel> getAdminById(@PathVariable(value = "adminUid") Integer adminUid) {
        AdminModel adminModel = new AdminModel();
        adminModel.setId(adminUid);
        return memberService.getAdmin(adminModel);
    }

}