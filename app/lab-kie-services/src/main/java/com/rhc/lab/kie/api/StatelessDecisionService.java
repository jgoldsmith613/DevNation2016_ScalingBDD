package com.rhc.lab.kie.api;

import java.util.Collection;

public interface StatelessDecisionService {

	public <T> T execute(Collection<Object> facts, String processId,
			Class<T> responseClazz);

	public <Response> Response execute(Collection<Object> facts,
			String processId);

	public <Response> Response execute(Collection<Object> facts,
			Class<Response> responseClazz);

	public <Response> Response execute(Collection<Object> facts);

}
