fun main() {
    // 1 задача
    val secondsAgo = 7200

    println(agoToText(secondsAgo))


    // 2 задача
    val result = calculateCommission("MasterCard", 70_000.0, 10_000.0)
    println(result.first) // 60.0 (10 000 * 0.006)
    println(result.second) // true

    val visaResult = calculateCommission(cardType = "Visa", transferAmount = 5000.0)
    println(visaResult.first) // 37.5 (5000 * 0.0075), округляем до 38
    println(visaResult.second) // true

// Превышение суточного лимита
    val blockedResult = calculateCommission(transferAmount = 200_000.0)
    println(blockedResult.first) // 0.0
    println(blockedResult.second) // false
}

fun getMinuteForm(minutes: Int): String {
    return when {
        minutes % 10 == 1 && minutes % 100 != 11 -> "$minutes минута"
        minutes in 2..4 || (minutes % 10 in 2..4 && minutes % 100 !in 12..14) -> "$minutes минуты"
        else -> "$minutes минут"
    }
}

fun getHourForm(hours: Int): String {
    return when {
        hours % 10 == 1 && hours % 100 != 11 -> "$hours час"
        hours in 2..4 || (hours % 10 in 2..4 && hours % 100 !in 12..14) -> "$hours часа"
        else -> "$hours часов"
    }
}

fun agoToText(secondsAgo: Int): String {
    return when {
        secondsAgo <= 60 -> "был(а) только что"
        secondsAgo <= 3600 -> "был(а) ${getMinuteForm((secondsAgo + 30) / 60)} назад"
        secondsAgo <= 86400 -> "был(а) ${getHourForm((secondsAgo + 1800) / 3600)} назад"
        secondsAgo <= 172800 -> "был(а) вчера"
        secondsAgo <= 259200 -> "был(а) позавчера"
        else -> "был(а) давно"
    }
}



fun calculateCommission(
    cardType: String = "Мир",
    previousTransfersInMonth: Double = 0.0,
    transferAmount: Double
): Pair<Any, Boolean> {

    if (transferAmount > 150_000) {
        return Pair(0.0, false)
    }

    val totalTransferInMonth = previousTransfersInMonth + transferAmount
    if (totalTransferInMonth > 600_000) {
        return Pair(0.0, false)
    }

    var commission = 0.0

    when (cardType) {
        "MasterCard" -> {
            val limitWithoutCommission = 75_000

            val amountAfterLimit: Double = when {
                previousTransfersInMonth >= limitWithoutCommission -> transferAmount
                previousTransfersInMonth < limitWithoutCommission && totalTransferInMonth > limitWithoutCommission ->
                    totalTransferInMonth - limitWithoutCommission

                else -> 0.0
            }

            commission = amountAfterLimit * 0.006 + 20
        }


        "Visa" -> {
            commission = transferAmount * 0.0075

            if (commission < 35) {
                commission = 35.0
            }
        }

        else -> { /* Карта Мир - комиссия 0 */ }
    }

    return Pair(commission, true)
}