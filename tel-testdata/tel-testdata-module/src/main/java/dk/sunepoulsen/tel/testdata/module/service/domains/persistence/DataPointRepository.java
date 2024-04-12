package dk.sunepoulsen.tel.testdata.module.service.domains.persistence;

import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

interface DataPointRepository extends PagingAndSortingRepository<DataPointEntity, Long>, ListCrudRepository<DataPointEntity, Long> {
}
