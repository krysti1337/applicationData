package com.example.reports.applicationdata.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

@Route("/import")
@Component
public class CsvImportView extends VerticalLayout {

    private final JobLauncher jobLauncher;
    private final Job importTransactionJob;



    public CsvImportView(JobLauncher jobLauncher, Job importTransactionJob) {
        this.jobLauncher = jobLauncher;
        this.importTransactionJob = importTransactionJob;
        Button importButton = new Button("Import CSV", event -> runJob());
        add(importButton);
    }

    private void runJob() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(importTransactionJob, params);
            Notification.show("Import job started!");
        } catch (Exception e) {
            e.printStackTrace();
            Notification.show("Failed to start import: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
        }
    }
}