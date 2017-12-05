package ticker

import fresh.FComponent
import react.RBuilder
import kotlin.browser.window

//interface TickerProps : RProps {
//    var startFrom: Int
//}
//
//interface TickerState : RState {
//    var secondsElapsed: Int
//}

class Ticker : FComponent() {
    val startFrom by prop(0)
    var secondsElapsed by state(0)

    override fun componentWillMount() {
        secondsElapsed = startFrom
    }

    var timerID: Int? = null

    override fun componentDidMount() {
        timerID = window.setInterval({
            // actually, the operation is performed on a state's copy, so it stays effectively immutable
            secondsElapsed += 1
        }, 1000)
    }

    override fun componentWillUnmount() {
        window.clearInterval(timerID!!)
    }

    override fun RBuilder.render() {
        println("Render! $secondsElapsed")
        +"This app has been running for $secondsElapsed seconds."
    }
}

fun RBuilder.ticker(startFrom: Int = 0) = child(Ticker::class) {
    attrs.values = mapOf("startFrom" to startFrom)
}