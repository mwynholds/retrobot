package retroboy.job.importer;

import java.util.Date;

import org.springframework.data.annotation.Id;

@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class StickiesIdentity {
  @Id
  private String id;
  private String email;
  private String source;
  private Date updated;
}
