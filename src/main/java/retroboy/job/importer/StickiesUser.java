package retroboy.job.importer;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@lombok.Data
@lombok.NoArgsConstructor
@Document(collection = "users")
public class StickiesUser {
  @Id
  private String id;
  private Set<StickiesIdentity> identities;
  private String activeIdentity;

  public StickiesIdentity activeIdentity() {
    if (identities == null)
      return null;
    if (identities.size() == 0)
      return null;
    if (identities.size() == 1)
      return identities.iterator().next();
    if (activeIdentity != null) {
      Optional<StickiesIdentity> active = identities.stream().filter(ident -> ident.getId().equals(activeIdentity))
          .findFirst();
      if (active.isPresent())
        return active.get();
    }
    Comparator<StickiesIdentity> sorter = Comparator.comparing(StickiesIdentity::getUpdated).reversed();
    return identities.stream().sorted(sorter).findFirst().orElse((null));
  }
}
