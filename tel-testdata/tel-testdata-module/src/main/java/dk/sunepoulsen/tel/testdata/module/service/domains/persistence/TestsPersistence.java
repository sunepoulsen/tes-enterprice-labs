package dk.sunepoulsen.tel.testdata.module.service.domains.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestsPersistence {

    private final DataPointRepository dataPointRepository;
    private final DataPointDataSetRepository dataPointDataSetRepository;

    @Autowired
    public TestsPersistence(DataPointRepository dataPointRepository, DataPointDataSetRepository dataPointDataSetRepository) {
        this.dataPointRepository = dataPointRepository;
        this.dataPointDataSetRepository = dataPointDataSetRepository;
    }

    public void clearDatabase() {
        dataPointRepository.deleteAll();
        dataPointDataSetRepository.deleteAll();
    }
}
