package sopi.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages={"sopi"},
	excludeFilters={
		//@Filter(type=FilterType.ANNOTATION, value=EnableWebMvc.class)
	})
public class RootConfig {
	
}
