package retroboy.job.importer;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@Document(collection = "users")
public class StickiesUser implements StickiesIdentifiable {
  @Id
  private String id;
  private Set<StickiesIdentity> identities;
  private String activeIdentity;

  public StickiesIdentity activeIdentity() {
    if (identities == null) return null;
    List<StickiesIdentity> valid = identities.stream().filter(ident -> ident.getEmail() != null).toList();

    if (valid.size() == 0) return null;
    if (valid.size() == 1) return valid.get(0);

    if (activeIdentity != null) {
      Optional<StickiesIdentity> active = valid.stream().filter(ident -> ident.getId().equals(activeIdentity)).findFirst();
      if (active.isPresent()) return active.get();
    }
    Comparator<StickiesIdentity> sorter = Comparator.comparing(StickiesIdentity::getUpdated).reversed();
    return valid.stream().sorted(sorter).findFirst().orElse(null);
  }
}
