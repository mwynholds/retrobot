package retroboy.config;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import me.paulschwarz.springdotenv.DotenvPropertySource;

@Order(Ordered.HIGHEST_PRECEDENCE)
public class DotEnvLoader implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

  @Override
  public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
    DotenvPropertySource.addToEnvironment(event.getEnvironment());
  }
}
