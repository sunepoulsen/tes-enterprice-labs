package dk.sunepoulsen.tel.testdata.module.integrator;

import dk.sunepoulsen.tes.rest.integrations.TechEasySolutionsBackendIntegrator;
import dk.sunepoulsen.tes.rest.integrations.TechEasySolutionsClient;
import io.reactivex.rxjava3.core.Single;

public class TelTestDataTestsIntegrator extends TechEasySolutionsBackendIntegrator {

    public TelTestDataTestsIntegrator(TechEasySolutionsClient httpClient) {
        super(httpClient);
    }

    public Single<Class<Void>> deletePersistence() {
        return Single.fromFuture(this.httpClient.delete("/tests/persistence"))
            .map(s -> Void.TYPE)
            .onErrorResumeNext(this::mapClientExceptions);
    }

}
