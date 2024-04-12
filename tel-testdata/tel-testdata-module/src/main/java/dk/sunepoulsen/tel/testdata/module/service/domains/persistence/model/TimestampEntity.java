package dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Clock;
import java.time.ZonedDateTime;

@MappedSuperclass
@Getter
public abstract class TimestampEntity {

    @Column( name = "create_time", nullable = false, updatable = false )
    @Temporal( TemporalType.TIMESTAMP )
    private ZonedDateTime createDateTime;

    @Column( name = "update_time", nullable = false )
    @Temporal( TemporalType.TIMESTAMP )
    private ZonedDateTime updateDateTime;

    @PrePersist
    private void createTimestamp() {
        this.createDateTime = ZonedDateTime.now(Clock.systemUTC());
        this.updateDateTime = this.createDateTime;
    }

    @PreUpdate
    private void updateTimestamp() {
        this.updateDateTime = ZonedDateTime.now(Clock.systemUTC());
    }

}
