package retroboy.model;

import de.bytefish.pgbulkinsert.mapping.AbstractMapping;

public class AuthorMapping extends AbstractMapping<Author> {
  public AuthorMapping() {
    super("public", "authors");

    mapText("card_id", Author::getCardId);
    mapText("user_id", Author::getUserId);
  }

}
