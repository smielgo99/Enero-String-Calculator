import groovy.util.GroovyTestCase

class StringCalculatorTest extends GroovyTestCase {

    def stringCalculator = new StringCalculator()

    void testEmptyStringReturnZero() {
        assert stringCalculator.add(" ") == 0
    }

    void testStringReturnNumber() {
        assert stringCalculator.add("1") == 1
    }

    void testTwoStringReturnTheirSum() {
        assert stringCalculator.add("1,2") == 3
    }
    
    void testStringNumbersReturnTheirSum() {
        assert stringCalculator.add("1,2,3") == 6
    }
    
    void testStringNumbersWithNewLinesReturnTheirSum() {
        assert stringCalculator.add("1\n2,3") == 6
    }
    
    void testStringNumbersWithDelimiterReturnTheirSum() {
        assert stringCalculator.add("//;\n1;2") == 3
    }
    
    void testStringNumbersNegativesNotAllowed() {
        def message = shouldFail(Exception) {
            stringCalculator.add("1,-1,2,-2")
        }
        assert message.contains("negatives not allowed")
        assert message.contains("-1")
        assert message.contains("-2")  
    }
    
}

class StringCalculator {

    int add(String numbers) {
        numbers = changeDelimiterToComma(numbers)
        numbers.replaceAll(" ", "0").replaceAll("\n", ",").split(",").inject(0) { sum, num ->
            if (num.toInteger() >= 0)
                sum += num.toInteger()
            else
                negativesNotAllowed(numbers)
        }
    }
    
    private String changeDelimiterToComma(String numbers) {
        numbers.find(/\/\/\S[\n]/) { match ->
            def delimeter = match[2]
            numbers = numbers.minus(match).replaceAll(delimeter, ",")
        }
        return numbers
    }
    
    def negativesNotAllowed(String numbers) {
        def negativeNums = []
        numbers.replaceAll(" ", "0").replaceAll("\n", ",").split(",").each { num ->
            if (num.toInteger() < 0)
                negativeNums << num
        }
        throw new Exception("negatives not allowed " + negativeNums)
    }
    
}
