package cz.fi.muni.eshop.controller;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import cz.fi.muni.eshop.util.Controller;

@Model
public class ControllerBean {
	@Inject
	private Controller controller;
	@Inject
	private Logger log;
	private boolean autoClean;

	public void wipeOutDb() {
		log.warning("Deleting all enties from db");

		if (!controller.wipeOutDb()) {
			System.out.println("print some message!");
		}
	}

	public boolean isAutoCleanUp() {
		return controller.isAutoClean();
	}

	public void switchCleanUp() {
		log.warning("switching clean up");
		controller.switchAutoClean();
	}

	public String autoCleanUpString() {
		return String.valueOf(controller.isAutoClean());
	}

}
