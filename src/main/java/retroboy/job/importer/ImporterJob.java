package retroboy.job.importer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import retroboy.model.Author;
import retroboy.model.Card;
import retroboy.model.User;

@Component
public class ImporterJob {
  @Autowired
  private StickiesUserRepository stickiesUserRepo;
  @Autowired
  private StickiesCardRepository stickiesCardRepo;
  @Autowired
  private JdbcAggregateTemplate aggregateTemplate;
  @Autowired
  private JdbcTemplate template;

  public void run() {
    template.update("truncate table users, cards, authors restart identity");

    System.out.print("Inserting users ...");
    try (Stream<StickiesUser> susers = stickiesUserRepo.findAllBy()) {
      partition(susers, 10000, slice -> processUser(slice));
    }
    System.out.println();

    Set<String> suserIds = new HashSet<>(template.queryForList("select id from users", String.class));
    System.out.println(suserIds.size());
    System.out.println(suserIds.contains("51dd9cadc14a5e281400000e"));

    System.out.print("Inserting cards ...");
    try (Stream<StickiesCard> scards = stickiesCardRepo.findAllBy()) {
      partition(scards, 50000, slice -> processCard(slice, suserIds));
    }
    System.out.println();
  }

  private <T extends StickiesIdentifiable> void partition(Stream<T> stream, int max, Consumer<List<T>> action) {
    List<T> buffer = new ArrayList<T>(max);
    stream.forEach(doc -> {
      buffer.add(doc);
      if (buffer.size() >= max) {
        action.accept(buffer);
        buffer.clear();
      }
    });
    action.accept(buffer);
  }

  @Transactional
  private void processUser(List<StickiesUser> susers) {
    List<User> users = susers.stream().map(suser -> {
      StickiesIdentity sident = suser.activeIdentity();
      if (sident == null) return null;

      User user = new User();
      user.setId(suser.getId());
      user.setEmail(sident.getEmail());
      user.setSource(sident.getSource());
      return user;
    }).filter(Objects::nonNull).toList();

    aggregateTemplate.insertAll(users);
    System.out.print(".");
  }

  @Transactional
  private void processCard(List<StickiesCard> scards, Set<String> suserIds) {
    List<Card> cards = new ArrayList<>(scards.size());
    List<Author> authors = new ArrayList<>(scards.size());

    scards.forEach(scard -> {
      if (scard.getCreator() == null) return;
      if (!suserIds.contains(scard.getCreator())) return;

      Card card = new Card();
      card.setId(scard.getId());
      card.setBody(scard.getText());
      card.setCreatorId(scard.getCreator());
      cards.add(card);

      scard.getAuthors().forEach(authorId -> {
        if (!suserIds.contains(authorId)) return;

        Author author = new Author();
        author.setCardId(scard.getId());
        author.setUserId(authorId);
        authors.add(author);
      });
    });

    aggregateTemplate.insertAll(cards);
    aggregateTemplate.insertAll(authors);
    System.out.print(".");
  }
}