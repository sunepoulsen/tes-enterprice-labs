package dk.sunepoulsen.tel.testdata.module.integrator.model;

import dk.sunepoulsen.tes.rest.models.BaseModel;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "Data Point", description = "Data point of x and y values")
public class DataPoint implements BaseModel {
    @NotNull
    @Parameter(description = "X coordinate", required = true)
    private Double x;

    @NotNull
    @Parameter(description = "Y coordinate", required = true)
    private Double y;
}
