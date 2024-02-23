# Chatbox

[English](README.md) | ***简体中文***

## 概述

Chatbox 是一款利用 OSC 帮助玩家向 VRChat 发送 Chatbox 消息的 Android 应用。

<table>
<tr>
    <td>
        <img src="https://github.com/ScrapW/Chatbox/assets/19533993/ebc20616-f238-4dd9-ac2e-abb13afc64a0" alt="Screebshot 1">
    </td>
    <td>
        <img src="https://github.com/ScrapW/Chatbox/assets/19533993/bcad66e6-bd92-49e1-8699-c3774379654b" alt="Screebshot 2">
    </td>
    <td>
        <img src="https://github.com/ScrapW/Chatbox/assets/19533993/f353f2ea-c490-4a95-9bb6-b36aa7e51950" alt="Screebshot 3">
    </td>
    <td>
        <img src="https://github.com/ScrapW/Chatbox/assets/19533993/aeb7aa91-7d55-44e3-81fe-bbc84e58bae3" alt="Screebshot 4">
    </td>
</tr>
</table>

## 特点

- 通过手机，帮助 VR 玩家快速发送中文消息
- 提供悬浮按钮，用于在 VRChat Mobile 中一键开启 Chatbox
- 快速编辑发送过的消息
- 快速重复他人未看清，或是过期的消息
- 长按发送按钮，清空并暂存输入的消息
- 提供实时发送功能，即时同步输入的内容

## 下载

本项目的 [Github releases](https://github.com/ScrapW/Chatbox/releases) 是唯一官方提供的下载地址。

## 使用说明

### 通过手机向电脑客户端发送消息

1. 确保电脑 IP 地址是可达的 (于同一网络内，或手机连接电脑热点)
2. 设置目标 IP 地址为你电脑的 IP 地址
3. 于游戏内开启 OSC 功能

> [!IMPORTANT]  
> 确保手机到电脑的通信条件良好，若存在丢包，可能会导致部分消息发送失败。

---

### 利用悬浮按钮在 VRChat Mobile 中一键发送消息

1. 为本应用授予 `显示在其他应用的上层 (悬浮窗)` 权限
2. 设置目标 IP 地址为 `127.0.0.1`，或是打开选项 `发送至 localhost` （推荐）
3. 于游戏内开启 OSC 功能

## FAQ

### 如何开启 OSC 功能?

`Action menu (R 键菜单) > Options > OSC > Enabled`

如果开启成功，你所使用的的 Avatar 应当会重新载入一下。

### 消息发送失败

- 检查手机和电脑是否在同一网络内，并确保你找到了正确的 IP 地址。
- 检查 OSC 是否开启成功

### OSC 开启失败

一般情况下，是端口被 VRChat 的残留进程占用了。<br>
可以检查后台是否存在 `VRChat.exe` `install.exe` 进程并结束进程，再重新尝试开启。<br>
若失败，建议重启电脑再次尝试，或自行搜索如何解决端口占用。

### 问题反馈 / 需要帮助 ?

前往本项目的 [Issues](https://github.com/ScrapW/Chatbox/issues) 。

---

*项目名称是临时的，并可能会在未来修改。*