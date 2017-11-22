package uk.gov.dwp.personal.details.server.dao.mongo.spring;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dwp.common.mongo.codec.MongoCodecSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mongodb.MongoCredential.createScramSha1Credential;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;

public class MongoDaoProperties {

    private static Logger LOGGER = LoggerFactory.getLogger(MongoDaoProperties.class);

    private List<MongoServerAddress> serverAddresses = new ArrayList<>();
    private Optional<String> dbName = Optional.empty();
    private Collection personalDetails = new Collection();
    private MongoOptions options = new MongoOptions();

    public List<MongoServerAddress> getServerAddresses() {
        return serverAddresses;
    }

    public void setServerAddresses(List<MongoServerAddress> serverAddresses) {
        this.serverAddresses = serverAddresses;
    }

    public String getDbName() {
        return dbName.orElse("personal-details");
    }

    public void setDbName(String dbName) {
        this.dbName = ofNullable(dbName);
    }

    public Collection getPersonalDetails() {
        return personalDetails;
    }

    public void setPersonalDetails(Collection personalDetails) {
        this.personalDetails = personalDetails;
    }

    public MongoOptions getOptions() {
        return options;
    }

    public void setOptions(MongoOptions options) {
        this.options = options;
    }

    public MongoClientOptions mongoClientOptions() {
        return new MongoClientOptions.Builder()
                .sslEnabled(options.ssl.enabled)
                .sslInvalidHostNameAllowed(options.ssl.invalidHostnameAllowed)
                .codecRegistry(MongoCodecSupport.createCodecRegistry())
                .build();
    }

    public static class Collection {

        private String name = "personalDetails";
        private Optional<String> username = Optional.empty();
        private Optional<String> password = Optional.empty();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Optional<String> getUsername() {
            return username;
        }

        public void setUsername(Optional<String> username) {
            this.username = username;
        }

        public Optional<String> getPassword() {
            return password;
        }

        public void setPassword(Optional<String> password) {
            this.password = password;
        }
    }

    public static class MongoServerAddress {
        private String host;
        private Integer port;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }
    }

    public static class MongoOptions {

        private SSL ssl = new SSL();

        public SSL getSsl() {
            return ssl;
        }

        public void setSsl(SSL ssl) {
            this.ssl = ssl;
        }

        public static class SSL {
            private boolean enabled;
            private boolean invalidHostnameAllowed;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public boolean isInvalidHostnameAllowed() {
                return invalidHostnameAllowed;
            }

            public void setInvalidHostnameAllowed(boolean invalidHostnameAllowed) {
                this.invalidHostnameAllowed = invalidHostnameAllowed;
            }
        }
    }

    public List<ServerAddress> createSeeds() {
        return serverAddresses
                .stream()
                .map(serverAddress -> new ServerAddress(serverAddress.getHost(), serverAddress.getPort()))
                .peek(serverAddress -> LOGGER.debug("Adding {} as a seed server for Mongo", serverAddress))
                .collect(Collectors.toList());
    }

    public List<MongoCredential> createCredentials() {
        return personalDetails.getUsername()
                .map(username -> singletonList(createScramSha1Credential(
                        username,
                        getDbName(),
                        personalDetails
                                .getPassword()
                                .orElseThrow(() -> new IllegalArgumentException("Password is required when username specified"))
                                .toCharArray())))
                .orElse(Collections.emptyList());
    }

}
