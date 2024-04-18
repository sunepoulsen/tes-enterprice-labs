package dk.sunepoulsen.tel.testdata.module.service.domains.persistence;

import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointDataSetEntity;
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointsDataSetStatusType;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DataPointDataSetPersistence {

    private final DataPointDataSetRepository repository;

    public DataPointDataSetPersistence(DataPointDataSetRepository repository) {
        this.repository = repository;
    }

    public DataPointDataSetEntity create(DataPointDataSetEntity entity) {
        return this.repository.save(entity);
    }

    public Optional<DataPointDataSetEntity> get(Long id) {
        return this.repository.findById(id);
    }

    @Transactional
    public void updateStatus(Long dataSetId, DataPointsDataSetStatusType newStatus) {
        repository.findAndLockById(dataSetId).ifPresent(entity -> {
            entity.setStatus(newStatus);
            repository.save(entity);
        });
    }
}
