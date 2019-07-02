package fun.billon.member.service;

import fun.billon.common.util.MD5;
import fun.billon.common.util.StringUtils;
import org.junit.Test;

/**
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class MemberServiceTest {

    @Test
    public void testAddAdmin() {
        String initalPassword = MD5.encode("billon");
        String salt = StringUtils.random(32, false);
        String password = MD5.encode(initalPassword + salt);
        System.out.println("initalPassword:" + initalPassword);
        System.out.println("password:" + password);
        System.out.println("salt:" + salt);
    }

}
