package dk.sunepoulsen.tel.testdata.module.service.domains.persistence;

import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointDataSetEntity;
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointsDataSetStatusType;
import dk.sunepoulsen.tes.springboot.rest.logic.exceptions.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Spring Boot service for the persistence layer of data sets
 * of data points.
 */
@Service
public class DataPointDataSetPersistence {

    private final DataPointDataSetRepository repository;

    /**
     * Spring Boot constructor with injection.
     */
    public DataPointDataSetPersistence(DataPointDataSetRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates a new data set of data points.
     *
     * @param entity Entity of data set of data points to create.
     * @return The created entity of data set of data points.
     */
    public DataPointDataSetEntity create(DataPointDataSetEntity entity) {
        return this.repository.save(entity);
    }

    /**
     * Lookup and returns a data set of data points in the persistence storage.
     *
     * @param id Id of the data set of data points to lookup.
     * @return Entity of the data set of data points that was found.
     * @throws ResourceNotFoundException If no data set of data points exists
     *                                   with the given {@code id}
     */
    public DataPointDataSetEntity get(Long id) throws ResourceNotFoundException {
        Optional<DataPointDataSetEntity> optionalEntity = this.repository.findById(id);
        if (optionalEntity.isEmpty()) {
            throw new ResourceNotFoundException("id", "No data sets of data points exists with the given id");
        }

        return optionalEntity.get();
    }

    /**
     * Checks if a data set of data points exists.
     *
     * @param id Id of the data set of data points to lookup.
     */
    public boolean exists(Long id) {
        return this.repository.existsById(id);
    }

    /**
     * Updates the status of a data set of data points.
     * <p>
     *      The update is performed in a new transaction and the updated row
     *      is locked in the process.
     * </p>
     *
     * @param dataSetId Id of the data set of data points to update.
     * @param newStatus The new status.
     */
    @Transactional
    public void updateStatus(Long dataSetId, DataPointsDataSetStatusType newStatus) {
        repository.findAndLockById(dataSetId).ifPresent(entity -> {
            entity.setStatus(newStatus);
            repository.save(entity);
        });
    }
}
