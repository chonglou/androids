Android APP项目
=========

### 开发环境（for ArchLinux）

#### x86_64环境设置
 * vi /etc/pacman.conf，增加
	
	[multilib]
	Include = /etc/pacman.d/mirrorlist

 * 安装lib32依赖

	pacman -S java-environment swt lib32-alsa-lib lib32-openal lib32-libstdc++5 lib32-libxv lib32-mesa lib32-ncurses lib32-sdl lib32-zlib


#### 软件包列表
 * https://aur.archlinux.org/packages/an/android-sdk/android-sdk.tar.gz
 * https://aur.archlinux.org/packages/an/android-sdk-platform-tools/android-sdk-platform-tools.tar.gz
 * https://aur.archlinux.org/packages/an/android-sdk-build-tools/android-sdk-build-tools.tar.gz

#### .bashrc中增加
    ANDROID_HOME=/opt/android-sdk
    PATH=$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools:$PATH
    export ANDROID_HOME PATH

#### 创建项目
	android sdk # 安装target
	android list targets # 列出target
	android create project --target <target-id> --name MyFirstApp --path <path-to-workspace>/MyFirstApp --activity MainActivity --package com.example.myfirstapp

### 设备启动调试模式
 * android版本小于3.2 点击 设置->应用->开发
 * android本本大于4.0小于4.2 点击 设置->开发者选项
 * android版本大于4.2 开发者选项默认隐藏。如果需要显示，需要点击 设置->关于手机，并连续点击版本号7次，然后返回上一页才可以看到开发者选项。
 * 创建文件/etc/udev/rules.d/51-android.rules(04e8是samsung厂商ID，替换成你自己的 http://developer.android.com/intl/zh-cn/tools/device.html#VendorIds)

	SUBSYSTEM=="usb", ATTR{idVendor}=="04e8", MODE="0666", GROUP="plugdev"

 * 运行chmod a+r /etc/udev/rules.d/51-android.rules


### 模拟器
    dd bs=1M count=512 if=/dev/zero of=tmp/sd0 # 创建sd卡文件
    android avd # 创建并启动

### 调试
		./install.sh RssReader # 调试RssReader
		adb kill-server # 停止调试器

### 编译release版
	cd RssReader
	ant release

### 常见问题
 * Failure [INSTALL_FAILED_ALREADY_EXISTS]

    adb install -r PATH_TO.apk
