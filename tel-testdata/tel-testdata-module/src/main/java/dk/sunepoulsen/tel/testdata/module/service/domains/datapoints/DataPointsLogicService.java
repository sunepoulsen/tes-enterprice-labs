package dk.sunepoulsen.tel.testdata.module.service.domains.datapoints;

import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSet;
import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSetStatus;
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.DataPointDataSetPersistence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Logic layer for all REST operations.
 */
@Slf4j
@Service
public class DataPointsLogicService {

    private final DataPointDataSetPersistence dataSetPersistence;
    private final DataPointsTransformations transformations;

    /**
     * Spring Boot constructor with injection.
     */
    @Autowired
    public DataPointsLogicService(DataPointDataSetPersistence dataSetPersistence, DataPointsTransformations transformations) {
        this.dataSetPersistence = dataSetPersistence;
        this.transformations = transformations;
    }

    /**
     * Creates a new data set of data points
     *
     * @param dataPointDataSet The data set of data points to create.
     * @return The created data set of data points.
     */
    public DataPointDataSet create(DataPointDataSet dataPointDataSet) {
        dataPointDataSet.setStatus(DataPointDataSetStatus.NEW);
        return transformations.toModel(
            dataSetPersistence.create(
                transformations.toEntity(dataPointDataSet)
            )
        );
    }

    /**
     * Lookup and returns a data set of data points from the persistence layer.
     *
     * @param id Id of the data set of data points to lookup.
     * @return Data set of data points that was found.
     */
    public DataPointDataSet get(Long id) {
        return transformations.toModel(
            dataSetPersistence.get(id)
        );
    }

}
