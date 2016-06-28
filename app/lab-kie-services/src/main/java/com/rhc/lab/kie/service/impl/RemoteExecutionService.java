package com.rhc.lab.kie.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.command.Command;
import org.kie.api.runtime.ExecutionResults;
import org.kie.internal.command.CommandFactory;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.marshalling.xstream.XStreamMarshaller;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.RuleServicesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rhc.lab.domain.Booking;
import com.rhc.lab.domain.BookingRequest;
import com.rhc.lab.domain.BookingResponse;
import com.rhc.lab.domain.PerformanceType;
import com.rhc.lab.domain.Performer;
import com.rhc.lab.domain.Venue;
import com.rhc.lab.kie.api.StatelessDecisionService;

@SuppressWarnings("unchecked")
public class RemoteExecutionService implements StatelessDecisionService {

	private static final Logger logger = LoggerFactory
			.getLogger(RemoteExecutionService.class);

	private static String REST_ENDPOINT = "http://localhost:8080/kie-server/services/rest/server";
	private static String REST_USERNAME = "kieserver";
	private static String REST_PASSWORD = "kieserver1!";
	private static String SESSION_ID = "kession1";
	private static String DEFAULT_PROCESS = "bookingProcess";
	private static String CONTAINER_ID = "booking1.0";

	@Override
	public <T> T execute(Collection<Object> facts, String processId,
			Class<T> responseClazz) {
		KieServicesConfiguration config = KieServicesFactory
				.newRestConfiguration(REST_ENDPOINT, REST_USERNAME,
						REST_PASSWORD);
		config.setMarshallingFormat(MarshallingFormat.XSTREAM);
		// Set<Class<?>> extraJaxbClasses = new HashSet<Class<?>>();
		// extraJaxbClasses.add(ObjectFactory.class);
		// extraJaxbClasses.add(LocalDate.class);
		// extraJaxbClasses.add(LocalDateAdapter.class);
		// config.addJaxbClasses(extraJaxbClasses);
		KieServicesClient client = KieServicesFactory
				.newKieServicesClient(config);
		RuleServicesClient ruleClient = client
				.getServicesClient(RuleServicesClient.class);

		BatchExecutionCommand batchExecutionCommand = CommandFactory
				.newBatchExecution(new ArrayList<Command<?>>(), SESSION_ID);
		try {
			batchExecutionCommand = createBatchExecutionCommand(facts,
					processId, responseClazz.newInstance(), null);
		} catch (Exception e) {
			System.out.println("batch execution screwed up");
			e.printStackTrace();
		}

		Set<Class<?>> clazzes = new HashSet<Class<?>>();
		clazzes.add(Booking.class);
		clazzes.add(BookingRequest.class);
		clazzes.add(BookingResponse.class);
		clazzes.add(Performer.class);
		clazzes.add(Venue.class);
		clazzes.add(PerformanceType.class);
		XStreamMarshaller marshaller = new XStreamMarshaller(clazzes, this
				.getClass().getClassLoader());

		// JSONMarshaller marshaller = new JSONMarshaller(clazzes,
		// this.getClass()
		// .getClassLoader());
		System.out.println(marshaller.marshall(batchExecutionCommand));
		// tring result = marshaller.marshall(batchExecutionCommand);
		// System.out.println(result);
		// only use in test situations
		// session.addEventListeners(new DebugRuleRuntimeEventListener());
		// session.addEventListener(new DebugAgendaEventListener());

		ServiceResponse<ExecutionResults> results = ruleClient
				.executeCommandsWithResults(CONTAINER_ID, batchExecutionCommand);
		// System.out.println(results.getResult());

		// System.out.println(result.toString());
		// System.out.println(result.getValue("response"));
		System.out.println(results.getResult().getFactHandle("response")
				.getClass());

		T result = (T) results.getResult().getValue("response");

		// ObjectMapper mapper = new ObjectMapper();
		// T response = mapper.convertValue(result, responseClazz);
		// System.out.println(resultArray.get(0).getClass());
		// String response = resultArray.get(0).toString();
		// System.out.println(response);
		// T responseObject = marshaller.unmarshall(response,
		// responseClazz);
		// /System.out.println(response.toString());
		return result;
	}

	@Override
	public <Response> Response execute(Collection<Object> facts,
			String processId) {
		// TODO Auto-generated method stub
		return (Response) execute(facts, processId, Object.class);
	}

	@Override
	public <Response> Response execute(Collection<Object> facts,
			Class<Response> responseClazz) {

		return execute(facts, DEFAULT_PROCESS, responseClazz);
	}

	@Override
	public <Response> Response execute(Collection<Object> facts) {
		// TODO Auto-generated method stub
		return (Response) execute(facts, DEFAULT_PROCESS, Object.class);
	}

	public BatchExecutionCommand createBatchExecutionCommand(
			Collection<Object> facts, String processId, Object responseClazz,
			Map<String, Object> variables) throws InstantiationException,
			IllegalAccessException {
		List<Command<?>> commands = new ArrayList<Command<?>>();

		// commands.add(CommandFactory.newSetGlobal("logger", logger));

		if (facts != null) {
			commands.add(CommandFactory.newInsertElements(facts));
		}
		if (responseClazz != null) {
			Collection<Object> responseFacts = new ArrayList<Object>();

			responseFacts.add(responseClazz);
			commands.add(CommandFactory.newInsert(responseClazz, "response"));
			// + "(responseClazz,
			// "response", true, null));
		}

		if (processId != null && !processId.isEmpty()) {
			commands.add(CommandFactory.newStartProcess(processId));
		}

		commands.add(CommandFactory.newFireAllRules());

		System.out.println(commands.toString());

		return CommandFactory.newBatchExecution(commands, SESSION_ID);
	}
}