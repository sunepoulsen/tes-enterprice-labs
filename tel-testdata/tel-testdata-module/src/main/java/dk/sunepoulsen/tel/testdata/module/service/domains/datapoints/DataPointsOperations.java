package dk.sunepoulsen.tel.testdata.module.service.domains.datapoints;

import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSet;
import dk.sunepoulsen.tes.rest.models.ServiceErrorModel;
import dk.sunepoulsen.tes.rest.models.ServiceValidationErrorModel;
import dk.sunepoulsen.tes.rest.models.validation.annotations.OnCrudCreate;
import dk.sunepoulsen.tes.rest.models.validation.annotations.OnCrudRead;
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
import org.springframework.web.bind.annotation.*;

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
    DataPointDataSet create(@Valid @RequestBody DataPointDataSet dataPointDataSet);

    @Operation(
        summary = "Get data set of data points",
        description = "Returns a data set of data points by its id"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully returned",
            content = @Content(
                schema = @Schema(implementation = DataPointDataSet.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request because of bad id",
            content = @Content(
                schema = @Schema(implementation = ServiceErrorModel.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "No data set of data points exists with the given id",
            content = @Content(
                schema = @Schema(implementation = ServiceErrorModel.class)
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
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Validated({Default.class, OnCrudRead.class})
    DataPointDataSet get(@Valid @PathVariable("id") Long id);
}
