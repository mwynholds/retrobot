package retroboy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
@EnableMongoRepositories(basePackages = "retroboy.job.importer")
public class MongoConfig extends AbstractMongoClientConfiguration {

  @Value("${spring.data.mongodb.host:localhost}")
  private String host;

  @Value("${spring.data.mongodb.port:27017}")
  private int port;

  @Value("${spring.data.mongodb.database}")
  private String database;

  @Override
  protected String getDatabaseName() {
    return database;
  }

  @Override
  public MongoClient mongoClient() {
    String connectionString = String.format("mongodb://%s:%d/%s", host, port, database);
    System.out.println("MongoDB Connection String: " + connectionString);

    ConnectionString connString = new ConnectionString(connectionString);
    MongoClientSettings settings = MongoClientSettings.builder()
        .applyConnectionString(connString)
        .build();

    return MongoClients.create(settings);
  }
}
