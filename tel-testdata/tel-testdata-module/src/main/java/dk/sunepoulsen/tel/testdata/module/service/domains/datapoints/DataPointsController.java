package dk.sunepoulsen.tel.testdata.module.service.domains.datapoints;

import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSet;
import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSetStatus;
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.DataPointDataSetPersistence;
import dk.sunepoulsen.tes.springboot.rest.logic.exceptions.LogicException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
            return this.logicService.create(dataPointDataSet);
        } catch (LogicException ex) {
            throw ex.mapApiException();
        }
    }
}
