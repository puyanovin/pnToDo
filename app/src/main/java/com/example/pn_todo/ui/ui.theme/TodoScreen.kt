package com.example.pn_todo.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.pn_todo.R
import com.example.pn_todo.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen() {
    var tasks by remember { mutableStateOf(listOf(
        Task(1, "یادگیری کاتلین", true),
        Task(2, "ساخت اپلیکیشن اندروید"),
        Task(3, "تمرین Jetpack Compose")
    ))}
    
    var newTaskText by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "📝 TodoApp",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (newTaskText.isNotBlank()) {
                        val newTask = Task(
                            id = tasks.size + 1,
                            title = newTaskText
                        )
                        tasks = tasks + newTask
                        newTaskText = ""
                    }
                }
            ) {
                Icon(Icons.Filled.Add, stringResource(id = R.string.add_task))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // بخش افزودن کار جدید
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = newTaskText,
                    onValueChange = { newTaskText = it },
                    label = { Text("کار جدید...") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Button(
                    onClick = {
                        if (newTaskText.isNotBlank()) {
                            val newTask = Task(
                                id = tasks.size + 1,
                                title = newTaskText
                            )
                            tasks = tasks + newTask
                            newTaskText = ""
                        }
                    }
                ) {
                    Text("افزودن")
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // لیست کارها
            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("کاری برای نمایش وجود ندارد")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(tasks) { task ->
                        TaskItem(
                            task = task,
                            onTaskChecked = { checkedTask ->
                                tasks = tasks.map {
                                    if (it.id == checkedTask.id) {
                                        it.copy(isCompleted = !it.isCompleted)
                                    } else {
                                        it
                                    }
                                }
                            },
                            onTaskDeleted = { taskToDelete ->
                                tasks = tasks.filter { it.id != taskToDelete.id }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onTaskChecked: (Task) -> Unit,
    onTaskDeleted: (Task) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onTaskChecked(task) }
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = task.title,
                modifier = Modifier.weight(1f),
                style = if (task.isCompleted) {
                    MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = TextDecoration.LineThrough
                    )
                } else {
                    MaterialTheme.typography.bodyLarge
                }
            )
            
            IconButton(onClick = { onTaskDeleted(task) }) {
                Icon(Icons.Filled.Delete, "حذف")
            }
        }
    }
}