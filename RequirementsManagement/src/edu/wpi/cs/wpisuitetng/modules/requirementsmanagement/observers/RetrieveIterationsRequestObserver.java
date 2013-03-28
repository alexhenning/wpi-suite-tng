package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers;

import java.util.Arrays;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.CreateIterationController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.IterationCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.RequirementsCallback;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

public class RetrieveIterationsRequestObserver implements RequestObserver {
	
	private IterationCallback callback;
	
	public RetrieveIterationsRequestObserver(IterationCallback callback) {
		this.callback = callback;
	}

	@Override
	public void responseSuccess(IRequest iReq) {
		final ResponseModel response = iReq.getResponse();
		final Iteration[] reqs = Iteration.fromJSONArray(response.getBody());
		System.out.println(reqs.toString());
		callback.callback(Arrays.asList(reqs));

	}

	@Override
	public void responseError(IRequest iReq) {
		System.err.println("The request to retrieve Iterations failed.");	
	}

	@Override
	public void fail(IRequest iReq, Exception exception) {
		System.err.println("The request to retrieve Iterations failed.");
	}
}
