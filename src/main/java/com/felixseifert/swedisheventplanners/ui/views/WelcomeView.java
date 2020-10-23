package com.felixseifert.swedisheventplanners.ui.views;

import com.felixseifert.swedisheventplanners.ui.views.main.MainView;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainView.class)
@PageTitle("Swedish Event Planners")
public class WelcomeView extends VerticalLayout {

    public WelcomeView() {
        add(new Span("Welcome to the application of Swedish Event Planners."));
    }
}
