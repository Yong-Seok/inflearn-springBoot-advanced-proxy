package hello.proxy;

import hello.proxy.config.v1_proxy.InterfaceProxyConfig;
import hello.proxy.trace.logtrace.LogTrace;
import hello.proxy.trace.logtrace.ThreadLocalLogTrace;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

//@Import(AppV1Config.class)
//@Import({AppV1Config.class, AppV2Config.class})
@Import(InterfaceProxyConfig.class)
// V3는 @SpringBootApplication 어노테이션 안의 @ComponentScan에 의해 스캔되므로 별도 설정파일 Import 없어도 됨
@SpringBootApplication(scanBasePackages = "hello.proxy.app")	// 주의. hello.proxy.config 패키지는 @Import를 사용해 적용
public class ProxyApplication {
	// SpringBoot run!!
	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}
}
