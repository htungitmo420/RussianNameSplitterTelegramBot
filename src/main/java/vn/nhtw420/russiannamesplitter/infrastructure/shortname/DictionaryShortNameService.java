package vn.nhtw420.russiannamesplitter.infrastructure.shortname;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import vn.nhtw420.russiannamesplitter.application.port.ShortNamePort;

import java.io.InputStream;
import java.util.*;

@Component
public class DictionaryShortNameService implements ShortNamePort {

    private static final String DATA_PATH = "data/russian-short-names.yml";

    private final Map<String, List<String>> dict;

    public DictionaryShortNameService() {
        this.dict = Collections.unmodifiableMap(loadDict());
    }

    @Override
    public List<String> suggestShortNames(String givenName) {
        if (givenName == null || givenName.trim().isEmpty()) {
            return List.of();
        }

        String normalizedName = normalizeKey(givenName);
        return dict.getOrDefault(normalizedName, List.of());
    }

    private Map<String, List<String>> loadDict() {
        try (InputStream is = new ClassPathResource(DATA_PATH).getInputStream()) {
            Yaml yaml = new Yaml();
            Object obj = yaml.load(is);

            if (!(obj instanceof Map<?, ?> map)) {
                return Map.of();
            }

            Map<String, List<String>> out = new HashMap<>();
            for (Map.Entry<?, ?> e : map.entrySet()) {
                if (!(e.getKey() instanceof String keyRaw)) continue;

                List<String> values = toStringList(e.getValue());
                if (values.isEmpty()) continue;

                out.put(normalizeKey(keyRaw), List.copyOf(values));
            }

            return out;
        } catch (Exception ex) {
            return Map.of();
        }
    }

    private static List<String> toStringList(Object value) {
        if (value == null) return List.of();

        if (value instanceof List<?> xs) {
            List<String> out = new ArrayList<>();
            for (Object x : xs) {
                if (x == null) continue;
                String s = x.toString().trim();
                if (!s.isEmpty()) out.add(s);
            }
            return out;
        }

        // allow scalar string
        String s = value.toString().trim();
        return s.isEmpty() ? List.of() : List.of(s);
    }

    private static String normalizeKey(String s) {
        String t = s.trim().toLowerCase(Locale.ROOT);
        // for lookup convenience: treat ё as е
        t = t.replace('ё', 'е');
        return t;
    }
}
