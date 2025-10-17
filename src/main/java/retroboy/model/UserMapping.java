package retroboy.model;

import de.bytefish.pgbulkinsert.mapping.AbstractMapping;

public class UserMapping extends AbstractMapping<User> {
  public UserMapping() {
    super("public", "users");

    mapText("id", User::getId);
    mapText("email", User::getEmail);
    mapText("source", User::getSource);
  }

}
