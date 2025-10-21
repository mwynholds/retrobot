package retroboy;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import retroboy.job.importer.ImporterJob;
import retroboy.tool.CardTools;

@SpringBootApplication
public class Application implements CommandLineRunner {
  @Autowired private ImporterJob importerJob;

  public static void main(String[] args) {
    if (args.length == 0)
      SpringApplication.run(Application.class, args);
    else
      new SpringApplicationBuilder(Application.class)
          .web(WebApplicationType.NONE)
          .run(args);
  }

  @Bean
  public ToolCallbackProvider mcpTools(CardTools cardTools) {
    return MethodToolCallbackProvider.builder().toolObjects(cardTools).build();
  }

  @Override
  public void run(String... args) throws Exception {
    if (args.length == 0) return;

    String job = args[0].toLowerCase();
    if (job.equals("import")) {
      importerJob.run();
    }
  }
}
