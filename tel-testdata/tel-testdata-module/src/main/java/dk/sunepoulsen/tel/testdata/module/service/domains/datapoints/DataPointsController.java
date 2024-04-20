package dk.sunepoulsen.tel.testdata.module.service.domains.datapoints;

import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSet;
import dk.sunepoulsen.tes.springboot.rest.logic.exceptions.LogicException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(DataPointsOperations.DATASETS_DATA_POINTS_ENDPOINT_PATH)
class DataPointsController implements DataPointsOperations {

    private final DataPointsLogicService logicService;
    private final DataPointsGeneratorService dataPointsGeneratorService;

    @Autowired
    public DataPointsController(DataPointsLogicService logicService, DataPointsGeneratorService dataPointsGeneratorService) {
        this.logicService = logicService;
        this.dataPointsGeneratorService = dataPointsGeneratorService;
    }

    @PostMapping
    public DataPointDataSet create(DataPointDataSet dataPointDataSet) {
        try {
            DataPointDataSet result = this.logicService.create(dataPointDataSet);
            this.dataPointsGeneratorService.generateDataPoints(result);
            return result;
        } catch (LogicException ex) {
            throw ex.mapApiException();
        }
    }

    @GetMapping("/{id}")
    public DataPointDataSet get(@Valid @PathVariable("id") Long id) {
        try {
            return logicService.get(id);
        } catch (LogicException ex) {
            throw ex.mapApiException();
        }
    }

}
