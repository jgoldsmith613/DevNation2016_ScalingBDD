package com.rhc.lab.test.cucumber;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@ActiveProfiles("local")
@ContextConfiguration("kie-context.xml")
@CucumberOptions(features = "classpath:features/", plugin = {"json:target/cucumber.json"}, tags = {
		"~@Ignore", "~@not_implemented", "@Overload"}, glue = {"com.rhc.lab.test.cucumber.kie"})
public class OverloadRunner {

}
