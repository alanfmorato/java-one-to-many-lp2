package com.example.entity;

import java.time.Instant;

public abstract class BaseEntity {
    protected Long id;
    protected Instant createdAt;
    protected Instant updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void touchCreated() { this.createdAt = Instant.now(); }
    public void touchUpdated() { this.updatedAt = Instant.now(); }
}
