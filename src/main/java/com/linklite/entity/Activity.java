package com.linklite.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity")
public class Activity {

```
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private String action;

private String description;

private LocalDateTime createdAt;

public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}

public String getAction() {
    return action;
}

public void setAction(String action) {
    this.action = action;
}

public String getDescription() {
    return description;
}

public void setDescription(String description) {
    this.description = description;
}

public LocalDateTime getCreatedAt() {
    return createdAt;
}

public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
}
```

}
