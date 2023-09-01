LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_SRC_FILES := \
		tracerdemo.cxx
LOCAL_MODULE := libtracerdemo
LOCAL_CPPFLAGS := -Wall -std=gnu++17 -frtti
LOCAL_LDLIBS := -llog
LOCAL_SHARED_LIBRARIES := libtracerdemo
NDK_TOOLCHAIN_VERSION := clang
include $(BUILD_SHARED_LIBRARY)
