#pragma once

#ifdef __APPLE__
#ifndef IPCV_API
#define IPCV_API
#endif
#else

#ifndef IPCV_API
#ifdef IPCV_EXPORT
#define IPCV_API __declspec(dllexport)
#else
#define IPCV_API __declspec(dllimport)
#endif
#endif

#endif
