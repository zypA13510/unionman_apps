
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-subdir-java-files)
# LOCAL_LDFLAGS := samba

LOCAL_JAVA_LIBRARIES :=

LOCAL_STATIC_JAVA_LIBRARIES := NetShare
LOCAL_STATIC_JAVA_LIBRARIES += HiBDInfo
LOCAL_STATIC_JAVA_LIBRARIES += SDKInvoke
LOCAL_STATIC_JAVA_LIBRARIES += HiDVDInfo

LOCAL_JNI_SHARED_LIBRARIES := libandroid_runtime

LOCAL_PACKAGE_NAME := UMFileManager
ALL_DEFAULT_INSTALLED_MODULES += $(LOCAL_PACKAGE_NAME)

LOCAL_CERTIFICATE := platform

LOCAL_PROGUARD_ENABLED := disabled

include $(BUILD_PACKAGE)

