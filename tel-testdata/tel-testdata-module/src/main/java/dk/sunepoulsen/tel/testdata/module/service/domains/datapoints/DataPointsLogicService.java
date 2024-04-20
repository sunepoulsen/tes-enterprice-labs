package dk.sunepoulsen.tel.testdata.module.service.domains.datapoints;

import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSet;
import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSetStatus;
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.DataPointDataSetPersistence;
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.DataPointPersistence;
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointEntity;
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointsDataSetStatusType;
import dk.sunepoulsen.tes.rest.models.RangeModel;
import dk.sunepoulsen.tes.springboot.rest.logic.exceptions.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Logic layer for all REST operations.
 */
@Slf4j
@Service
public class DataPointsLogicService {

    private final DataPointDataSetPersistence dataSetPersistence;
    private final DataPointPersistence dataPointPersistence;
    private final DataPointsTransformations transformations;
    private final Random rand;

    /**
     * Spring Boot constructor with injection.
     */
    @Autowired
    public DataPointsLogicService(DataPointDataSetPersistence dataSetPersistence, DataPointPersistence dataPointPersistence, DataPointsTransformations transformations) throws NoSuchAlgorithmException {
        this.dataSetPersistence = dataSetPersistence;
        this.dataPointPersistence = dataPointPersistence;
        this.transformations = transformations;
        this.rand = SecureRandom.getInstanceStrong();
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

    /**
     * Generates all data points for a data set of data points.
     * <p>
     * This operation is completed asynchronously.
     * </p>
     *
     * @param dataSet The data set to generate data points for.
     */
    @Async
    public void generateDataPoints(DataPointDataSet dataSet) {
        try {
            this.dataSetPersistence.updateStatus(dataSet.getId(), DataPointsDataSetStatusType.IN_PROGRESS);

            RangeModel<Integer> quantity = dataSet.getConstraints().getQuantity();
            RangeModel<BigDecimal> xConstraints = dataSet.getConstraints().getXValues();
            RangeModel<BigDecimal> yConstraints = dataSet.getConstraints().getYValues();

            if (!dataSetPersistence.exists(dataSet.getId())) {
                log.warn("Unable to generate data points of the dataset {} because it does not exist", dataSet.getId());
                return;
            }

            List<DataPointEntity> entities = new ArrayList<>();

            for (int i = 0; i < this.rand.nextInt(quantity.getMin(), quantity.getMax() + 1); i++) {
                DataPointEntity entity = new DataPointEntity();
                entity.setX(this.rand.nextDouble(
                    xConstraints.getMin().doubleValue(),
                    xConstraints.getMax().doubleValue())
                );
                entity.setY(this.rand.nextDouble(
                    yConstraints.getMin().doubleValue(),
                    yConstraints.getMax().doubleValue())
                );

                entities.add(entity);
            }

            log.debug("Storing {} new data points", entities.size());
            this.dataPointPersistence.createDataPoints(dataSet.getId(), entities);

            this.dataSetPersistence.updateStatus(dataSet.getId(), DataPointsDataSetStatusType.COMPLETED);
        } catch (PersistenceException ex) {
            log.warn("Unable to generate data points: {}", ex.getMessage(), ex);
        }
    }

}
