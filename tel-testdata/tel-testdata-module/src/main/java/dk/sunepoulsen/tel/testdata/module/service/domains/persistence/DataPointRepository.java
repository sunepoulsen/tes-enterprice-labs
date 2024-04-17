package dk.sunepoulsen.tel.testdata.module.service.domains.persistence;

import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface DataPointRepository extends PagingAndSortingRepository<DataPointEntity, Long>, ListCrudRepository<DataPointEntity, Long> {

    @Query("SELECT p FROM DataPointEntity p WHERE p.dataSet.id = :id")
    List<DataPointEntity> findDataPoints(@Param("id") Long dataSetId);

    @Query("SELECT COUNT(p) FROM DataPointEntity p WHERE p.dataSet.id = :id")
    Long countDataPoints(@Param("id") Long dataSetId);
}
