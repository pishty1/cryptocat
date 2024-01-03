package com.cryptocat.web4;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String minerHash;

    @Column(name = "created", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    protected Block() {
    }

    public Block(Long id, String hash, Date created) {
        this.minerHash = hash;
        this.created = created;
        this.id = id;
    }

    public Block(String hash) {
        this.minerHash = hash;
        this.created = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMinerHash() {
        return minerHash;
    }

    public void setMinerHash(String minerHash) {
        this.minerHash = minerHash;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}