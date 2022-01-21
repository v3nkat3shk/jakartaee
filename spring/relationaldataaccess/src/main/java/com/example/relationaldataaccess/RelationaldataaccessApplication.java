package com.example.relationaldataaccess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@SpringBootApplication
public class RelationaldataaccessApplication implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(RelationaldataaccessApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RelationaldataaccessApplication.class, args);
	}

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public void run(String... args) throws Exception {
		log.info("Creating tables");

		// Creating a table is H2 Database
		jdbcTemplate.execute("DROP TABLE customers IF EXISTS");
		jdbcTemplate.execute("CREATE TABLE customers(" +
				"id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");

		// Split up array of name into first and last name
		List<Object[]> splitNames = Arrays.asList("Jhon Whoo", "Jeff Dean", "Josh Block", "Josh Long").stream()
				.map(name -> name.split(" "))
				.collect(Collectors.toList());

		splitNames.forEach(name -> log.info(String.format("Inserting customers record for %s %s", name[0], name[1])));

		// using jdbc templates batchUpdate operation for bulk loading
		jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?, ?)", splitNames);

		log.info("Querying for customers record where first_name = 'Josh':");
		jdbcTemplate.query("SELECT id, first_name, last_name FROM customers WHERE first_name = ?",
				(rs, rowNum) -> new Customer(rs.getLong("id"),
						rs.getString("first_name"),
						rs.getString("last_name")),
				new Object[] {"Josh"}).forEach(customer -> log.info(customer.toString()));

	}
}


























































