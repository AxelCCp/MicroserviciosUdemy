package com.springboot.app.commons.usuarios;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude= {DataSourceAutoConfiguration.class}) //SE EXCLUYE LA CONFIGURACIÓN DE SPRING DATA JPA,PARA QUE NO PIDA LA CONEXIÓN.
public class SpringbootServicioUsuariosCommons8Application {

}
