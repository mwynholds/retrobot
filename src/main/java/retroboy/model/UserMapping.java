package retroboy.model;

import de.bytefish.pgbulkinsert.mapping.AbstractMapping;

public class UserMapping extends AbstractMapping<User> {
  public UserMapping() {
    super("public", "users");

    mapText("id", User::id);
    mapText("email", User::email);
    mapText("source", User::source);
    mapTimeStamp("created_at", User::createdAt);
    mapTimeStamp("updated_at", User::updatedAt);
  }

}
