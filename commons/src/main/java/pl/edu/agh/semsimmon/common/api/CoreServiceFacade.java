package pl.edu.agh.semsimmon.common.api;

import pl.edu.agh.semsimmon.common.api.knowledge.KnowledgeService;
import pl.edu.agh.semsimmon.common.api.measurement.CoreMeasurementService;
import pl.edu.agh.semsimmon.common.api.remote.RemoteClientsService;
import pl.edu.agh.semsimmon.common.api.resource.CoreResourcesService;

/**
 * Interface for managing the Core component
 *
 * @author koperek
 * @author Tadeusz Kozak
 */
public interface CoreServiceFacade extends CoreResourcesService,
    CoreMeasurementService, RemoteClientsService, KnowledgeService {

}
