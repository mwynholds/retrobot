package retroboy.job.importer.model;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public record StickiesUser(@Id String id, Set<StickiesIdentity> identities, String activeIdentity) implements StickiesIdentifiable {
  public StickiesIdentity extractActiveIdentity() {
    if (identities == null) return null;
    List<StickiesIdentity> valid = identities.stream().filter(ident -> ident.email() != null).toList();

    if (valid.size() == 0) return null;
    if (valid.size() == 1) return valid.get(0);

    if (activeIdentity != null) {
      Optional<StickiesIdentity> active = valid.stream().filter(ident -> ident.id().equals(activeIdentity)).findFirst();
      if (active.isPresent()) return active.get();
    }
    Comparator<StickiesIdentity> sorter = Comparator.comparing(StickiesIdentity::updated).reversed();
    return valid.stream().sorted(sorter).findFirst().orElse(null);
  }
}
