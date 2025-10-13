package retroboy.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class Card {
  private String body;
  private User creator;
  private Set<User> authors = new HashSet<User>();
}
