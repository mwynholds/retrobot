package retroboy.model;

import de.bytefish.pgbulkinsert.mapping.AbstractMapping;

public class CardMapping extends AbstractMapping<Card> {
  public CardMapping() {
    super("public", "cards");

    mapText("id", Card::getId);
    mapText("body", Card::getBody);
    mapText("creator_id", Card::getCreatorId);
  }
}
