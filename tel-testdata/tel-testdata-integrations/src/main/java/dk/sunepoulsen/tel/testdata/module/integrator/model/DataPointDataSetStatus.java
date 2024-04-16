package dk.sunepoulsen.tel.testdata.module.integrator.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    description = "Status the generation of data points in the dataset",
    requiredMode = Schema.RequiredMode.NOT_REQUIRED,
    accessMode = Schema.AccessMode.READ_ONLY
)
public enum DataPointDataSetStatus {
    @Schema(
        description = "The dataset has been created, but not data points has been created yet"
    )
    NEW,

    @Schema(
        description = "The dataset has been created and the creation of data points is in progress"
    )
    IN_PROGRESS,

    @Schema(
        description = "All data points in the dataset has been created"
    )
    COMPLETED
}
