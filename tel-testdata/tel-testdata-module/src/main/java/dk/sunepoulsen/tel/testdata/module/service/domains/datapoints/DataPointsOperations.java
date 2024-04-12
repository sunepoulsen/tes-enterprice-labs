package dk.sunepoulsen.tel.testdata.module.service.domains.datapoints;

import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSet;
import dk.sunepoulsen.tes.rest.models.ServiceErrorModel;
import dk.sunepoulsen.tes.rest.models.ServiceValidationErrorModel;
import dk.sunepoulsen.tes.rest.models.validation.annotations.OnCrudCreate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name="Data Points", description = "Endpoints to manage data sets with X- and Y-coordinates")
@RequestMapping(DataPointsOperations.DATASETS_DATA_POINTS_ENDPOINT_PATH)
@Validated
interface DataPointsOperations {

    String DATASETS_DATA_POINTS_ENDPOINT_PATH = "/datasets/data-points";

    @Operation(
        summary = "Create data set of data points",
        description = """
            Accepts the creation of a data set of data points and begins the creation
            of the data set in the background.
        """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "202",
            description = "Successfully accepted",
            content = @Content(
                schema = @Schema(implementation = DataPointDataSet.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request because of validation errors",
            content = @Content(
                schema = @Schema(implementation = ServiceValidationErrorModel.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Unable to process this request",
            content = @Content(
                schema = @Schema(implementation = ServiceErrorModel.class)
            )
        )
    })
    @PostMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Validated({Default.class, OnCrudCreate.class})
    DataPointDataSet createDataPointDataSet(@Valid @RequestBody DataPointDataSet dataPointDataSet);
}
