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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class DataPointsLogicService {

    private final DataPointDataSetPersistence dataSetPersistence;
    private final DataPointPersistence dataPointPersistence;
    private final DataPointsTransformations transformations;

    @Autowired
    public DataPointsLogicService(DataPointDataSetPersistence dataSetPersistence, DataPointPersistence dataPointPersistence, DataPointsTransformations transformations) {
        this.dataSetPersistence = dataSetPersistence;
        this.dataPointPersistence = dataPointPersistence;
        this.transformations = transformations;
    }

    public DataPointDataSet create(DataPointDataSet dataPointDataSet) {
        dataPointDataSet.setStatus(DataPointDataSetStatus.NEW);
        return transformations.toModel(
            dataSetPersistence.create(
                transformations.toEntity(dataPointDataSet)
            )
        );
    }

    @Async
    public void generateDataPoints(DataPointDataSet dataSet) {
        try {
            this.dataSetPersistence.updateStatus(dataSet.getId(), DataPointsDataSetStatusType.IN_PROGRESS);

            RangeModel<Integer> quantity = dataSet.getConstraints().getQuantity();
            RangeModel<BigDecimal> xConstraints = dataSet.getConstraints().getXValues();
            RangeModel<BigDecimal> yConstraints = dataSet.getConstraints().getYValues();

            dataSetPersistence.get(dataSet.getId()).ifPresent(dataPointDataSetEntity -> {
                Random random = new Random();
                List<DataPointEntity> entities = new ArrayList<>();

                for (int i = 0; i < random.nextInt(quantity.getMin(), quantity.getMax() + 1); i++) {
                    DataPointEntity entity = new DataPointEntity();
                    entity.setX(random.nextDouble(
                        xConstraints.getMin().doubleValue(),
                        xConstraints.getMax().doubleValue())
                    );
                    entity.setY(random.nextDouble(
                        yConstraints.getMin().doubleValue(),
                        yConstraints.getMax().doubleValue())
                    );

                    entities.add(entity);
                }

                log.debug("Storing {} new data points", entities.size());
                this.dataPointPersistence.createDataPoints(dataSet.getId(), entities);

                this.dataSetPersistence.updateStatus(dataSet.getId(), DataPointsDataSetStatusType.COMPLETED);
            });
        } catch (PersistenceException ex) {
            log.warn("Unable to generate data points: {}", ex.getMessage(), ex);
        }
    }

}
