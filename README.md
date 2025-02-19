# mini-gemini-front

.
└── black
    └── bracken
        └── mini_gemini_front
            ├── App.kt
            ├── data
            │   ├── infra
            │   │   ├── conv
            │   │   │   ├── AiInteractiveChatConverter.kt
            │   │   │   └── GeminiEntityConverter.kt
            │   │   ├── GeminiInteractiveRequest.kt
            │   │   ├── GeminiInteractiveResponse.kt
            │   │   ├── GeminiTextStreamRequest.kt
            │   │   ├── GeminiTextStreamResponse.kt
            │   │   └── room
            │   │       ├── AppDatabase.kt
            │   │       ├── dao
            │   │       │   └── InteractiveHistoryDao.kt
            │   │       └── entity
            │   │           └── InteractiveHistory.kt
            │   └── kernel
            │       ├── AiInteractiveChat.kt
            │       └── AiTextStreamPart.kt
            ├── di
            │   ├── CoreModule.kt
            │   ├── HttpModule.kt
            │   └── RoomModule.kt
            ├── repository
            │   └── GeminiRepository.kt
            ├── ui
            │   ├── main
            │   │   ├── MainActivity.kt
            │   │   ├── MainScreen.kt
            │   │   ├── MainUiAction.kt
            │   │   ├── MainUiState.kt
            │   │   └── MainViewModel.kt
            │   └── theme
            │       ├── Color.kt
            │       ├── Theme.kt
            │       └── Type.kt
            └── util
                └── MgfJson.kt
