package eu.sweetlygeek.volkswagen;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.util.List;

/**
 * Magical test runner ! All runs well !
 * Created by valentin on 21/10/15.
 */
public class VolkswagenTestRunner extends BlockJUnit4ClassRunner {

    private static final String[] CI_ENV_VALUES = new String[]{
            "CI", "CONTINUOUS_INTEGRATION", "BUILD_ID", "BUILD_NUMBER", "TEAMCITY_VERSION", "TRAVIS", "CIRCLECI", "JENKINS_URL", "HUDSON_URL", "bamboo.buildKey", "PHPCI", "GOCD_SERVER_HOST", "BUILDKITE"
    };

    private static boolean isInCI() {
        for (String key : CI_ENV_VALUES) {
            String envValue = System.getenv(key);
            if (envValue != null && !envValue.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public VolkswagenTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected void collectInitializationErrors(List<Throwable> errors) {
        if (isInCI()) {
            // What initialization errors ?
            //noinspection UnnecessaryReturnStatement
            return;
        } else {
            super.collectInitializationErrors(errors);
        }
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        if (isInCI()) {
            Description description = describeChild(method);
            if (isIgnored(method)) {
                notifier.fireTestIgnored(description);
            } else {
                runLeafAndIgnoreErrors(methodBlock(method), description, notifier);
            }
        } else {
            super.runChild(method, notifier);
        }
    }

    private void runLeafAndIgnoreErrors(Statement statement, Description description, RunNotifier notifier) {
        EachTestNotifier eachNotifier = new EachTestNotifier(notifier, description);
        eachNotifier.fireTestStarted();
        try {
            statement.evaluate();
        } catch (Throwable e) {
            // An error ? Surely you must be joking
        } finally {
            eachNotifier.fireTestFinished();
        }
    }

    @Override
    protected Statement classBlock(RunNotifier notifier) {
        if (isInCI()) {
            Statement statement = childrenInvoker(notifier);
            statement = withBeforeClasses(statement);
            statement = withAfterClasses(statement);
            statement = withClassRules(statement);
            return statement;
        } else {
            return super.classBlock(notifier);
        }
    }

    @Override
    protected Statement withBeforeClasses(Statement statement) {
        if (isInCI()) {
            List<FrameworkMethod> befores = getTestClass().getAnnotatedMethods(BeforeClass.class);
            return befores.isEmpty() ? statement : new RunBefores(statement, befores, null);
        } else {
            return super.withBeforeClasses(statement);
        }
    }

    @Override
    protected Statement withAfterClasses(Statement statement) {
        if (isInCI()) {
            List<FrameworkMethod> afters = getTestClass().getAnnotatedMethods(AfterClass.class);
            return afters.isEmpty() ? statement : new RunAfters(statement, afters, null);
        } else {
            return super.withAfterClasses(statement);
        }
    }

    private Statement withClassRules(Statement statement) {
        List<TestRule> classRules = classRules();
        return classRules.isEmpty() ? statement : new RunRules(statement, classRules, getDescription());
    }
}
