package dk.sunepoulsen.tel.testdata.module.service.domains.datapoints;

import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSet;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(DataPointsOperations.DATASETS_DATA_POINTS_ENDPOINT_PATH)
public class DataPointsController implements DataPointsOperations {
    @PostMapping
    public DataPointDataSet createDataPointDataSet(DataPointDataSet dataPointDataSet) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
