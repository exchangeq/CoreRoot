# CoreRoot · 原创 Root 权限管理器

> 掌控核心权限，稳定而克制。

CoreRoot 是一个从零原创的 Android 特权管理应用，采用 **Kotlin + C(NDK) + CMake** 构建，
不捆绑、不内置任何已知厂商的私有框架或二进制。原生层为独立 C 实现，负责设备信息读取、
Root 探测与受控命令执行；UI 层基于 AndroidX 与 Material3。

- 包名：`io.coreroot.manager`
- 版本：`1.0.0` (versionCode 1)
- minSdk：26 · targetSdk / compileSdk：34
- 支持 ABI：`arm64-v8a` · `armeabi-v7a` · `x86_64`
- 协议：**GPL-3.0-only**（见 [LICENSE](LICENSE)）

---

## 功能一览

| 模块 | 说明 |
| --- | --- |
| 设备概览 | Root 就绪状态、架构、内核、系统版本、厂商机型、硬件平台、原生库版本；允许 / 拒绝 / 待询问 / 启用模块统计 |
| 应用授权管理 | 按应用管理「允许 / 拒绝 / 询问」特权状态，记录仅存本机，不联网上传 |
| 原创模块 | 内置原创能力模块，开关即时生效，状态本地持久化 |
| 特权终端 | 通过 NDK 桥接执行受控 shell 命令，回显退出码与输出；原生调用置于后台线程，避免阻塞 UI |
| 设置 | 深色模式跟随、授权二次确认、调试日志开关、关于、签名方案、开源许可与设计理念 |

## 设计理念

**标志——核心徽章**

- 六边形外廓象征**守护与边界**：特权操作被约束在清晰的安全边界内。
- 内部根系分叉象征对系统底层的**掌控与延伸**：由主干向多级细枝展开，代表权限由核心向外有序分发。
- 顶端节点标记能力的「入口」，主干向下延伸至底层。

**配色与版式**

- 深墨蓝 (`#0B1220` / `#111C2E`) 奠定权威、沉稳与专业感。
- 青绿锋芒 (`#2EE6A8`) 与天青 (`#38BDF8`) 点缀活力与科技感。
- 圆角卡片、克制留白与统一基准网格，保证在不同尺寸屏幕上对齐清晰、不易错位；
  长文本滚动收纳，信息层级以字重与颜色区分。

## 工程结构

```
CoreRoot/
├── app/
│   ├── src/main/
│   │   ├── java/io/coreroot/manager/
│   │   │   ├── ui/          # Activity / Fragment / Adapter
│   │   │   ├── data/        # 数据模型与本地仓库(SP)
│   │   │   ├── core/         # 设备信息采集
│   │   │   └── nat/          # JNI 桥接(Kotlin 侧)
│   │   ├── cpp/              # C 原生层 + CMake
│   │   └── res/             # 矢量图标、主题、布局、字符串
│   └── build.gradle
├── settings.gradle
├── build.gradle
└── LICENSE
```

## 本地构建

```bash
# Debug
./gradlew assembleDebug

# Release（使用本机 keystore 签名，v1 + v2）
./gradlew assembleRelease
```

产物位于：

- `app/build/outputs/apk/debug/app-debug.apk`
- `app/build/outputs/apk/release/app-release.apk`

### 签名说明

Release 包使用独立生成的 RSA 2048 密钥库签名（`v1 + v2` 签名方案）。
密钥库**不入库**；签名口令通过环境变量 `COREROOT_STORE_PASS` / `COREROOT_KEY_PASS` 注入，
未在仓库中以明文保存。

## 开源与第三方声明

- 本项目以 **GPL-3.0-only** 发布：你可自由使用、修改与再分发，衍生作品须同样以 GPL-3.0 开源。
- 第三方组件许可：AndroidX、Material Components、Kotlin Standard Library 均为 Apache-2.0。
- 应用内「设置 → 开源许可」页包含完整协议文本与署名。

## 免责声明

Root 与特权操作可能影响设备稳定性与保修。本应用仅作为权限管理框架，
不提供、不引导任何绕过系统安全机制或侵害他人设备的能力；请在合规、自愿且自担风险的前提下使用。
