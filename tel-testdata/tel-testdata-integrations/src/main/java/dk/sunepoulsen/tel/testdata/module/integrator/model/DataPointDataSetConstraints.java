package dk.sunepoulsen.tel.testdata.module.integrator.model;

import dk.sunepoulsen.tes.rest.models.BaseModel;
import dk.sunepoulsen.tes.rest.models.RangeModel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(name = "Data Point Constraints", description = "Constraints that defines how to generate data for a dataset")
public class DataPointDataSetConstraints implements BaseModel {
    @Schema(
        description = "Range of the x values, when generating the data set",
        requiredMode = Schema.RequiredMode.REQUIRED,
        accessMode = Schema.AccessMode.READ_WRITE
    )
    @NotNull
    @Valid
    private RangeModel<BigDecimal> xValues;

    @Schema(
        description = "Range of the y values, when generating the data set",
        requiredMode = Schema.RequiredMode.REQUIRED,
        accessMode = Schema.AccessMode.READ_WRITE
    )
    @NotNull
    @Valid
    private RangeModel<BigDecimal> yValues;

    @Schema(
        description = "Range of the number of data points in the data set",
        requiredMode = Schema.RequiredMode.REQUIRED,
        accessMode = Schema.AccessMode.READ_WRITE
    )
    @NotNull
    @Valid
    private RangeModel<Integer> quantity;
}
