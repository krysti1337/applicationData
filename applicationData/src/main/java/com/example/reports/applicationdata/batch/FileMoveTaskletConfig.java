package com.example.reports.applicationdata.batch;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Configuration
public class FileMoveTaskletConfig {

    @Bean
    public Tasklet moveFileTasklet() {
        return (contribution, chunkContext) -> {
            Path source = Path.of("src/main/resources/transactions.csv");
            Path target = Path.of("src/main/resources/archive/transactions.csv");
            Files.createDirectories(target.getParent());
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Step moveFileStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("moveFileStep", jobRepository)
                .tasklet(moveFileTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Job transactionFullJob(JobRepository jobRepository, Step transactionStep, Step moveFileStep) {
        return new JobBuilder("transactionFullJob", jobRepository)
                .start(transactionStep)
                .next(moveFileStep)
                .build();
    }
}
