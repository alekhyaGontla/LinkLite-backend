package com.linklite.entity;

import jakarta.persistence.*;
<<<<<<< HEAD

import java.time.LocalDateTime;

@Entity
@Table(name = "activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;

    @Column(length = 1000)
    private String description;

    private LocalDateTime createdAt;

    public Activity() {
    }

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
}
=======
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
>>>>>>> 07dc98d6affa2ec4f698256083be4284d7bddb38
