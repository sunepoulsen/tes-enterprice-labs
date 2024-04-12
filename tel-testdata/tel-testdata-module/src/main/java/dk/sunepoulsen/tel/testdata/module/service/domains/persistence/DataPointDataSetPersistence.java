package dk.sunepoulsen.tel.testdata.module.service.domains.persistence;

import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointDataSetEntity;
import org.springframework.stereotype.Service;

@Service
public class DataPointDataSetPersistence {

    private final DataPointDataSetRepository repository;

    public DataPointDataSetPersistence(DataPointDataSetRepository repository) {
        this.repository = repository;
    }

    public DataPointDataSetEntity create(DataPointDataSetEntity entity) {
        return this.repository.save(entity);
    }

}
