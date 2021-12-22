package com.springboot.app.commons;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


//SE DESHABILITA LA AUTOCONFIGURACIÓN DEL DATASOURCE 
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication
public class SpringbootServicioCommonsApplication {
	//SE QUITA EL MÉTODO MAIN, YA QUE ESTE SERVICIO ES MÁS BIEN SOLO UNA LIBRERÍA
}
