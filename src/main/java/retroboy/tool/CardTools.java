package retroboy.tool;

import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import retroboy.model.Card;
import retroboy.model.CardRepository;

@Service
public class CardTools {
  @Autowired private CardRepository cardRepo;

  @Tool(description = "Search for Stickies retro cards by keyword")
  public List<Card> searchCards(String query) {
    return cardRepo.findByQuery(query, 100);
  }
}
