package com.felixseifert.swedisheventplanners.security;

import com.felixseifert.swedisheventplanners.views.login.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;

@Component 
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

	@Override
	public void serviceInit(ServiceInitEvent event) {
		event.getSource().addUIInitListener(uiEvent -> { 
			final UI ui = uiEvent.getUI();
			ui.addBeforeEnterListener(this::authenticateNavigation);
		});
	}

	private void authenticateNavigation(BeforeEnterEvent event) {
		if (!LoginView.class.equals(event.getNavigationTarget())
				&& !SecurityUtils.isAccessGranted(event.getNavigationTarget())) {

			if(SecurityUtils.isUserLoggedIn()) {
				event.rerouteToError(NotFoundException.class);
				return;
			}
			event.rerouteTo(LoginView.class);
		}
	}
}