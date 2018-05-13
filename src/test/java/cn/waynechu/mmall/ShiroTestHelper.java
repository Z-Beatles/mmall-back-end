package cn.waynechu.mmall;

import cn.waynechu.mmall.entity.User;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.apache.shiro.util.ThreadState;
import org.mockito.Mockito;


/**
 * @author waynechu
 * Created 2018-05-13 11:31
 */
public class ShiroTestHelper {
    private static ThreadState threadState;

    private ShiroTestHelper() {
    }

    /**
     * 绑定Subject到当前线程
     */
    private  static void bindSubject(Subject subject) {
        clearSubject();
        threadState = new SubjectThreadState(subject);
        threadState.bind();
    }

    /**
     * 用Mockito快速创建一个已认证的用户
     */
    public static void mockSubject(User principal) {
        Subject subject = Mockito.mock(Subject.class);
        Mockito.when(subject.isAuthenticated()).thenReturn(true);
        Mockito.when(subject.getPrincipal()).thenReturn(principal);
        bindSubject(subject);
    }

    /**
     * 清除当前线程中的Subject
     */
    public static void clearSubject() {
        if (threadState != null) {
            threadState.clear();
            threadState = null;
        }
    }
}
