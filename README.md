# SMS Sender ID Spoofer Xposed Module

An Xposed module for LSposed that allows users to spoof the sender ID of incoming SMS messages on rooted Android devices (8.0+).

## Features

- Spoof sender ID of incoming SMS messages
- Manage sender IDs with predefined options and custom ID creation
- Enable/disable module functionality
- Import/export sender ID lists
- Material Design UI
- Room database for persistent storage
- Xposed hook for SMS interception

## Requirements

- Rooted Android device with LSposed installed
- Android 8.0 (API 26) or higher
- Xposed Framework

## Installation

1. Download the latest release APK from the [Releases](#) section
2. Install the APK on your device
3. Enable the module in LSposed Manager
4. Reboot your device
5. Open the app to configure sender IDs

## Usage

1. Open the SMS Spoofer app
2. Toggle the "Enable Module" switch to activate/deactivate the spoofing
3. Use the "+" button to add new sender IDs
4. Toggle individual sender IDs to enable/disable them
5. The active sender ID will be used for all incoming SMS messages

## Development

### Prerequisites

- Android Studio Arctic Fox or later
- JDK 11
- Android SDK 34

### Building

```bash
# Clone the repository
git clone https://github.com/yourusername/sms-spoofer.git
cd sms-spoofer

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease
```

### Architecture

The module follows a clean architecture pattern:

- **UI Layer**: Activities, adapters, and dialogs
- **Data Layer**: Room database, repositories, and DAOs
- **Xposed Layer**: Module entry point and hook handlers
- **Model Layer**: Data classes
- **Utility Layer**: Helpers and constants

### Xposed Hooking

The module hooks into `com.android.internal.telephony.InboundSmsHandler.dispatchIntent` to modify the sender ID of incoming SMS messages before they are processed by the system.

## Security and Privacy

- All processing is done locally on the device
- No data leaves the device
- No internet permissions required
- Clear UI indication when module is active
- Option to disable module temporarily

## Compatibility

- Tested on AOSP-based ROMs
- May require adjustments for heavily skinned devices (Samsung, Xiaomi, etc.)
- Hook methods may need version-specific implementations
- User should be warned about potential issues with custom ROMs

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Disclaimer

This module is intended for educational purposes only. Spoofing SMS sender IDs may be illegal in certain jurisdictions. Use this module responsibly and in accordance with local laws and regulations.

The developers are not responsible for any misuse of this module.
