package dk.sunepoulsen.tel.testdata.module.service.domains.persistence;

import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointDataSetEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

interface DataPointDataSetRepository extends PagingAndSortingRepository<DataPointDataSetEntity, Long>, ListCrudRepository<DataPointDataSetEntity, Long> {

    @Query("SELECT d FROM DataPointDataSetEntity d WHERE d.id = :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<DataPointDataSetEntity> findAndLockById(Long id);

}
