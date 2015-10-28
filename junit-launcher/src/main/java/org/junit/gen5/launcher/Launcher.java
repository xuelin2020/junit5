package org.junit.gen5.launcher;

import org.junit.gen5.engine.TestEngine;
import org.junit.gen5.engine.TestListener;
import org.junit.gen5.engine.TestListenerRegistry;
import org.junit.gen5.engine.TestPlanSpecification;

import static org.junit.gen5.engine.TestListenerRegistry.notifyListeners;
import static org.junit.gen5.launcher.TestEngineRegistry.lookupAllTestEngines;

public class Launcher {

  public void registerTestListener(TestListener testListener) {
    TestListenerRegistry.registerListener(testListener);
  }

  public TestPlan createTestPlanWithConfiguration(TestPlanSpecification specification) {
    TestPlan testPlan = new TestPlan();
    for (TestEngine testEngine : lookupAllTestEngines()) {
      testPlan.addTests(testEngine.discoverTests(specification));
    }
    return testPlan;
  }

  public void execute(TestPlan testPlan) {
    notifyListeners(TestListener::testPlanExecutionStarted);

    for (TestEngine testEngine : lookupAllTestEngines()) {
      testEngine.execute(testPlan.getAllTestsForTestEngine(testEngine));
    }

    notifyListeners(TestListener::testPlanExecutionFinished);
  }
}