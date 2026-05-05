package hexlet.code.config;

public enum LocalConfig implements Config {
    INSTANCE;

    @Override
    public String baseUrl() {
        return getEnvOrDefault("APP_BASE_URL", "http://localhost:5173");
    }

    @Override
    public String username() {
        return getEnvOrDefault("USERNAME", "aleksei98");
    }

    @Override
    public String password() {
        return getEnvOrDefault("PASSWORD", "[trcktnghjtrn_98");
    }

    @Override
    public String windowSize() {
        return getEnvOrDefault("WINDOW_SIZE", "1920,1080");
    }

    private String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return value != null ? value : defaultValue;
    }
}
