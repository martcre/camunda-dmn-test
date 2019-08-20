package de.martincremer.sample.camundadmg;

import java.io.InputStream;

import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.test.DmnEngineRule;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.model.dmn.Dmn;
import org.camunda.bpm.model.dmn.DmnModelInstance;
import org.camunda.bpm.model.dmn.instance.Decision;
import org.camunda.bpm.model.dmn.instance.DecisionTable;
import org.camunda.bpm.model.dmn.instance.InformationRequirement;
import org.camunda.bpm.model.dmn.instance.Input;
import org.camunda.bpm.model.dmn.instance.KnowledgeRequirement;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldTest {
	
	public static Logger LOGGER = LoggerFactory.getLogger(HelloWorldTest.class);

	@Rule
	public DmnEngineRule dmnEngineRule = new DmnEngineRule();
	public DmnEngine dmnEngine;
	public DmnDecision decision;

	@Before
	public void parseDecision() {
		InputStream inputStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("ruleset1.dmn11.xml");

		dmnEngine = dmnEngineRule.getDmnEngine();
		decision = dmnEngine.parseDecision("ruleset1", inputStream);
	}

//	@Test
	public void test1() {
		VariableMap vars = Variables.createVariables().putValue("alpha", "Sommer").putValue("beta", 1);

		DmnDecisionTableResult result = dmnEngine.evaluateDecisionTable(decision, vars);
		System.out.println((String) result.getSingleResult().getSingleEntry());

	}
	
	@Test
	public void test2() {
		LOGGER.info("test2");
		InputStream inputStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("ruleset1.dmn11.xml");
		
		DmnModelInstance modelInstance = Dmn.readModelFromStream(inputStream);
		
		Decision decision = modelInstance.getModelElementById("ruleset1");
		
		for (InformationRequirement ir : decision.getInformationRequirements()) {
			LOGGER.info("-- Information Requirement --");
			LOGGER.info("Text Content: " + ir.getTextContent());
		}
		
		for (KnowledgeRequirement kr : decision.getKnowledgeRequirements()) {
			LOGGER.info("-- Knowledge Requirement --");
			LOGGER.info("Text Content: " + kr.getTextContent());
		}
		
		for (DecisionTable dt : decision.getChildElementsByType(DecisionTable.class)) {
			LOGGER.info("-- Decision Tables --");
			LOGGER.info("ID: " + dt.getId());
			
			for (Input i : dt.getChildElementsByType(Input.class)) {
				LOGGER.info("- INPUT - ");
				LOGGER.info("ID: " + i.getId());
				LOGGER.info("Camunda Input Variable: " + i.getCamundaInputVariable());
			}
		}
	}

}
