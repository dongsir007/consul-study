package com.dosth.consul.task.batch.batchprocessing;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	private static final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
		return this.jobBuilderFactory.get("importUserJob")
			.incrementer(new RunIdIncrementer())
			.listener(listener)
			.flow(step1)
			.end()
			.build();
	}
	
	@Bean
	public Step step1(JdbcBatchItemWriter<Person> writer) {
		return this.stepBuilderFactory.get("step1")
			.<Person, Person> chunk(2)
			.reader(reader())
			.processor(processor())
			.writer(writer)
			.build();
	}
	
	@Bean
	public FlatFileItemReader<Person> reader() {
		logger.info("读取csv文件");
//		FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>();
//		reader.setName("personItemReader");
//		reader.setResource(new ClassPathResource("sample-data.csv"));
//		reader.setLinesToSkip(1);
//		reader.setLineMapper(new DefaultLineMapper<Person>() {{
//			setLineTokenizer(new DelimitedLineTokenizer() {{
//				setNames("firstName", "lastName");
//				setDelimiter(",");
//			}});
//			setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
//				setTargetType(Person.class);
//			}});
//		}});
//		
//		return reader;
				
			return	new FlatFileItemReaderBuilder<Person>()
			.name("personItemReader")
			.resource(new ClassPathResource("sample-data.csv"))
			.delimited()
			.names(new String[] {"firstName", "lastName"})
			.linesToSkip(1)
			.fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
				setTargetType(Person.class);
			}}).build();
	}
	
	@Bean
	public PersonItemProcessor processor() {
		return new PersonItemProcessor();
	}
	
	@Bean
	public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
		logger.info("数据源:" + dataSource);
		return new JdbcBatchItemWriterBuilder<Person>()
			.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
			.sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
			.dataSource(dataSource)
			.build();
	}
}