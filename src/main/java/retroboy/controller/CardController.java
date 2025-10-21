package retroboy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import retroboy.model.Card;
import retroboy.model.CardRepository;

@RestController
@RequestMapping("/cards")
public class CardController {
    @Autowired private CardRepository repo;

    @GetMapping("/search")
    public List<Card> searchCards(@RequestParam String query) {
        return repo.findByQuery(query, 100);
    }
}
