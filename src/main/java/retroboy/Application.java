package retroboy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import retroboy.job.importer.ImporterJob;

@SpringBootApplication
public class Application implements CommandLineRunner {
  @Autowired
  private ImporterJob importerJob;

  public static void main(String[] args) {
    if (args.length == 0)
      SpringApplication.run(Application.class, args);
    else
      new SpringApplicationBuilder(Application.class)
          .web(WebApplicationType.NONE)
          .run(args);
  }

  @Override
  public void run(String... args) throws Exception {
    if (args.length == 0)
      return;

    String job = args[0].toLowerCase();
    if (job.equals("import")) {
      importerJob.run();
    }
  }
}
