package dk.sunepoulsen.tel.testdata.module.service.domains.datapoints;

import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSet;
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.DataPointDataSetPersistence;
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.DataPointPersistence;
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointEntity;
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointsDataSetStatusType;
import dk.sunepoulsen.tes.rest.models.RangeModel;
import dk.sunepoulsen.tes.springboot.rest.logic.exceptions.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class DataPointsGeneratorService {

    private final DataPointDataSetPersistence dataSetPersistence;
    private final DataPointPersistence dataPointPersistence;
    private final Random rand;

    public DataPointsGeneratorService(DataPointDataSetPersistence dataSetPersistence, DataPointPersistence dataPointPersistence) throws NoSuchAlgorithmException {
        this.dataSetPersistence = dataSetPersistence;
        this.dataPointPersistence = dataPointPersistence;
        this.rand = SecureRandom.getInstanceStrong();
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
