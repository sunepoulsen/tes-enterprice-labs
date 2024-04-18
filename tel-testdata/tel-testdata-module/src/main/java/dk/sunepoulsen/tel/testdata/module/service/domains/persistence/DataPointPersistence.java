package dk.sunepoulsen.tel.testdata.module.service.domains.persistence;

import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointEntity;
import dk.sunepoulsen.tes.springboot.rest.logic.exceptions.PersistenceException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataPointPersistence {

    private final DataPointRepository repository;
    private final DataPointDataSetRepository dataSetRepository;

    public DataPointPersistence(DataPointRepository repository, DataPointDataSetRepository dataSetRepository) {
        this.repository = repository;
        this.dataSetRepository = dataSetRepository;
    }

    @Transactional
    public void createDataPoints(Long dataSetId, List<DataPointEntity> dataPoints) {
        if (dataPoints.isEmpty()) {
            return;
        }

        dataSetRepository.findById(dataSetId).ifPresent(dataSetEntity -> {
            dataPoints.forEach(entity -> {
                if (dataSetEntity.getMinX() > entity.getX()) {
                    throw new PersistenceException("X value is smaller than constraints");
                }

                if (dataSetEntity.getMaxX() < entity.getX()) {
                    throw new PersistenceException("X value is bigger than constraints");
                }

                if (dataSetEntity.getMinY() > entity.getY()) {
                    throw new PersistenceException("Y value is smaller than constraints");
                }

                if (dataSetEntity.getMaxY() < entity.getY()) {
                    throw new PersistenceException("Y value is bigger than constraints");
                }

                entity.setDataSet(dataSetEntity);
            });

            Long numberOfDataPoints = repository.countDataPoints(dataSetEntity.getId());
            if (dataSetEntity.getMaxQuantity() - numberOfDataPoints < dataPoints.size()) {
                throw new PersistenceException("Too many data points");
            }

            repository.saveAll(dataPoints);
        });
    }

}
