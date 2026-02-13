package qbsss.docsCenter.docsCenterGradleGenerated.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VariableService {

    private final Map<String, Object> variables = new ConcurrentHashMap<>();

    public void set(String key, Object value) {
        variables.put(key, value);
    }

    public Object get(String key) {
        return variables.get(key);
    }

    public Map<String, Object> getAll() {
        return variables;
    }
}