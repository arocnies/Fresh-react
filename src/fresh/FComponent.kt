package fresh

import react.RComponent
import react.RProps
import react.RState
import react.setState
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * A [RComponent] with build in delegation for props and state.
 * In your component, declare state variables using [val foo by state(<initialValue>)].
 * Changes to the variables are reflected in the [FState] via delegation.
 */
abstract class FComponent : RComponent<RProps, FState>() {

    override fun componentDidMount() {
        // Initialize map for state once mounted.
        setState { values = mapOf() }
    }

    override fun shouldComponentUpdate(nextProps: RProps, nextState: FState): Boolean {
        // Should update if any of the values in the state map have changed.
        return state.values != nextState
    }

    fun <T> state(initialValue: T): ReadWriteProperty<FComponent, T> = object : ReadWriteProperty<FComponent, T> {
        val value: T = initialValue

        @Suppress("UNCHECKED_CAST")
        override fun getValue(thisRef: FComponent, property: KProperty<*>): T {
            return state.values?.get(property.name) as T ?: initialValue
        }

        override fun setValue(thisRef: FComponent, property: KProperty<*>, value: T) = setState {
            values?.toMutableMap()?.let {
                it[property.name] = value
                values = it
            }
        }
    }
}

/**
 * State value map for [FComponent]s
 */
interface FState : RState {
    var values: Map<String, Any?>?
}