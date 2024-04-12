package dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * The JPA entity of the dataset table in the database.
 * <p>
 *     The entity uses the <code>dataset_id_seq</code> sequence to generate primary keys.
 * </p>
 */
@Entity
@Table( name = "data_points" )
@Data
@EqualsAndHashCode( callSuper = false )
public class DataPointEntity extends TimestampEntity {

    private static final String SEQUENCE_NAME = "datapoint_id_seq";

    /**
     * Primary key.
     */
    @Id
    @SequenceGenerator( name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 1 )
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME )
    @Column( name = "datapoint_id" )
    private Long id;

    @ManyToOne( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    @JoinColumn(name = "dataset_id", nullable = false)
    private DataPointDataSetEntity dataSet;

    @Column(name="x", nullable = false)
    private Double x;

    @Column(name="y", nullable = false)
    private Double y;
}
