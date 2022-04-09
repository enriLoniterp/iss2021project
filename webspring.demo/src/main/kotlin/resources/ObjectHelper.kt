package resources

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import java.io.File
import java.lang.reflect.Modifier

typealias ObjMap = HashMap<String, Any?>

fun <T : Any> T.getInstance() : Any? {
    val target = if(this is Class<*>) this else javaClass

    return target.getDeclaredField("INSTANCE")?.get(null)
}

class ObjectHelper {
    companion object {
        val mapper = ObjectMapper().apply {
            enable(SerializationFeature.INDENT_OUTPUT)
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }

        fun objectToMap(obj: Any): ObjMap {
            var res = ObjMap()

            val instance = obj.getInstance()
            val o = instance ?: obj

            o.javaClass.declaredFields.forEach {
                if(it.name != "INSTANCE") {
                    it.isAccessible = true
                    val value = if(Modifier.isStatic(it.modifiers)) it.get(null) else it.get(o)
                    res[it.name] = value
                }
            }

            o.javaClass.classes.forEach {
                res[it.simpleName] = objectToMap(it)
            }

            return res
        }

        fun saveObject(path: String, obj: Any)  {
            mapper.writeValue(File(path), objectToMap(obj))
        }

        fun loadObject(path: String, obj: Any) {
            val json = mapper.readValue<HashMap<*,*>>(File(path), HashMap::class.java) as ObjMap

            loadObject(obj, json)
        }

        fun loadObject(obj: Any, props: ObjMap) {
            val objectParam = mapper.writeValueAsString(props)

            mapper.readValue(objectParam, obj::class.java)

            obj.javaClass.classes.forEach {
                val instance = it.getInstance()
                val map = props[it.simpleName]

                if(map != null && instance != null) {
                    loadObject(instance, map as ObjMap)
                }
            }
        }
    }
}