package dk.sunepoulsen.tel.testdata.module.integrator.model;

import dk.sunepoulsen.tes.rest.models.BaseModel;
import dk.sunepoulsen.tes.rest.models.validation.annotations.OnCrudCreate;
import dk.sunepoulsen.tes.rest.models.validation.annotations.OnCrudRead;
import dk.sunepoulsen.tes.rest.models.validation.annotations.OnCrudUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;

@Data
@Schema(name = "Dataset", description = "Defines a data set")
public class DataSet implements BaseModel {
    @Schema(
        description = "Unique id of the data set",
        requiredMode = Schema.RequiredMode.REQUIRED,
        accessMode = Schema.AccessMode.READ_ONLY
    )
    @Null(groups = {OnCrudCreate.class, OnCrudUpdate.class})
    @NotNull(groups = {OnCrudRead.class})
    private Long id;

    @Schema(
        description = "Name of the data set",
        requiredMode = Schema.RequiredMode.REQUIRED,
        accessMode = Schema.AccessMode.READ_WRITE
    )
    @NotNull(groups = {OnCrudCreate.class, OnCrudRead.class})
    private String name;

    @Schema(
        description = "Description of the data set",
        requiredMode = Schema.RequiredMode.REQUIRED,
        accessMode = Schema.AccessMode.READ_WRITE
    )
    @NotNull(groups = {OnCrudCreate.class, OnCrudRead.class})
    private String description;
}
