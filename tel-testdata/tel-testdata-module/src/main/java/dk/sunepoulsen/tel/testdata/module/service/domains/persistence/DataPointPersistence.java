package dk.sunepoulsen.tel.testdata.module.service.domains.persistence;

import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointDataSetEntity;
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointEntity;
import dk.sunepoulsen.tes.springboot.rest.logic.exceptions.PersistenceException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataPointPersistence {

    private final DataPointRepository repository;

    public DataPointPersistence(DataPointRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void createDataPoints(List<DataPointEntity> dataPoints) {
        if (dataPoints.isEmpty()) {
            return;
        }

        final DataPointDataSetEntity dataSetEntity = dataPoints.getFirst().getDataSet();
        dataPoints.forEach(entity -> {
            if (dataSetEntity != entity.getDataSet()) {
                throw new PersistenceException("All data points must belong to the same data set");
            }

            if (entity.getDataSet().getMinX() > entity.getX()) {
                throw new PersistenceException("X value is smaller than constraints");
            }

            if (entity.getDataSet().getMaxX() < entity.getX()) {
                throw new PersistenceException("X value is bigger than constraints");
            }

            if (entity.getDataSet().getMinY() > entity.getY()) {
                throw new PersistenceException("Y value is smaller than constraints");
            }

            if (entity.getDataSet().getMaxY() < entity.getY()) {
                throw new PersistenceException("Y value is bigger than constraints");
            }
        });

        Long numberOfDataPoints = repository.countDataPoints(dataPoints.getFirst().getId());
        if (dataPoints.getFirst().getDataSet().getMaxQuantity() - numberOfDataPoints < dataPoints.size()) {
            throw new PersistenceException("Too many data points");
        }

        repository.saveAll(dataPoints);
    }

}
