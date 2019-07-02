package fun.billon.member.dao;

import fun.billon.common.cache.CacheType;
import fun.billon.common.cache.annotation.Cacheable;
import fun.billon.member.api.constant.MemberCacheConstant;
import fun.billon.member.api.model.AdminModel;
import org.springframework.stereotype.Repository;

/**
 * 系统管理员DAO
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository
public interface IAdminDAO {

    /**
     * 根据特定条件查询主键
     *
     * @param adminModel
     * @return
     */
    @Cacheable(namespace = MemberCacheConstant.CACHE_NAMESPACE_ADMIN_MODEL,
            key = MemberCacheConstant.CACHE_KEY_ADMIN_ACCOUNT,
            type = Integer.class, cacheType = CacheType.VALUE)
    Integer queryPKByCriteria(AdminModel adminModel);


    /**
     * 根据主键获取管理员信息
     *
     * @param adminModel
     * @return
     */
    @Cacheable(namespace = MemberCacheConstant.CACHE_NAMESPACE_ADMIN_MODEL,
            key = MemberCacheConstant.CACHE_KEY_ADMIN_ID,
            type = AdminModel.class, cacheType = CacheType.HASH)
    AdminModel queryAdminByPK(AdminModel adminModel);

}
