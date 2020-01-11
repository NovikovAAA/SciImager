#pragma once

#ifndef IPCV_API
#ifdef IPCV_EXPORT
#define IPCV_API __declspec(dllexport)
#else
#define IPCV_API __declspec(dllimport)
#endif
#endif