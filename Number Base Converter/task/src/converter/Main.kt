package converter

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.pow

// Do not delete this line

fun main() {
    showMenu()
}

fun showMenu() {
    while (true) {
        print("Enter two numbers in format: {source base} {target base} (To quit type /exit)")
        val request1 = readln()
        if (request1 == "/exit") return
        val (sourceBase, targetBase) = request1.trim().split("\\s+".toRegex()).map (String::toInt)

        while (true){
            println("Enter number in base $sourceBase to convert to base $targetBase (To go back type /back)")
            val request2 = readln()

            if (request2 == "/back") break

            if (targetBase < sourceBase && sourceBase != 10) {
                val tmp = translatorToDec(request2.uppercase(), sourceBase).toString()
                println("Conversion result: ${translatorToBase(tmp,targetBase)}")
            } else if (targetBase < sourceBase && sourceBase == 10) {
                println("Conversion result: ${translatorToBase(request2,targetBase)}")
            } else if (targetBase == sourceBase){
                println("Conversion result: $request2")
            } else {
                val tmp = translatorToDec(request2.uppercase(), sourceBase).toString()
                println("Conversion result: ${translatorToBase(tmp, targetBase)}")
            }
        }
    }
}

fun translatorToDec(number:String, fromBase: Int): BigDecimal {
    var result = BigDecimal.ZERO

    return if(number.contains(".")) {
        val integerPart = number.split(".").first()
        val fractional = number.split(".").last()
        result = translatorToDec(integerPart, fromBase) + translatorFractionalToDec(fractional, fromBase)
        result.setScale(5,RoundingMode.CEILING)
    } else {
        for (i in number.indices) {
            result += try {
                (fromBase.toBigDecimal().pow(number.lastIndex - i).toBigInteger() * number[i].digitToInt().toBigInteger()).toBigDecimal()
            } catch (e: IllegalArgumentException) {
                (fromBase.toBigDecimal().pow(number.lastIndex - i).toBigInteger() * (number[i].code - 55).toBigInteger()).toBigDecimal()
            }
        }
        result
    }
}

fun translatorFractionalToDec(number:String, fromBase: Int): BigDecimal {
    var result: BigDecimal = BigDecimal.ZERO

    for (i in number.indices) {
        result += try {
            (1.0/(fromBase.toDouble().pow(i + 1)) * number[i].digitToInt()).toBigDecimal()
        } catch (e: IllegalArgumentException) {
            (1.0/(fromBase.toDouble().pow(i + 1)) * (number[i].code - 55)).toBigDecimal()
        }
    }
    return result
}

fun translatorToBase(number: String, toBase: Int): String {
    var result = ""
    if(number.contains(".")) {
        val integerPart = number.split(".").first()
        var fractional = number.toBigDecimal().remainder(BigDecimal.ONE)
        result += translatorToBase(integerPart, toBase)
        result += "."
        while (result.split(".").last().length < 5) {
            fractional *= toBase.toBigDecimal() //7.05
            result += if (fractional.toInt() > 9) '7' + fractional.toInt() else fractional.toInt()
            fractional = fractional.remainder(BigDecimal.ONE)
        }
        return result
    } else {
        if (number == "0") return number
        var integerPart = number.toBigInteger()
        while(integerPart > 0.toBigInteger()) {
            val remains = integerPart % toBase.toBigInteger()
            integerPart /= toBase.toBigInteger()
            result += if (remains > 9.toBigInteger() ) '7' + remains.toInt() else remains
        }
        return result.reversed()
    }
}




