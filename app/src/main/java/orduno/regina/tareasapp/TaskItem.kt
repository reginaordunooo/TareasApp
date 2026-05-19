package orduno.regina.tareasapp

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    task: TaskEntity,
    onToggleCompleted: () -> Unit,
    onDelete: () -> Unit
) {

    var showDialog by remember {
        mutableStateOf(false)
    }

    val dismissState = rememberSwipeToDismissBoxState()

    LaunchedEffect(dismissState.currentValue) {

        if (
            dismissState.currentValue ==
            SwipeToDismissBoxValue.EndToStart
        ) {

            showDialog = true
            dismissState.reset()
        }
    }

    val dateFormat = remember {
        SimpleDateFormat("dd/MM HH:mm")
    }

    val fechaTexto = remember(task.creado_en) {
        dateFormat.format(Date(task.creado_en))
    }

    SwipeToDismissBox(

        state = dismissState,

        enableDismissFromStartToEnd = false,

        backgroundContent = {

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Blue)
                    .padding(horizontal = 20.dp),

                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Update,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        },

        content = {

            ListItem(

                leadingContent = {

                    Checkbox(
                        checked = task.completado,
                        onCheckedChange = {
                            onToggleCompleted()
                        }
                    )
                },

                headlineContent = {

                    Text(
                        text = task.titulo,

                        textDecoration =
                            if (task.completado)
                                TextDecoration.LineThrough
                            else null,

                        color =
                            if (task.completado)
                                MaterialTheme.colorScheme.onSurface
                                    .copy(alpha = 0.5f)
                            else
                                MaterialTheme.colorScheme.onSurface
                    )
                },

                supportingContent = {

                    Text(text = fechaTexto)
                },

                trailingContent = {

                    IconButton(
                        onClick = {
                            showDialog = true
                        }
                    ) {

                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(
                                R.string.delete_action_desc
                            )
                        )
                    }
                }
            )
        }
    )

    if (showDialog) {

        AlertDialog(

            onDismissRequest = {
                showDialog = false
            },

            title = {
                Text("Eliminar tarea")
            },

            text = {
                Text("¿Seguro que quieres eliminar esta tarea?")
            },

            confirmButton = {

                Button(
                    onClick = {
                        onDelete()
                        showDialog = false
                    }
                ) {

                    Text("Eliminar")
                }
            },

            dismissButton = {

                Button(
                    onClick = {
                        showDialog = false
                    }
                ) {

                    Text("Cancelar")
                }
            }
        )
    }
}