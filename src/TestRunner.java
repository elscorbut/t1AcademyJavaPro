import annotation.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.TreeSet;

public class TestRunner {

    public static void runTests(Class<?> c) {

        Method beforeSuiteMethod = null;
        Method afterSuiteMethod = null;
        Method beforeTestMethod = null;
        Method afterTestMethod = null;

        Comparator<Method> testMethodComparator = Comparator.comparing(method -> method.getAnnotation(Test.class).priority());
        TreeSet<Method> sortedTestMethods = new TreeSet<>(testMethodComparator);
        try {
            Object testedObject = c.getDeclaredConstructor().newInstance();
            // Получение всех методов
            for (Method method : c.getDeclaredMethods()) {
                // Получение метода с аннотацией @BeforeSuite
                if (method.isAnnotationPresent(BeforeSuite.class)) {
                    String annotationName = BeforeSuite.class.getSimpleName();
                    beforeSuiteMethod = handleSuiteMethod(method, beforeSuiteMethod, annotationName, true);
                }

                // Получение метода с аннотацией @AfterSuite
                if (method.isAnnotationPresent(AfterSuite.class)) {
                    String annotationName = AfterSuite.class.getSimpleName();
                    afterSuiteMethod = handleSuiteMethod(method, afterSuiteMethod, annotationName, true);
                }

                // Получение метода с аннотацией @BeforeTest
                if (method.isAnnotationPresent(BeforeTest.class)) {
                    String annotationName = BeforeTest.class.getSimpleName();
                    beforeTestMethod = handleSuiteMethod(method, beforeTestMethod, annotationName, false);
                }

                // Получение метода с аннотацией @AfterTest
                if (method.isAnnotationPresent(AfterTest.class)) {
                    String annotationName = AfterTest.class.getSimpleName();
                    afterTestMethod = handleSuiteMethod(method, afterTestMethod, annotationName, false);
                }

                // Получение всех методов с аннотацией @Test и добавление их в сортируемый TreeSet
                if (method.isAnnotationPresent(Test.class)) {
                    int priority = method.getAnnotation(Test.class).priority();
                    validatePriority(priority);
                    sortedTestMethods.add(method);
                }
            }

            // Вызов метода с аннотацией @BeforeSuite
            if (beforeSuiteMethod != null) {
                beforeSuiteMethod.invoke(testedObject);
            }

            // Вызов методов с аннотацией @Test в порядке priority
            for (Method testMethod : sortedTestMethods) {
                if (beforeTestMethod != null) {
                    beforeTestMethod.invoke(testedObject);
                }

                if (testMethod.isAnnotationPresent(CsvSource.class)) {
                    String csvArguments = testMethod.getAnnotation(CsvSource.class).arguments();
                    Object[] args = getArgs(csvArguments, testMethod);
                    testMethod.invoke(testedObject, args);
                } else {
                    testMethod.invoke(testedObject);
                }

                if (afterTestMethod != null) {
                    afterTestMethod.invoke(testedObject);
                }
            }

            // Вызов метода с аннотацией @AfterSuite
            if (afterSuiteMethod != null) {
                afterSuiteMethod.invoke(testedObject);
            }
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static Method handleSuiteMethod(Method method, Method suiteMethod, String annotationName, boolean staticMethod) {
        if (suiteMethod == null) {
            boolean isStatic = Modifier.isStatic(method.getModifiers());
            if (staticMethod == isStatic) {
                return method;
            } else {

                throw new IllegalStateException("@" + annotationName + " method " + (staticMethod ? "must" : "cannot") + " be static");
            }
        } else {
            throw new IllegalStateException("Only one @" + annotationName + " method is allowed");
        }
    }

    private static void validatePriority(int priority) throws NoSuchMethodException {
        Method priorityMethod = Test.class.getDeclaredMethod("priority");
        if (priorityMethod.isAnnotationPresent(IntRange.class)) {
            int from = priorityMethod.getAnnotation(IntRange.class).from();
            int to = priorityMethod.getAnnotation(IntRange.class).to();
            if (priority < from || priority > to) {
                throw new IllegalArgumentException("priority must be between " + from + " and " + to);
            }
        }
    }

    private static Object[] getArgs(String csvArgumentsStr, Method method) {
        Object[] args = new Object[method.getParameterCount()];
        String[] csvArgumentsArr = csvArgumentsStr.split(",\\s*");
        if (csvArgumentsArr.length != method.getParameterCount()) {
            throw new IllegalArgumentException("@CsvSource arguments must have " + method.getParameterCount() + " arguments divided by comma");
        }

        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Object casted = castToType(csvArgumentsArr[i].trim(), parameterTypes[i]);
            args[i] = casted;
        }

        return args;
    }

    private static Object castToType(String csvArgument, Class<?> parameterType) {
        if (parameterType == int.class) {
            return Integer.parseInt(csvArgument);
        } else if (parameterType == boolean.class) {
            return Boolean.parseBoolean(csvArgument);
        } else if (parameterType == String.class) {
            return csvArgument;
        } else {
            throw new IllegalArgumentException("Unsupported parameter type: " + parameterType);
        }
    }
}
