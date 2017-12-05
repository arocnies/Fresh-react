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
abstract class FComponent : RComponent<FProps, FState>() {
    override fun shouldComponentUpdate(nextProps: FProps, nextState: FState): Boolean {
        // Should update if any of the values in the state map have changed.
        return state.values != nextState
    }

    fun <T> state(initialValue: T): ReadWriteProperty<FComponent, T> = object : ReadWriteProperty<FComponent, T> {
        @Suppress("UNCHECKED_CAST")
        override fun getValue(thisRef: FComponent, property: KProperty<*>): T {
            return state.values?.get(property.name) as T ?: initialValue
        }

        override fun setValue(thisRef: FComponent, property: KProperty<*>, value: T) = setState {
            println("Setting ${property.name} to $value")
            values = (values?.toMutableMap() ?: mutableMapOf()).apply { set(property.name, value) }
        }
    }

    fun <T> prop(initialValue: T) : ReadOnlyProperty<FComponent, T> = object : ReadOnlyProperty<FComponent, T> {
        @Suppress("UNCHECKED_CAST")
        override fun getValue(thisRef: FComponent, property: KProperty<*>): T {
            return props.values?.get(property.name) as T ?: initialValue
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