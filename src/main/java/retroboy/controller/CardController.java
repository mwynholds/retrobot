package retroboy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import retroboy.model.Card;
import retroboy.model.CardRepository;

@RestController
@RequestMapping("/cards")
public class CardController {
    @Autowired private CardRepository repo;

    @GetMapping
    public List<Card> getCards() {
        return repo.findSome(100);
    }
}
