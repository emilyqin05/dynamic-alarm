## System Architecture

```mermaid
flowchart LR
    subgraph UI["Client Android App"]
        A["MainActivity (Kotlin - Jetpack Compose)"]
        B["AlarmItem (Data Model)"]
        C["AlarmScheduler (Interface)"]
        A -->|Creates| B
        A -->|Calls| C
    end

    D["AndroidAlarmScheduler (Implementation)"]
    C -->|Implemented by| D

    subgraph OS["Android OS"]
        E["AlarmManager"]
        F["PendingIntent"]
        G["AlarmReceiver (BroadcastReceiver)"]
    end

    D -->|Uses| E
    D -->|Creates| F
    F -->|Triggers| G
    E -->|Fires at time| G

    subgraph Worker["Background Service - Worker"]
        H["Trip Time Worker"]
    end

    subgraph External["External Service"]
        I["Google Directions API"]
    end

    H -->|Queries| I
    I -->|Travel duration| H
    H -->|Recalculates time| B
    H -->|Cancel and Reschedule| C

    G -->|Sound - Vibration - Log| J["User Notification"]
