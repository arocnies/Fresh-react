package fresh

import react.RComponent
import react.RProps
import react.RState
import react.setState
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * A [RComponent] with build in delegation for props and state.
 * In your component, declare state variables using [val foo by state(<initialValue>)].
 * Changes to the variables are reflected in the [FState] via delegation.
 */
abstract class FComponent<P: FProps, S: FState> : RComponent<P, S>() {
    override fun shouldComponentUpdate(nextProps: P, nextState: S): Boolean {
        // Should update if any of the values in the state map have changed.
        return state.values != nextState
    }

    fun <T> state(initialValue: T): ReadWriteProperty<Any?, T> = object : ReadWriteProperty<Any?, T> {
        @Suppress("UNCHECKED_CAST", "UNNECESSARY_SAFE_CALL") // TODO: Should this SAFE_CALL be removed and allowed to error?
        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return state?.values?.get(property.name) as T ?: initialValue
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = setState {
            values = (values?.toMutableMap() ?: mutableMapOf()).apply { set(property.name, value) }
        }
    }

    fun <T> prop(initialValue: T) : ReadOnlyProperty<Any?, T> = object : ReadOnlyProperty<Any?, T> {
        @Suppress("UNCHECKED_CAST", "UNNECESSARY_SAFE_CALL") // TODO: Should this SAFE_CALL be removed and allowed to error?
        override fun getValue(thisRef: Any?, property: KProperty<*>): T {
            return props?.values?.get(property.name) as T ?: initialValue
        }
    }
}

/**
 * State value map for [FComponent]s.
 * All state variables are stored in [values]
 */
interface FState : RState {
    var values: Map<String, Any?>?
}

/**
 * Props value map for [FComponent]s.
 * All props variables are stored in [values]
 */
interface FProps : RProps {
    var values: Map<String, Any?>?
}