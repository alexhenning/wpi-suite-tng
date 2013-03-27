package edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.observers;

import java.util.Arrays;

import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.CreateIterationController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.controllers.GetSingleRequirementController;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.Iteration;
import edu.wpi.cs.wpisuitetng.modules.requirementsmanagement.models.RequirementModel;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

public class RetrieveIterationRequestObserver implements RequestObserver {
	
	private CreateIterationController controller;
	
	public RetrieveIterationRequestObserver(CreateIterationController controller) {
		this.controller = controller;
	}

	@Override
	public void responseSuccess(IRequest iReq) {
		final ResponseModel response = iReq.getResponse();
		final Iteration[] reqs = Iteration.fromJSONArray(response.getBody());
		controller.receivedGetIterationConfirmation(Arrays.asList(reqs));

	}

	@Override
	public void responseError(IRequest iReq) {
		System.err.println("The request to add a message failed.");	
	}

	@Override
	public void fail(IRequest iReq, Exception exception) {
		System.err.println("The request to add a message failed.");
	}

}
