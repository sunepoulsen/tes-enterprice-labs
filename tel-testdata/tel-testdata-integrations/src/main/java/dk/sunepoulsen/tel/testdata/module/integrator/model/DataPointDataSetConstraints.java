package dk.sunepoulsen.tel.testdata.module.integrator.model;

import dk.sunepoulsen.tes.rest.models.BaseModel;
import dk.sunepoulsen.tes.rest.models.RangeModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DataPointDataSetConstraints implements BaseModel {
    @NotNull
    @Valid
    private RangeModel<Double> xValues;

    @NotNull
    @Valid
    private RangeModel<Double> yValues;

    @NotNull
    @Valid
    private RangeModel<Integer> quantity;
}
