package org.zerock.ex2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing  //  BaseEntity 클래스 내의 {AuditingEntityListener.class} 를 활성화시키기 위한 설정
public class SpringbootWebProjectApplication {

    public static void main(String[] args) {

        SpringApplication.run(SpringbootWebProjectApplication.class, args);
    }

}
