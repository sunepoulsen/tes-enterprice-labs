package dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * The JPA entity of the dataset table in the database.
 * <p>
 *     The entity uses the <code>dataset_id_seq</code> sequence to generate primary keys.
 * </p>
 */
@Entity
@Table( name = "datasets" )
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class DataSetEntity extends TimestampEntity {

    private static final String SEQUENCE_NAME = "dataset_id_seq";

    /**
     * Primary key.
     */
    @Id
    @SequenceGenerator( name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 1 )
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME )
    @Column( name = "dataset_id" )
    private Long id;

    @Column( name = "type", nullable = false )
    @Enumerated( EnumType.STRING )
    private DataSetType type;

    @Column( name = "name", nullable = false )
    private String name;

    @Column( name = "description", nullable = false )
    private String description;

}
