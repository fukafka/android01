package com.norajet.firstgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.norajet.firstgame.ui.theme.FirstGameTheme

class ComposeActivity:ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContent{
            FirstGameTheme{
                TaskListScreen()
            }
        }
    }
}

data class Task(val title:String,val done: Boolean=false)

@Composable
fun TaskListScreen() {
    val tasks=remember{
        mutableStateListOf(
            Task("学习Compose基础", done=true),
            Task("学习Compose状态管理"),
            Task("完成Compose实验"),
        )
    }
    var input by remember { mutableStateOf("") }

    val doneCount=tasks.count { it.done }

    Scaffold(modifier=Modifier.fillMaxSize()){innerPadding ->
        Column(
            modifier=Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal=20.dp,vertical=16.dp)
        ) {
            Text(
                text="课程学习任务",
                style=MaterialTheme.typography.headlineSmall,
                fontWeight=FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment=Alignment.CenterVertically) {
                OutlinedTextField(
                    value=input,
                    onValueChange={input=it },
                    modifier=Modifier.weight(1f),
                    singleLine=true,
                    label={Text("任务内容") }
                )
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick={
                        if (input.isNotBlank()){
                            tasks.add(Task(input.trim()))
                            input = ""
                        }
                    }
                ) {
                    Text("添加")
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text="已完成:$doneCount/${tasks.size}",
                style=MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(8.dp))
            LazyColumn(
                modifier=Modifier.weight(1f),
                verticalArrangement=Arrangement.spacedBy(4.dp)
            ) {
                itemsIndexed(tasks){index, task ->
                    Row(verticalAlignment=Alignment.CenterVertically){
                        Checkbox(
                            checked=task.done,
                            onCheckedChange=
                                {
                                    checked->
                                    tasks[index]=task.copy(done=checked)
                                }
                        )
                        Text(
                            text=task.title,
                            modifier=Modifier.weight(1f),
                            textDecoration=if(task.done){
                                TextDecoration.LineThrough
                            }
                            else
                            {
                                TextDecoration.None
                            }
                        )
                        Text(
                            text="删除",
                            modifier=Modifier
                                .clickable
                                {
                                    tasks.removeAt(index)
                                }
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}