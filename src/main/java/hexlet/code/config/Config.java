package hexlet.code.config;

public interface Config {
    String baseUrl();
    String username();
    String password();

    static Config get() {
        String environment = System.getProperty("test.env", "local");
        if ("local".equals(environment)) {
            return LocalConfig.INSTANCE;
        } else {
            throw new IllegalArgumentException("Can't find config with given env: " + environment);
        }
    }
}
