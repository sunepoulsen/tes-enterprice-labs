package dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * The JPA entity of the dataset table in the database.
 * <p>
 *     The entity uses the <code>dataset_id_seq</code> sequence to generate primary keys.
 * </p>
 */
@Entity
@Table( name = "datapoint_datasets" )
@Data
@EqualsAndHashCode(callSuper = true)
@ToString( callSuper = true )
public class DataPointDataSetEntity extends DataSetEntity {

    @Column(name="x_min", nullable = false)
    private Double minX;

    @Column(name="x_max", nullable = false)
    private Double maxX;

    @Column(name="y_min", nullable = false)
    private Double minY;

    @Column(name="y_max", nullable = false)
    private Double maxY;

    @Column(name="quantity_min", nullable = false)
    private Integer minQuantity;

    @Column(name="quantity_max", nullable = false)
    private Integer maxQuantity;

    @Column(name="status", nullable = false)
    @Enumerated( EnumType.STRING )
    private DataPointsDataSetStatusType status;

    @OneToMany(mappedBy = "dataSet", cascade = CascadeType.ALL)
    private List<DataPointEntity> dataPoints;

    @PrePersist
    @PreUpdate
    private void updateType() {
        this.setType(DataSetType.DATA_POINTS);
    }
}
