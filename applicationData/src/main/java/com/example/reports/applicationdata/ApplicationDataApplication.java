package com.example.reports.applicationdata;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApplicationDataApplication implements CommandLineRunner {

    private final JobLauncher jobLauncher;
    private final Job transactionImportJob;

    public ApplicationDataApplication(JobLauncher jobLauncher,
                                      @Qualifier("transactionImportJob") Job transactionImportJob) {
        this.jobLauncher = jobLauncher;
        this.transactionImportJob = transactionImportJob;
    }

    public static void main(String[] args) {
        SpringApplication.run(ApplicationDataApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        JobParameters parameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(transactionImportJob, parameters);
    }
}
