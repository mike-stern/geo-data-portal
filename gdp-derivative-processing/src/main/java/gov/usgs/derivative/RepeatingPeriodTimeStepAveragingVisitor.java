package gov.usgs.derivative;

import gov.usgs.derivative.time.NetCDFDateUtil;
import gov.usgs.derivative.time.RepeatingPeriodTimeStepDescriptor;
import gov.usgs.derivative.time.TimeStepDescriptor;
import java.util.List;
import org.joda.time.ReadablePeriod;
import ucar.nc2.dt.GridDatatype;

/**
 *
 * @author tkunicki
 */
public class RepeatingPeriodTimeStepAveragingVisitor extends AbstractTimeStepAveragingVisitor {

    private ReadablePeriod repeatingPeriod;

    public RepeatingPeriodTimeStepAveragingVisitor(ReadablePeriod repeatingPeriod, String outputDir) {
        this.repeatingPeriod = repeatingPeriod;
        if (outputDir != null) {
            this.outputDir = outputDir;
        } else {
            this.outputDir = DerivativeUtil.DEFAULT_P30Y_PATH;
        }
    }
   
    @Override
    protected TimeStepDescriptor generateDerivativeTimeStepDescriptor(List<GridDatatype> gridDatatypeList) {
        return new RepeatingPeriodTimeStepDescriptor(
            NetCDFDateUtil.toIntervalUTC(gridDatatypeList.get(0)),
            repeatingPeriod); 
    }
    
}
