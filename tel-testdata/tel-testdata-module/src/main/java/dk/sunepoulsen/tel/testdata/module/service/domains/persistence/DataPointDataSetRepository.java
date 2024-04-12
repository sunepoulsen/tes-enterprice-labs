package dk.sunepoulsen.tel.testdata.module.service.domains.persistence;

import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointDataSetEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

interface DataPointDataSetRepository extends PagingAndSortingRepository<DataPointDataSetEntity, Long>, ListCrudRepository<DataPointDataSetEntity, Long> {
}
