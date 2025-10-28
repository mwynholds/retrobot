package retroboy.controller;

import static tech.mogami.commons.constant.X402Constants.X402_X_PAYMENT_HEADER;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.web3j.crypto.Credentials;

import retroboy.model.Card;
import retroboy.model.CardRepository;
import tech.mogami.commons.header.payment.PaymentPayload;
import tech.mogami.commons.header.payment.PaymentRequired;
import tech.mogami.commons.header.payment.PaymentRequirements;
import tech.mogami.java.client.helper.X402PaymentHelper;
import tech.mogami.spring.annotation.X402PayUSDC;

@RestController
@RequestMapping("/cards")
public class CardController {
    @Autowired private CardRepository repo;
    @Value("${x402.client.fromaddress}") private String fromAddress;
    @Value("${x402.client.fromprivatekey}") private String fromPrivateKey;

    @GetMapping("/search")
    public List<Card> searchCards(@RequestParam String query) {
        return repo.findByQuery(query, 100);
    }

    @GetMapping("/$search")
    @X402PayUSDC(amount = "0.25")
    public List<Card> searchCardsX402(@RequestParam String query) {
        return searchCards(query);
    }

    @GetMapping("/searchclient")
    public String searchClient(@RequestParam String query) {
        RestTemplate rest = new RestTemplate();
        String url = "http://localhost:8080/api/v1/cards/$search";
        URI uri = UriComponentsBuilder.fromUriString(url).queryParam("query", query).build().encode().toUri();
        String body;

        try {
            rest.getForEntity(uri, String.class);
            throw new IllegalStateException("Did not receive 402 status code: 200");
        } catch (HttpClientErrorException e) {
            int code = e.getStatusCode().value();
            if (code != 402) throw new IllegalStateException("Did not receive 402 status code: %i".formatted(code));
            body = e.getResponseBodyAsString();
        }

        PaymentRequired required = X402PaymentHelper.getPaymentRequiredFromBody(body)
                .orElseThrow(() -> new IllegalStateException("Payment requirements not found in response"));
        PaymentRequirements requirements = required.accepts().getFirst();
        PaymentPayload payload = X402PaymentHelper.getPayloadFromPaymentRequirements(null, fromAddress, requirements);
        PaymentPayload signed = X402PaymentHelper.getSignedPayload(Credentials.create(fromPrivateKey), requirements, payload);

        HttpHeaders headers = new HttpHeaders();
        headers.set(X402_X_PAYMENT_HEADER, X402PaymentHelper.getPayloadHeader(signed));
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = rest.exchange(uri, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }
}
