package com.rhc.lab.kie.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.ExecutionResults;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rhc.lab.domain.BookingResponse;
import com.rhc.lab.kie.api.StatelessDecisionService;
import com.rhc.lab.kie.common.QueryUtils;
import com.rhc.lab.kie.common.ReflectiveExecutionResultsTransformer;

public class LocalStatelessDecisionService implements StatelessDecisionService {
	private static final Logger logger = LoggerFactory
			.getLogger(LocalStatelessDecisionService.class);
	private KieCommands commandFactory;
	private KieContainer kieContainer;
	private KieBase kieBase;
	private KieRuntimeLogger auditLogger;

	public LocalStatelessDecisionService() {
		kieContainer = KieServices.Factory.get().getKieClasspathContainer();
		try {
			kieContainer.newStatelessKieSession();
		} catch (Exception e) {
			logger.warn("There is no Kie Module on classpath");
		}

		commandFactory = KieServices.Factory.get().getCommands();
	}

	public LocalStatelessDecisionService(String kbase) {
		kieContainer = KieServices.Factory.get().getKieClasspathContainer();
		kieBase = kieContainer.getKieBase(kbase);
		try {
			kieBase.newStatelessKieSession();
		} catch (Exception e) {
			logger.error("Could not find kie module");
		}

		commandFactory = KieServices.Factory.get().getCommands();
	}

	@Override
	public <Response> Response execute(Collection<Object> facts,
			String processId, Class<Response> responseClazz) {
		;
		StatelessKieSession session;
		try {
			if (kieBase != null) {
				session = kieBase.newStatelessKieSession();
			} else {
				session = kieContainer.newStatelessKieSession();
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return null;
		}
		BatchExecutionCommand batchExecutionCommand = createBatchExecutionCommand(
				facts, processId, responseClazz);

		// only use in test situations
		session.addEventListener(new DebugRuleRuntimeEventListener());
		session.addEventListener(new DebugAgendaEventListener());

		ExecutionResults results = session.execute(batchExecutionCommand);

		Response response = ReflectiveExecutionResultsTransformer.transform(
				results, responseClazz);

		if (auditLogger != null) {
			auditLogger.close();
		}
		return response;
	}

	public BatchExecutionCommand createBatchExecutionCommand(
			Collection<Object> facts, String processId, Class<?> responseClazz) {
		List<Command<?>> commands = new ArrayList<Command<?>>();

		if (facts != null) {
			commands.add(commandFactory.newInsertElements(facts));
		}
		if (processId != null && !processId.isEmpty()) {
			commands.add(commandFactory.newStartProcess(processId));
		}
		commands.add(commandFactory.newInsert(new BookingResponse(),
				"response", true, null));

		commands.add(commandFactory.newFireAllRules());

		// creates commands to run the queries at the end of process
		commands.addAll(QueryUtils.buildQueryCommands(responseClazz));

		return commandFactory.newBatchExecution(commands);
	}

	public KieContainer getKieContainer() {
		return kieContainer;
	}

	@Override
	public <Response> Response execute(Collection<Object> facts,
			Class<Response> responseClazz) {

		return execute(facts, null, responseClazz);
	}

	public Object executeForClass(Collection<Object> facts, String processId,
			String clazzName) {
		try {
			Class<?> clazz = Class.forName(clazzName);
			return execute(facts, processId, clazz);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public <Response> Response execute(Collection<Object> facts) {
		return execute(facts, null, null);
	}

	@Override
	public <Response> Response execute(Collection<Object> facts,
			String processId) {
		return execute(facts, processId, null);
	}

}
