# 实验2 · Android 界面布局

Android 课程实验：分别用 **XML 布局**（LinearLayout / TableLayout / ConstraintLayout）
和 **Jetpack Compose** 实现 5 个界面。

- 包名：`com.norajet.firstgame`
- 全部界面在同一个 App 内，通过 Run 配置切换启动哪个 Activity

## 一、开发环境

| 项目 | 版本 / 说明 |
| --- | --- |
| 开发工具 | Android Studio |
| Gradle | 8.13（wrapper，已换腾讯云镜像） |
| Android Gradle Plugin | 8.13.2 |
| Kotlin | 2.0.21 |
| compileSdk / targetSdk | 36 |
| minSdk | 24（Android 7.0） |
| 运行设备 | MuMu 模拟器 / 任意 Android 7.0+ 真机 |

## 二、五个界面一览

| # | 界面 | 布局方式 | Activity | 布局文件 |
| --- | --- | --- | --- | --- |
| 1 | 4×4 文字网格（黑底白框） | LinearLayout（嵌套 + weight 分配） | `LinearLayoutActivity` | `activity_linear.xml` |
| 2 | 菜单列表（快捷键表） | TableLayout（TableRow + stretchColumns） | `TableLayoutActivity` | `activity_table.xml` |
| 3 | 计算器键盘（4×4 按钮） | ConstraintLayout（链 + weight 等分） | `ConstraintActivity1` | `constraint1.xml` |
| 4 | 星际出行界面 | ConstraintLayout（叠放 + 横向链） | `ConstraintActivity2` | `constraint2.xml` |
| 5 | 课程任务清单（可增删、可勾选） | Jetpack Compose | `ComposeActivity` | 无，纯 Kotlin 代码 |


## 三、目录结构

```
app/src/main/
├── java/com/norajet/firstgame/
│   ├── MainActivity.kt                 # 启动页（模板）
│   ├── LinearLayoutActivity.kt         # 界面 1
│   ├── TableLayoutActivity.kt          # 界面 2
│   ├── ConstraintActivity1.kt          # 界面 3
│   ├── ConstraintActivity2.kt          # 界面 4
│   ├── ComposeActivity.kt              # 界面 5（含 Task / TaskListScreen）
│   └── ui/theme/                       # Compose 主题（Color/Theme/Type）
└── res/
    ├── layout/
    │   ├── activity_linear.xml
    │   ├── activity_table.xml
    │   ├── constraint1.xml
    │   └── constraint2.xml
    ├── drawable/
    │   ├── cell_border.xml             # 白边框（shape + stroke）
    │   ├── rowtopline.xml
    │   └── 6 个界面图标（见下）
    └── values/
        └── themes.xml                   # 公共样式 LayoutCell / TableText / key
```

界面 4 用到的 6 个图标素材来自课程群文件：
`space_station_icon`、`rover_icon`、`rocket_icon`、`galaxy`、`single_arrow`、`double_arrows`。

## 四、各界面实现要点

### 1. LinearLayout 4×4 网格

- 最外层 `LinearLayout` 竖向 + 黑底 `#FF000000` + `padding=24dp`
- 里面 4 个横向 `LinearLayout`，各写 `layout_height=0dp` + `layout_weight=1` → 4 行等高
- 每行 4 个 `TextView`：宽度 `0dp` + `layout_weight` 按份数分宽度
  （第 2 列给 `1.5`，让这一列更宽，对应实验要求里的不等宽效果）
- 黑底白字白框由样式 `LayoutCell` 统一配置，边框用 `@drawable/cell_border`
  （XML 没有 border 属性，只能用 `<shape>` + `<stroke>` 画）

### 2. TableLayout 菜单列表

- 根元素 `TableLayout` + `android:stretchColumns="1"` → 第 2 列自动拉伸
- 每个 `<TableRow>` 是一行；直接放在 `TableLayout` 下的控件独占一行（标题就这么放）
- 列是按 `<TableRow>` 里的顺序推断出来的，不用声明列数

### 3. ConstraintLayout 计算器键盘

- 4 行 × 4 列按钮，每行 = 一条横向链
- **沿行方向用"首尾相接"**：`Start_toEndOf` / `End_toStartOf`
- **垂直于行方向用"同边对齐"**：`Top_toTopOf` / `Bottom_toBottomOf`
- 每行第 1 个按钮负责纵向上下相接（`Bottom_toTopOf` 下一行、`Top_toBottomOf` 上一行），
  最后一行改成 `Bottom_toBottomOf="parent"`
- 行、列都用 `layout_constraintVertical_weight` / `Horizontal_weight` = 1 等分
- 每个子 View 必须至少有 1 个水平约束 + 1 个垂直约束，否则不生效

### 4. ConstraintLayout 星际出行界面

- 顶部 3 个页签（Space Stations / Flights / Rovers）用**横向链 + weight=1** 三等分
- 每个页签的图标约束到各自文字的 `Start` 和 `End`
  （左右都对齐同一个目标 = 在格子里水平居中），文字动、图标自动跟着动
- 中间的绿色条：先放一个空 `View` 当底色，再把 DCA / 箭头 / MARS 叠在它上面
- "One Way" 用 `paddingEnd` 给右侧的双箭头图标腾位置
- 星系用 `layout_constraintVertical_bias` 微调垂直位置（0=贴顶，1=贴底）

### 5. Jetpack Compose 任务清单

和 XML 的根本区别：**没有布局文件，界面是 Kotlin 函数"画"出来的**。

- 状态：`mutableStateListOf` 存任务列表，`mutableStateOf` 存输入框内容
- `tasks[index] = task.copy(done = checked)`：必须**换新对象**，Compose 靠引用变化判断要不要重画
- 完成线：`TextDecoration.LineThrough`；计数：`tasks.count { it.done }` 自动算
- 每行右侧 `Text("删除")` + `Modifier.clickable { tasks.removeAt(index) }` 删除该条
- 输入框 + "添加"按钮新增任务，加完清空输入框
- `Modifier.weight(1f)` 在 `Row` 里相当于"抢剩余宽度"（和 LinearLayout 的 weight 一个道理）

## 五、踩坑记录

1. **`The activity 'Xxx' is not declared in AndroidManifest.xml`**
   - 原因：新 Activity 没注册
   - 处理：在 Manifest 里加 `<activity>`

2. **`The activity must be exported or contain an intent-filter`**
   - 原因：Run 配置用 Specified Activity，但 `exported=false`
   - 处理：改成 `android:exported="true"`

3. **`Unable to load class 'ijInit1...'`**
   - 原因：Gradle 脚本缓存损坏
   - 处理：`gradlew --stop` 后删 `~/.gradle/caches/jars-9`

4. **`checkDebugAarMetadata` 失败**
   - 原因：依赖版本比 AGP 要求的还新
   - 处理：降级：`coreKtx 1.10.1`、`lifecycle 2.6.1`、`espresso 3.5.1`

5. **Gradle 下载卡住 / 极慢**
   - 原因：走代理 + 官方源
   - 处理：wrapper 换腾讯云镜像，`settings.gradle.kts` 加阿里云镜像

6. **资源合并报错**
   - 原因：`res/` 目录里放了非资源文件（如 `.bak`）
   - 处理：备份文件不要放 `res/` 下