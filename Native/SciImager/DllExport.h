#pragma once

#ifndef IMAGER_API
#ifdef SCIIMAGER_DLL_EXPORT
#define IMAGER_API __declspec(dllexport)
#else
#define IMAGER_API __declspec(dllimport)
#endif
#endif