package orduno.regina.tareasapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.Volatile

@Database(
    entities = [TaskEntity::class],
    version = 1
)
abstract class AppDatabase: RoomDatabase(){

    abstract fun taskDao(): TaskDao

    companion object{

        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Tareas que se cargan la primera vez que se
        // instala la app. Edita esta lista con tus
        // tareas reales del reto con el socio formador.
        private val TAREAS_INICIALES = listOf(
            TaskEntity(
                titulo = "Configurar repositorio en GitHub",
                completado = true
            ),
            TaskEntity(
                titulo = "Implementar base de datos con Room",
                completado = true
            ),
            TaskEntity(
                titulo = "Construir UI con Jetpack Compose",
                completado = true
            ),
            // Agrega aquí las tuyas reales.
            TaskEntity(
                titulo = "Integrar SciChart como dependencia en el proyecto",
                completado = true
            ),
            TaskEntity(
                titulo = "Implementar SCIChartSurface base con UIViewRepresentable",
                completado = true
            ),
            TaskEntity(
                titulo = "Implementar gráfica scatter con datos reales",
                completado = true
            ),
            TaskEntity(
                titulo = "Implementar ChartConfigParser para parsear JSON de gráficas",
                completado = true
            ),
            TaskEntity(
                titulo = "Implementar ChartRendererView para seleccionar tipo de gráfica",
                completado = true
            ),
            TaskEntity(
                titulo = "Diseñar e implementar feed cards de visualizaciones",
                completado = true
            ),
            TaskEntity(
                titulo = "Implementar vista fullscreen de gráficas",
                completado = true
            ),
            TaskEntity(
                titulo = "Implementar ChartTooltipCoordinator para manejo de tooltips",
                completado = true
            ),
            TaskEntity(
                titulo = "Descartar tooltip al hacer pan en la gráfica",
                completado = true
            ),
            TaskEntity(
                titulo = "Hacer rango del eje Y dinámico con autoRange",
                completado = true
            ),
            TaskEntity(
                titulo = "Adaptar header de fullscreen para modo landscape",
                completado = true
            ),
            TaskEntity(
                titulo = "Agregar zoom y pan a las gráficas con SCIZoomPanModifier",
                completado = true
            ),
            TaskEntity(
                titulo = "Implementar tooltip interactivo al tocar un punto",
                completado = true
            ),
            TaskEntity(
                titulo = "Agregar flecha indicadora al tooltip",
                completado = true
            ),
            TaskEntity(
                titulo = "Agregar modo landscape en fullscreen",
                completado = true
            ),
            TaskEntity(
                titulo = "Implementar prevención de screenshots con UITextField seguro",
                completado = true
            ),
            TaskEntity(
                titulo = "Fijar posición del tooltip al centro exacto del punto tocado",
                completado = false
            ),
            TaskEntity(
                titulo = "Corregir valor Y del tooltip en gráficas stacked bar",
                completado = false
            ),
            TaskEntity(
                titulo = "Agregar límites de zoom y drag para no perder la gráfica",
                completado = false
            ),
            TaskEntity(
                titulo = "Hacer dinámicos los títulos de las feed cards desde Firestore",
                completado = false
            ),
            TaskEntity(
                titulo = "Escribir unit tests para ChartConfigParser",
                completado = false
            ),
            TaskEntity(
                titulo = "Escribir unit tests para los use cases de la app",
                completado = false
            )
        )

        fun getInstance(
            context: Context
        ): AppDatabase {
            return INSTANCE ?: synchronized(
                this
            ){
                val instance = Room
                    .databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "tasks_db"
                    )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                        // Insertamos las tareas iniciales en un
                        // hilo separado. NUNCA en el main thread.
                            CoroutineScope(Dispatchers.IO).launch {
                                val dao = getInstance(context).taskDao()
                                TAREAS_INICIALES.forEach { tarea ->
                                    dao.insert(tarea)
                                }
                            }
                        }
                    })

                    .build()
                INSTANCE = instance
                instance
            }
        }

    }
}