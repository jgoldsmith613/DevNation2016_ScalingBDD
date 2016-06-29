package com.rhc.lab.test.cucumber;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features/", plugin = {"json:target/cucumber.json"}, tags = {
		"~@Ignore", "~@not_implemented", "@Rules"}, glue = {"com.rhc.lab.test.cucumber.kie"})
public class RemoteRulesRunner {
}
