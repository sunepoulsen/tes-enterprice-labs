package dk.sunepoulsen.tel.testdata.module.integrator;

import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSet;
import dk.sunepoulsen.tes.rest.integrations.TechEasySolutionsBackendIntegrator;
import dk.sunepoulsen.tes.rest.integrations.TechEasySolutionsClient;
import io.reactivex.rxjava3.core.Single;

public class TelTestDataIntegrator extends TechEasySolutionsBackendIntegrator {

    public TelTestDataIntegrator(TechEasySolutionsClient httpClient) {
        super(httpClient);
    }

    public Single<DataPointDataSet> createDataPointDataSet(DataPointDataSet dataSet) {
        return Single.fromFuture(this.httpClient.post("/datasets/data-points", dataSet, DataPointDataSet.class))
            .onErrorResumeNext(this::mapClientExceptions);
    }

    public Single<DataPointDataSet> get(Long id) {
        return Single.fromFuture(this.httpClient.get("/datasets/data-points/" + id.toString(), DataPointDataSet.class))
            .onErrorResumeNext(this::mapClientExceptions);
    }

}
