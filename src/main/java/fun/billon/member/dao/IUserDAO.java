package fun.billon.member.dao;

import fun.billon.common.cache.CacheType;
import fun.billon.common.cache.annotation.CacheEvict;
import fun.billon.common.cache.annotation.Cacheable;
import fun.billon.member.api.constant.MemberCacheConstant;
import fun.billon.member.api.model.UserModel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户DAO
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository
public interface IUserDAO {

    /**
     * 新增用户
     *
     * @param userModel
     * @return
     */
    int insertUser(UserModel userModel);

    /**
     * 修改用户
     *
     * @param userModel
     * @return
     */
    @CacheEvict(namespace = MemberCacheConstant.CACHE_NAMESPACE_USER_MODEL,
            key = MemberCacheConstant.CACHE_KEY_USER_ID)
    int updateUserByPK(UserModel userModel);

    /**
     * 根据特定条件查询主键
     *
     * @param userModel
     * @return
     */
    @Cacheable(namespace = MemberCacheConstant.CACHE_NAMESPACE_USER_MODEL,
            key = MemberCacheConstant.CACHE_KEY_USER_ACCOUNT,
            type = Integer.class, cacheType = CacheType.VALUE)
    Integer queryPKByCriteria(UserModel userModel);

    /**
     * 根据特定条件获取记录数
     *
     * @param userModel
     * @return
     */
    int queryPKListCountByCriteria(UserModel userModel);

    /**
     * 根据特定条件查询主键列表
     *
     * @param userModel
     * @return
     */
    List<Integer> queryPKListByCriteria(UserModel userModel);


    /**
     * 根据主键获取用户信息
     *
     * @param userModel
     * @return
     */
    @Cacheable(namespace = MemberCacheConstant.CACHE_NAMESPACE_USER_MODEL,
            key = MemberCacheConstant.CACHE_KEY_USER_ID,
            type = UserModel.class, cacheType = CacheType.HASH)
    UserModel queryUserByPK(UserModel userModel);

}
