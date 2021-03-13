package ba.unsa.etf.nwt.taskservice.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "priorities")
public class Priority {

    public enum PriorityType { CRITICAL, HIGH, MEDIUM, LOW }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private PriorityType priorityType;
}
