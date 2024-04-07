package dk.sunepoulsen.tel.testdata.module.integrator.model;

import dk.sunepoulsen.tes.rest.models.BaseModel;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "Data Point", description = "Data point of x and y values")
public class DataPoint implements BaseModel {
    @Schema(
        description = "X-coordinate of a data point",
        requiredMode = Schema.RequiredMode.REQUIRED,
        accessMode = Schema.AccessMode.READ_WRITE
    )
    @NotNull
    private Double x;

    @Schema(
        description = "Y-coordinate of a data point",
        requiredMode = Schema.RequiredMode.REQUIRED,
        accessMode = Schema.AccessMode.READ_WRITE
    )
    @NotNull
    private Double y;
}
