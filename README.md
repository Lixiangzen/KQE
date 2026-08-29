# KQE 科启数码英语（KeQi English）

一款**原生 Android** 英语背单词 App（Kotlin + Jetpack Compose），非 WebView / HTML 套壳。适配 Android 8（minSdk 26）与华强北手表方形小屏。

## 功能

- 首次启动选单词书（初中 / 高中 / 高三重点）
- 主学习页仪表盘：单词书统计（已学/总词数/剩余）、每日任务进度、答错统计、激励文案
- 单词练习：四选一释义答题、即时判分、答案解析卡、单词发音
- 词典：搜索查询任意单词的释义与发音
- 无尽练习：不限量随机刷题，支持「看英文选中文 / 看中文选英文」双模式
- 设置：更换单词书、每日练习数量（10/20/30/50）、清除学习记录（二次确认）
- 关于应用：品牌卡片与简介

## 技术栈

| 项 | 版本 |
| --- | --- |
| 语言 | Kotlin 1.9.24 |
| UI | Jetpack Compose（BOM 2024.06.00） |
| 构建 | AGP 8.5.2 + Gradle 8.7 |
| 最低系统 | Android 8.0（API 26） |
| 数据存储 | Room（学习进度）+ DataStore（设置） |
| JSON | Gson |

## 如何构建 APK

**方式一（推荐）：Android Studio**

1. 用 Android Studio（Hedgehog 及以上）打开本目录 `KqeEnglish`。
2. 等待 Gradle 同步完成（首次会自动下载依赖，需联网）。
3. 菜单 `Build → Build Bundle(s) / APK(s) → Build APK(s)`。
4. 产物在 `app/build/outputs/apk/debug/app-debug.apk`。

**方式二：命令行**

```bash
# 本机需已安装 JDK 17
./gradlew assembleDebug
# Windows 下用 gradlew.bat assembleDebug
```

## 词库数据（重要）

`app/src/main/assets/` 下内置三个词库 JSON，当前为**精选真实词条**（每库 40 个，字段：`en` 英文 / `phonetic` 音标 / `pos` 词性 / `cn` 释义 / `example` 例句 / `exampleCn` 例句翻译）。

要替换为完整词表，直接覆盖同名文件即可，字段结构保持不变：

```json
[
  {"en":"abandon","phonetic":"/əˈbændən/","pos":"v.","cn":"放弃",
   "example":"Never abandon your dream.","exampleCn":"永远不要放弃你的梦想。"}
]
```

- `words_chuzhong.json` → 初中英语（目标 1600 词）
- `words_gaozhong.json` → 高中英语（目标 3500 词）
- `words_gaosan.json` → 高三重点英语（目标 1200 高频词）

> 说明：主页「总词数 / 剩余」按**实际加载到的词条数**统计，保证进度可真实完成；词库说明文案（如「高中 3500 词」）仍按目标词数展示。

## 方形手表适配

- 全局深色主题，固定配色（背景 `#0A0F1E`、主蓝 `#3B82F6` 等）。
- `MainActivity` 内置自适应密度：屏幕宽度 < 300dp 时整体缩放 dp，避免小方屏布局溢出。
- 所有页面可滚动，触控区域 ≥ 44dp。

## 目录结构

```
app/src/main/
├── AndroidManifest.xml
├── assets/               # 词库 JSON（可替换）
└── java/com/kqe/english/
    ├── MainActivity.kt   # 入口 + 方形屏自适应
    ├── MainViewModel.kt  # 全局状态
    ├── data/             # 模型 / 仓库 / Room / DataStore
    ├── ui/
    │   ├── theme/        # 设计令牌（颜色/字体）
    │   ├── components/   # 通用组件
    │   ├── navigation/   # 路由
    │   └── screens/      # 8 个页面
    └── util/             # TTS 发音
```
