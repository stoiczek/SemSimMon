package pl.edu.agh.semmon.common.api;

import pl.edu.agh.semmon.common.api.measurement.CoreMeasurementService;
import pl.edu.agh.semmon.common.api.remote.RemoteClientsService;
import pl.edu.agh.semmon.common.api.resource.CoreResourcesService;

/**
 * Interface for managing the Core component
 *
 * @author koperek
 * @author Tadeusz Kozak
 */
public interface CoreServiceFacade extends CoreResourcesService,
    CoreMeasurementService, RemoteClientsService {

}
