package retroboy.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import retroboy.model.Card;
import retroboy.model.User;

@RestController
@RequestMapping("/cards")
public class CardController {

    @GetMapping
    public List<Card> getCards() {
        User user1 = new User();
        user1.setEmail("mike@wynholds.com");

        Card card1 = new Card();
        card1.setBody("I like that we show up on time");
        card1.setCreator(user1);
        card1.getAuthors().add(user1);

        return List.of(card1);
    }
}
