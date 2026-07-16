package br.com.dio.dioprojetomodulo5curso4springcloudopenfeign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.map.repository.config.EnableMapRepositories;

@SpringBootApplication
@EnableMapRepositories
@EnableFeignClients
public class DioProjetoModulo5Curso4SpringCloudOpenfeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(DioProjetoModulo5Curso4SpringCloudOpenfeignApplication.class, args);
    }

}
