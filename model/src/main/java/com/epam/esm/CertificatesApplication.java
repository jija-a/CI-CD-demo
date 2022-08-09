package com.epam.esm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * CertificatesApplication
 *
 * @author alex
 * @version 1.0
 * @since 21.04.22
 */
@SpringBootApplication
public class CertificatesApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CertificatesApplication.class, args);
    }
}
