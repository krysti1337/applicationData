package com.example.reports.applicationdata.ui;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.component.page.Push;
import org.springframework.stereotype.Component;

@Component
@PWA(name = "Enterprise App", shortName = "Enterprise")
@Push
public class AppShellConfig implements AppShellConfigurator {
}
