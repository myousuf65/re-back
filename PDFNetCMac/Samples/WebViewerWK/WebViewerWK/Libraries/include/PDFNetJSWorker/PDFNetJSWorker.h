#pragma once

#include <stdlib.h>

#if defined(_WIN32)
#   define PJSW_CALL __cdecl
#   define PJSW_EXPORT __declspec(dllexport)
#else
#   define PJSW_CALL
#   define PJSW_EXPORT __attribute__((visibility("default")))
#endif
#define PJSW_API PJSW_EXPORT PJSW_Error PJSW_CALL
#define PJSW_NO_ERROR 0

#ifdef __cplusplus
extern "C"
{
#endif // __cplusplus

    typedef struct _PJSW_Error* PJSW_Error;
    typedef void(*PJSW_CallbackWrapper)(const char* const callbackArg, void* userData);

    PJSW_API PJSW_Initialize();
    PJSW_API PJSW_InitializeWithPaths(const char* const tempPath, const char* const cachePath);
    PJSW_API PJSW_Terminate();
    PJSW_API PJSW_GetErrorMessage(PJSW_Error error, char* buffer, size_t bufferLength);
    PJSW_API PJSW_CleanError(PJSW_Error* error);
    PJSW_API PJSW_GetProcessId(size_t* result);
    PJSW_API PJSW_GenerateImageRenderFilePath(char* result, size_t resultLength, size_t* bytesWritten);
    PJSW_API PJSW_GenerateNextImageRenderFilePath(const char* const sourcePath, size_t nextNumber, char* result, size_t resultLength, size_t* bytesWritten);
    PJSW_API PJSW_DeleteGeneratedImageRenderFile(const char* const imageRenderFilePath);
    PJSW_API PJSW_AddEventListener(const char* const eventName, PJSW_CallbackWrapper eventHandler);
    PJSW_API PJSW_AddEventListenerWithUserData(const char* const eventName, PJSW_CallbackWrapper eventHandler, void* userData);
    PJSW_API PJSW_RemoveEventListener(const char* const eventName);
    PJSW_API PJSW_PostMessage(const char* const message);

    // Asynchronous API
    typedef void(*PJSW_OnGetProcessId)(size_t errorCode, const char* const errorMessage, size_t pid);
    PJSW_API PJSW_GetProcessIdAsync(PJSW_OnGetProcessId callback);

    typedef void(*PJSW_OnDeleteGeneratedImageRenderFile)(size_t errorCode, const char* const errorMessage);
    PJSW_API PJSW_DeleteGeneratedImageRenderFileAsync(const char* const imageRenderFilePath, PJSW_OnDeleteGeneratedImageRenderFile callback);

#ifdef __cplusplus
}
#endif // __cplusplus
