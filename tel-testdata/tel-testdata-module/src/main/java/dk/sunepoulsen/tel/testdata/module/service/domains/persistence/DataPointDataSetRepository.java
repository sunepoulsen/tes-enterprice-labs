package dk.sunepoulsen.tel.testdata.module.service.domains.persistence;

import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointDataSetEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

interface DataPointDataSetRepository extends PagingAndSortingRepository<DataPointDataSetEntity, Long>, ListCrudRepository<DataPointDataSetEntity, Long> {

    /**
     * Find and locks a data set of data points.
     * <p>
     * The lock is released on commit or rollback.
     * </p>
     *
     * @param id Id of the data set of data points.
     * @return An optional of the found data set of data points.
     */
    @Query("SELECT d FROM DataPointDataSetEntity d WHERE d.id = :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<DataPointDataSetEntity> findAndLockById(Long id);

}
