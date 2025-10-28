package retroboy;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import retroboy.config.DotEnvLoader;
import retroboy.job.importer.ImporterJob;
import retroboy.tool.CardTools;

@SpringBootApplication
public class Application implements CommandLineRunner {
  @Autowired private ImporterJob importerJob;

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(Application.class);
    app.setBannerMode(Banner.Mode.OFF);
    app.addListeners(new DotEnvLoader());

    String job = jobName(args);
    if ("stdio".equals(job)) {
      System.setProperty("spring.ai.mcp.server.stdio", "true");
      System.setProperty("spring.main.log-startup-info", "false");
      System.setProperty("logging.level.root", "ERROR");
    } else if ("import".equals(job)) {
      app.setWebApplicationType(WebApplicationType.NONE);
    }

    app.run(args);
  }

  @Bean
  public ToolCallbackProvider mcpTools(CardTools cardTools) {
    return MethodToolCallbackProvider.builder().toolObjects(cardTools).build();
  }

  @Override
  public void run(String... args) throws Exception {
    String job = jobName(args);
    if ("import".equals(job)) {
      importerJob.run();
    }
  }

  private static String jobName(String[] args) {
    if (args.length == 0) return null;

    return args[0].toLowerCase();
  }
}
