package dk.sunepoulsen.tel.testdata.module.integrator

import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSet
import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSetConstraints
import dk.sunepoulsen.tes.rest.integrations.TechEasySolutionsClient
import dk.sunepoulsen.tes.rest.integrations.exceptions.ClientBadRequestException
import dk.sunepoulsen.tes.rest.integrations.exceptions.ClientNotFoundException
import dk.sunepoulsen.tes.rest.models.RangeModel
import dk.sunepoulsen.tes.rest.models.ServiceErrorModel
import spock.lang.Specification

import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException

class TelTestDataIntegratorSpec extends Specification {

    private TechEasySolutionsClient httpClient
    private TelTestDataIntegrator sut

    void setup() {
        this.httpClient = Mock(TechEasySolutionsClient)
        this.sut = new TelTestDataIntegrator(this.httpClient)
    }

    void "POST Data Point DataSet with OK"() {
        given:
            DataPointDataSet dataSet = new DataPointDataSet(
                name: 'name',
                description: 'description',
                constraints: new DataPointDataSetConstraints(
                    xValues: new RangeModel<BigDecimal>(min: 1.0, max: 200.0),
                    yValues: new RangeModel<BigDecimal>(min: 1.0, max: 200.0),
                    quantity: new RangeModel<Integer>(min: 50, max: 20000)
                )
            )

            DataPointDataSet expected = new DataPointDataSet(
                id: 1L,
                name: 'name',
                description: 'description',
                constraints: new DataPointDataSetConstraints(
                    xValues: new RangeModel<BigDecimal>(min: 1.0, max: 200.0),
                    yValues: new RangeModel<BigDecimal>(min: 1.0, max: 200.0),
                    quantity: new RangeModel<Integer>(min: 50, max: 20000)
                )
            )

        when:
            DataPointDataSet result = this.sut.createDataPointDataSet(dataSet).blockingGet()

        then:
            1 * httpClient.post('/datasets/data-points', dataSet, DataPointDataSet) >> CompletableFuture.supplyAsync {
                expected
            }
            result == expected
    }

    void "POST Data Point DataSet with BadRequest result"() {
        given:
            DataPointDataSet dataSet = new DataPointDataSet(
                name: 'name',
                description: 'description',
                constraints: new DataPointDataSetConstraints(
                    xValues: new RangeModel<BigDecimal>(min: 1.0, max: 200.0),
                    yValues: new RangeModel<BigDecimal>(min: 1.0, max: 200.0),
                    quantity: new RangeModel<Integer>(min: 50, max: 20000)
                )
            )

        when:
            this.sut.createDataPointDataSet(dataSet).blockingGet()

        then:
            1 * httpClient.post('/datasets/data-points', dataSet, DataPointDataSet) >> CompletableFuture.supplyAsync {
                throw new ExecutionException("message", new ClientBadRequestException(Mock(HttpResponse), new ServiceErrorModel(
                    code: 'code',
                    param: 'param',
                    message: 'message'
                )))
            }
            ClientBadRequestException ex = thrown(ClientBadRequestException)
            ex.serviceError.code == 'code'
            ex.serviceError.param == 'param'
            ex.serviceError.message == 'message'
    }

    void "GET Data Point DataSet with OK"() {
        given:
            DataPointDataSet expected = new DataPointDataSet(
                id: 1L,
                name: 'name',
                description: 'description',
                constraints: new DataPointDataSetConstraints(
                    xValues: new RangeModel<BigDecimal>(min: 1.0, max: 200.0),
                    yValues: new RangeModel<BigDecimal>(min: 1.0, max: 200.0),
                    quantity: new RangeModel<Integer>(min: 50, max: 20000)
                )
            )

        when:
            DataPointDataSet result = this.sut.get(1L).blockingGet()

        then:
            1 * httpClient.get('/datasets/data-points/1', DataPointDataSet) >> CompletableFuture.supplyAsync {
                expected
            }
            result == expected
    }

    void "GET Data Point DataSet with error"() {
        when:
            this.sut.get(1L).blockingGet()

        then:
            1 * httpClient.get('/datasets/data-points/1', DataPointDataSet) >> CompletableFuture.supplyAsync {
                throw new ExecutionException("message", new ClientNotFoundException(Mock(HttpResponse), new ServiceErrorModel(
                    code: 'code',
                    param: 'param',
                    message: 'message'
                )))
            }
            ClientNotFoundException ex = thrown(ClientNotFoundException)
            ex.serviceError.code == 'code'
            ex.serviceError.param == 'param'
            ex.serviceError.message == 'message'
    }

}
