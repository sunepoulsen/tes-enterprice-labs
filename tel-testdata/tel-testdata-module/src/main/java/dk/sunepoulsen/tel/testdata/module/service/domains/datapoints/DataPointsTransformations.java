package dk.sunepoulsen.tel.testdata.module.service.domains.datapoints;

import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSet;
import dk.sunepoulsen.tel.testdata.module.integrator.model.DataPointDataSetConstraints;
import dk.sunepoulsen.tel.testdata.module.service.domains.persistence.model.DataPointDataSetEntity;
import dk.sunepoulsen.tes.rest.models.RangeModel;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
class DataPointsTransformations {

    public DataPointDataSetEntity toEntity(DataPointDataSet model) {
        DataPointDataSetEntity entity = new DataPointDataSetEntity();

        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setDescription(model.getDescription());
        entity.setMinX(model.getConstraints().getXValues().getMin().doubleValue());
        entity.setMaxX(model.getConstraints().getXValues().getMax().doubleValue());
        entity.setMinY(model.getConstraints().getYValues().getMin().doubleValue());
        entity.setMaxY(model.getConstraints().getYValues().getMax().doubleValue());
        entity.setMinQuantity(model.getConstraints().getQuantity().getMin());
        entity.setMaxQuantity(model.getConstraints().getQuantity().getMax());

        return entity;
    }

    public DataPointDataSet toModel(DataPointDataSetEntity entity) {
        DataPointDataSet model = new DataPointDataSet();

        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());

        RangeModel<BigDecimal> xValues = new RangeModel<>();
        xValues.setMin(BigDecimal.valueOf(entity.getMinX()));
        xValues.setMax(BigDecimal.valueOf(entity.getMaxX()));

        RangeModel<BigDecimal> yValues = new RangeModel<>();
        yValues.setMin(BigDecimal.valueOf(entity.getMinY()));
        yValues.setMax(BigDecimal.valueOf(entity.getMaxY()));

        RangeModel<Integer> quantity = new RangeModel<>();
        quantity.setMin(entity.getMinQuantity());
        quantity.setMax(entity.getMaxQuantity());

        DataPointDataSetConstraints constraints = new DataPointDataSetConstraints();
        constraints.setXValues(xValues);
        constraints.setYValues(yValues);
        constraints.setQuantity(quantity);
        model.setConstraints(constraints);

        return model;
    }

}

