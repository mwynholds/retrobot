package retroboy.job.importer;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.postgresql.PGConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import de.bytefish.pgbulkinsert.PgBulkInsert;
import de.bytefish.pgbulkinsert.util.PostgreSqlUtils;
import retroboy.job.importer.model.StickiesCard;
import retroboy.job.importer.model.StickiesCardRepository;
import retroboy.job.importer.model.StickiesIdentifiable;
import retroboy.job.importer.model.StickiesIdentity;
import retroboy.job.importer.model.StickiesUser;
import retroboy.job.importer.model.StickiesUserRepository;
import retroboy.model.Author;
import retroboy.model.AuthorMapping;
import retroboy.model.Card;
import retroboy.model.CardMapping;
import retroboy.model.User;
import retroboy.model.UserMapping;

@Component
public class ImporterJob {
  @Autowired private StickiesUserRepository stickiesUserRepo;
  @Autowired private StickiesCardRepository stickiesCardRepo;
  @Autowired private JdbcTemplate template;

  private PgBulkInsert<User> bulkUser = new PgBulkInsert<>(new UserMapping());
  private PgBulkInsert<Card> bulkCard = new PgBulkInsert<>(new CardMapping());
  private PgBulkInsert<Author> bulkAuthor = new PgBulkInsert<>(new AuthorMapping());

  private ExecutorService bulkPool = Executors.newFixedThreadPool(1);

  private PGConnection pgcon;

  public void run() {
    try {
      pgcon = PostgreSqlUtils.getPGConnection(template.getDataSource().getConnection());

      template.update("truncate table users, cards, authors restart identity");

      Instant start;
      Duration elapsed;

      start = Instant.now();
      System.out.print("Inserting users ...");
      try (Stream<StickiesUser> susers = stickiesUserRepo.findAllBy()) {
        process(susers, 25_000, slice -> processUser(slice));
      }
      elapsed = Duration.between(start, Instant.now());
      System.out.println(" " + elapsed.toMillis() + " ms");

      Set<String> suserIds = new HashSet<>(template.queryForList("select id from users", String.class));

      start = Instant.now();
      System.out.print("Inserting cards ...");
      try (Stream<StickiesCard> scards = stickiesCardRepo.findAllBy()) {
        process(scards, 100_000, slice -> processCard(slice, suserIds));
      }
      bulkPool.shutdown();
      bulkPool.awaitTermination(1, TimeUnit.MINUTES);
      elapsed = Duration.between(start, Instant.now());
      System.out.println(" " + elapsed.toMillis() + " ms");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private <T extends StickiesIdentifiable> void process(Stream<T> stream, int max, Consumer<List<T>> action) {
    List<T> buffer = new ArrayList<T>(max);
    stream.forEach(doc -> {
      buffer.add(doc);
      if (buffer.size() >= max) {
        action.accept(buffer);
        buffer.clear();
      }
    });
  }

  private void processUser(List<StickiesUser> susers) {
    List<User> users;

    users = susers.stream().map(suser -> {
      StickiesIdentity sident = suser.activeIdentity();
      if (sident == null)
        return null;

      User user = new User();
      user.setId(suser.getId());
      user.setEmail(sident.getEmail());
      user.setSource(sident.getSource());
      return user;
    }).filter(Objects::nonNull).toList();

    try {
      bulkUser.saveAll(pgcon, users.stream());
    } catch (SQLException se) {
      throw new RuntimeException(se);
    }
    System.out.print(".");
  }

  private void processCard(List<StickiesCard> scards, Set<String> suserIds) {
    List<Card> cards;
    List<Author> authors;

    cards = new ArrayList<>(scards.size());
    authors = new ArrayList<>(scards.size());

    scards.forEach(scard -> {
      if (scard.getCreator() == null)
        return;
      if (!suserIds.contains(scard.getCreator()))
        return;

      Card card = new Card();
      card.setId(scard.getId());
      card.setBody(scard.getText());
      card.setCreatorId(scard.getCreator());
      cards.add(card);

      scard.getAuthors().forEach(authorId -> {
        if (!suserIds.contains(authorId))
          return;

        Author author = new Author();
        author.setCardId(scard.getId());
        author.setUserId(authorId);
        authors.add(author);
      });
    });

    try {
      bulkCard.saveAll(pgcon, cards.stream());
    } catch (SQLException se) {
      throw new RuntimeException(se);
    }

    bulkPool.execute(() -> bulkInsertAuthors(authors));
    System.out.print(".");
  }

  private void bulkInsertAuthors(List<Author> authors) {
    try {
      bulkAuthor.saveAll(pgcon, authors.stream());
    } catch (SQLException se) {
      throw new RuntimeException(se);
    }
  }
}