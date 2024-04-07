package dk.sunepoulsen.tel.testdata.module.integrator.model;

import dk.sunepoulsen.tes.rest.models.PaginationModel;
import dk.sunepoulsen.tes.rest.models.validation.annotations.OnCrudCreate;
import dk.sunepoulsen.tes.rest.models.validation.annotations.OnCrudRead;
import dk.sunepoulsen.tes.rest.models.validation.annotations.OnCrudUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(name = "Data Points", description = "Defines a data set of data points")
public class DataPointDataSet extends DataSet {
    @Null(groups = {OnCrudCreate.class, OnCrudUpdate.class})
    @NotNull(groups = {OnCrudRead.class})
    @Valid
    private PaginationModel<DataPoint> dataPoints;

    @NotNull(groups = {OnCrudCreate.class, OnCrudRead.class})
    @Valid
    private DataPointDataSetConstraints creationConstraints;
}
