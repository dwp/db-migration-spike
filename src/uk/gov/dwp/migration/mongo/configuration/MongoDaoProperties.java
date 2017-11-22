package uk.gov.dwp.migration.mongo.configuration;

import com.mongodb.MongoClientOptions;
import uk.gov.dwp.common.mongo.codec.MongoCodecSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MongoDaoProperties {

    private List<MongoServerAddress> serverAddresses = new ArrayList<>();
    private String dbName;
    private Collection collection = new Collection();
    private MongoOptions options = new MongoOptions();

    public List<MongoServerAddress> getServerAddresses() {
        return serverAddresses;
    }

    public void setServerAddresses(List<MongoServerAddress> serverAddresses) {
        this.serverAddresses = serverAddresses;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public Collection getCollection() {
        return collection;
    }

    public void setCollection(Collection collection) {
        this.collection = collection;
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

        private String name;
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
}
