package retroboy.job.importer;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImporterJob {
  private StickiesUserRepo userRepo;

  @Autowired
  public void setUserRepo(StickiesUserRepo repo) {
    this.userRepo = repo;
  }

  public void run() {
    System.out.println(userRepo.count());

    try (Stream<StickiesUser> users = userRepo.findAllBy()) {
      users.forEach(user -> process(user));
    }
  }

  private void process(StickiesUser user) {
    System.out.println(user.activeIdentity().getEmail());
  }
}