package dk.sunepoulsen.tel.testdata.module.service.domains.datapoints;

import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSet;
import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSetStatus;
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.DataPointDataSetPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataPointsLogicService {

    private final DataPointDataSetPersistence persistence;
    private final DataPointsTransformations transformations;

    @Autowired
    public DataPointsLogicService(DataPointDataSetPersistence persistence, DataPointsTransformations transformations) {
        this.persistence = persistence;
        this.transformations = transformations;
    }

    public DataPointDataSet create(DataPointDataSet dataPointDataSet) {
        dataPointDataSet.setStatus(DataPointDataSetStatus.NEW);
        return transformations.toModel(
            persistence.create(
                transformations.toEntity(dataPointDataSet)
            )
        );
    }

}
