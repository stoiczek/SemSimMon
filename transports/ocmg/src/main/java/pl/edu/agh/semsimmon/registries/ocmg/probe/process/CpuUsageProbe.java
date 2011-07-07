/*
 * Copyright (C) SayMama Ltd 2011
 *
 * All rights reserved. Any use, copying, modification, distribution and selling
 * of this software and it's documentation for any purposes without authors' written
 * permission is hereby prohibited.
 */
package pl.edu.agh.semsimmon.registries.ocmg.probe.process;

import org.balticgrid.ocmg.base.ConnectionException;
import org.balticgrid.ocmg.base.MonitorException;
import org.balticgrid.ocmg.objects.Application;

import pl.edu.agh.semsimmon.common.vo.core.measurement.CapabilityValue;
import pl.edu.agh.semsimmon.common.vo.core.resource.Resource;
import pl.edu.agh.semsimmon.registries.ocmg.OcmgException;
import pl.edu.agh.semsimmon.registries.ocmg.probe.CapabilityProbe;

import static pl.edu.agh.semsimmon.registries.ocmg.AppHierarchyParsingUtils.findProcess;

/**
 * @author Tadeusz Kozak
 * @TODO description
 *
 * Created on: 2011-07-07 21:01
 * <br/>
 * <a href="http://www.saymama.com">http://www.saymama.com</a>
 */
public class CpuUsageProbe implements CapabilityProbe {

  private static final String PREV_CPU_TIME = "previousCPUTime";

  private static final String PREV_MEASUREMENT_TIME = "previousMeasurement";


  @Override
  public CapabilityValue getCapabilityValue(Resource resource, Application application, String capabilityType)
      throws OcmgException {
    double previousCpuTime = resource.hasProperty(PREV_CPU_TIME) ? (Double) resource.getProperty(PREV_CPU_TIME) : 0L;
    long previousTime = resource.hasProperty(PREV_MEASUREMENT_TIME) ? (Long) resource.getProperty(PREV_MEASUREMENT_TIME) : 0L;


    try {
      org.balticgrid.ocmg.objects.Process process = findProcess(resource, application);
      if(process == null || process.getDynamicInfo() == null) {
        return new CapabilityValue(Double.NaN);
      }
      final double totalTime = process.getDynamicInfo().getTotalTime();
      final double diff = totalTime - previousCpuTime;
      final long currentTime = System.currentTimeMillis() / 1000;
      resource.setProperty(PREV_CPU_TIME, totalTime);
      resource.setProperty(PREV_MEASUREMENT_TIME, currentTime);
      final double usage = (diff / (currentTime - previousTime)) * 100;
      return new CapabilityValue(usage);
    } catch (MonitorException e) {
      throw new OcmgException(e);
    } catch (ConnectionException e) {
      throw new OcmgException(e);
    }
  }
}
