import annotation.*;

public class AppTest {

    @BeforeSuite
    public static void setup() {
        System.out.println("@BeforeSuite executed");
    }

    @BeforeTest
    public void setupThis() {
        System.out.println("@BeforeTest executed");
    }

    @Test(priority = 1)
    public void runTestOne() {
        System.out.println("======TEST ONE EXECUTED=======");
    }

    @Test(priority = 2)
    public void runTestTwo() {
        System.out.println("======TEST TWO EXECUTED=======");
    }

    @Test
    public void runTestThree() {
        System.out.println("======TEST THREE EXECUTED=======");
    }

    @CsvSource(arguments = "10, Java, 20, true")
    @Test(priority = 6)
    public void runTestFour(int a, String b, int c, boolean d) {
        System.out.println("======TEST FOUR EXECUTED with following arguments: a = " + a + ", b = " + b + ", c = " + c + ", d = " + d + "=======");
    }

    @AfterTest
    public void tearThis() {
        System.out.println("@AfterTest executed");
    }

    @AfterSuite
    public static void tear() {
        System.out.println("@AfterSuite executed");
    }
}
