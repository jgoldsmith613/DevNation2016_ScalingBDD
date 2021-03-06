package com.rhc.lab.test.cucumber;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features/", plugin = {"pretty",
		"json:target/cucumber.json"}, tags = {"@wip", "~@Ignore",
		"~@not_implemented"}, glue = {"com.rhc"})
public class RunTags {

}
