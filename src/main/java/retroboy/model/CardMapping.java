package retroboy.model;

import de.bytefish.pgbulkinsert.mapping.AbstractMapping;

public class CardMapping extends AbstractMapping<Card> {
  public CardMapping() {
    super("public", "cards");

    mapText("id", Card::id);
    mapText("body", Card::body);
    mapText("creator_id", Card::creatorId);
  }
}
