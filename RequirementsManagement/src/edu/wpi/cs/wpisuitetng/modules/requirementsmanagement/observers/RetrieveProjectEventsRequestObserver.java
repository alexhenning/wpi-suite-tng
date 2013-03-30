package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers;

import java.util.Arrays;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.IterationCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.ProjectEventsCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.ProjectEvent;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

public class RetrieveProjectEventsRequestObserver implements RequestObserver {
	
	private ProjectEventsCallback callback;
	
	public RetrieveProjectEventsRequestObserver(ProjectEventsCallback callback) {
		this.callback = callback;
	}

	@Override
	public void responseSuccess(IRequest iReq) {
		final ResponseModel response = iReq.getResponse();
		final ProjectEvent[] reqs = ProjectEvent.fromJSONArray(response.getBody());
		System.out.println(reqs.toString());
		callback.callback(Arrays.asList(reqs));

	}

	@Override
	public void responseError(IRequest iReq) {
		System.err.println("The request to retrieve ProjectEvents failed.");	
	}

	@Override
	public void fail(IRequest iReq, Exception exception) {
		System.err.println("The request to retrieve ProjectEvents failed.");
	}
}
