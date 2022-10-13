package hello.proxy.advisor;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Method;

@Slf4j
public class AdvisorTest {

    @Test
    void advisorTest1() {
        ServiceInterface target = new ServiceImpl();

        // 프록시 팩토리는 어드바이저가 필수
        ProxyFactory proxyFactory = new ProxyFactory(target);

        // Advisor 인터페이스의 가장 일반적인 구현체. 생성자를 통해 하나의 포인트컷과 하나의 advice를 갖는다.
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(Pointcut.TRUE, new TimeAdvice());
        proxyFactory.addAdvisor(advisor);

        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.find();
        proxy.save();
    }

    @Test
    @DisplayName("직접 만든 포인트컷")
    void advisorTest2() {
        ServiceInterface target = new ServiceImpl();

        // 프록시 팩토리는 어드바이저가 필수
        ProxyFactory proxyFactory = new ProxyFactory(target);

        // Advisor 인터페이스의 가장 일반적인 구현체. 생성자를 통해 하나의 포인트컷과 하나의 advice를 갖는다.
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(new MyPointcut(), new TimeAdvice());
        proxyFactory.addAdvisor(advisor);

        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
        proxy.find();
    }

    @Test
    @DisplayName("스프링이 제공하는 포인트컷")
    void advisorTest3() {
        ServiceInterface target = new ServiceImpl();

        // 프록시 팩토리는 어드바이저가 필수
        ProxyFactory proxyFactory = new ProxyFactory(target);

        /**
         * NameMatchMethodPointcut: 메소드 이름을 기반으로 매칭. 내부에서는 PatternMatchUtils 사용
         * JdkRegextMethodPointcut: JDK 정규 표현식을 기반으로하는 포인트컷 매칭
         * TruePointcut: 항상 참
         * AnnotationMatchingPointcut: 어노테이션 매칭
         * AspectJExpressionPointcut: aspectJ 표현식으로 매칭
         */
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("save");

        // Advisor 인터페이스의 가장 일반적인 구현체. 생성자를 통해 하나의 포인트컷과 하나의 advice 갖는다.
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, new TimeAdvice());
        proxyFactory.addAdvisor(advisor);

        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
        proxy.find();
    }

    static class MyPointcut implements Pointcut {

        @Override
        public ClassFilter getClassFilter() {
            return ClassFilter.TRUE;
        }

        @Override
        public MethodMatcher getMethodMatcher() {
            return new MyMethodMatcher();
        }
    }

    static class MyMethodMatcher implements MethodMatcher {

        private final String matchName = "save";

        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            boolean result = method.getName().equals(matchName);
            log.info("포인트컷 호출 method={}, targetClass={}", method.getName(), targetClass);
            log.info("포인트컷 결과 result={}", result);

            return result;
        }

        @Override
        public boolean isRuntime() {
            return false;
        }

        @Override
        public boolean matches(Method method, Class<?> targetClass, Object... args) {
            return false;
        }
    }
}
