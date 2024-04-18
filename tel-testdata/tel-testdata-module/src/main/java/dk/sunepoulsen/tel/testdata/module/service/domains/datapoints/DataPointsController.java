package dk.sunepoulsen.tel.testdata.module.service.domains.datapoints;

import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSet;
import dk.sunepoulsen.tes.springboot.rest.logic.exceptions.LogicException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(DataPointsOperations.DATASETS_DATA_POINTS_ENDPOINT_PATH)
class DataPointsController implements DataPointsOperations {

    private final DataPointsLogicService logicService;

    @Autowired
    public DataPointsController(DataPointsLogicService logicService) {
        this.logicService = logicService;
    }

    @PostMapping
    public DataPointDataSet createDataPointDataSet(DataPointDataSet dataPointDataSet) {
        try {
            DataPointDataSet result = this.logicService.create(dataPointDataSet);
            this.logicService.generateDataPoints(result);
            return result;
        } catch (LogicException ex) {
            throw ex.mapApiException();
        }
    }
}
