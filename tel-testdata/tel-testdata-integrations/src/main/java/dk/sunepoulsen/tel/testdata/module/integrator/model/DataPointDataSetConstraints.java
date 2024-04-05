package dk.sunepoulsen.tel.testdata.module.integrator.model;

import dk.sunepoulsen.tes.rest.models.BaseModel;
import dk.sunepoulsen.tes.rest.models.RangeModel;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DataPointDataSetConstraints implements BaseModel {
    @NotNull
    private RangeModel<Double> xValues;

    @NotNull
    private RangeModel<Double> yValues;

    @NotNull
    private RangeModel<Integer> quantity;
}
