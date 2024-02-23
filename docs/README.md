# Chatbox

***English*** | [简体中文](README_SC.md)

## Introduction

Chatbox is an Android app that using OSC to help players send Chatbox messages to VRChat.

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

## Features

- Helps VR players send messages quickly from their cell phones
- Provides a floating button to open the Chatbox in VRChat Mobile with one click
- Quickly edit sent messages
- Quickly repeat messages that others have not seen, or that expired
- Long press the send button to clear and stash the inputted message
- Provides real-time sending to instantly synchronize inputs

## Download

[Github Releases](https://github.com/ScrapW/Chatbox/releases) is the only source where you can get official Chatbox downloads.

## Instructions

### Send messages from your phone to your PC client

1. Make sure the IP address of your PC is reachable (within the same network, or your phone is connected to your PC hotspot).
2. Set the destination IP address to your PC's IP address.
3. Enable OSC function in the game

> [!IMPORTANT]  
> Ensure that the communication condition from your phone to your PC is good. If there are packet losses, some messages may not be sent.

---

### Sending messages with one click in VRChat Mobile using floating button

1. Grant `Display over other app` permission for this app
2. Set the destination IP address to `127.0.0.1` or turn on the option `Send to localhost` (recommended)
3. Enable OSC function in the game

## Known issues

- Text field cursor have abnormal behavior

This is a bug related to the Android Compose library. Need to wait for an upstream fix.

## FAQ

### How to enable the OSC function?

`Action menu (Press R) > Options > OSC > Enabled`

If enabled successfully, the avatar you are using should reload.

### Failed to send message

- Check if your phone and computer are within the same network and make sure you have found the correct IP address.
- Check if OSC is enabled successfully

### Failed to enable OSC

In most cases, the port is occupied by a residual process of VRChat.<br>
You can check if `VRChat.exe` `install.exe` process exists in the background and end the process, then try to open it again.<br>
If it fails, it is recommended to restart your computer and try again, or search on your own how to fix port occupancy.

### Feedback / Need Help ?

Go to [Issues](https://github.com/ScrapW/Chatbox/issues) for this repository.

---

*The project name is temporary and may change in the future.*