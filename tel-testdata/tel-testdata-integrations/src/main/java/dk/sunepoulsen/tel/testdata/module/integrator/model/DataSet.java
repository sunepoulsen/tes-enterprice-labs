package dk.sunepoulsen.tel.testdata.module.integrator.model;

import dk.sunepoulsen.tes.rest.models.BaseModel;
import dk.sunepoulsen.tes.rest.models.validation.annotations.OnCrudCreate;
import dk.sunepoulsen.tes.rest.models.validation.annotations.OnCrudRead;
import dk.sunepoulsen.tes.rest.models.validation.annotations.OnCrudUpdate;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;

@Data
@Schema(name = "Dataset", description = "Defines a data set")
public class DataSet implements BaseModel {
    @Null(groups = {OnCrudCreate.class, OnCrudUpdate.class})
    @NotNull(groups = {OnCrudRead.class})
    @Parameter(description = "Unique id of the data set", required = true)
    private Long id;

    @NotNull(groups = {OnCrudCreate.class, OnCrudRead.class})
    @Parameter(description = "Name of the data set", required = true)
    private String name;

    @NotNull(groups = {OnCrudCreate.class, OnCrudRead.class})
    @Parameter(description = "Description of the data set", required = true)
    private String description;
}
