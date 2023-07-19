package org.projectx.api.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Document("measurements")
public class Measurement {
    @Id
    private String id;

    private String device;

    private LocalDateTime timestamp;

    private Map<String, String> values;
}
