LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_PACKAGE_NAME := UMStoreModeService
LOCAL_STATIC_JAVA_LIBRARIES := Hitv DvbPlayer DvbStorage android-support-v4
LOCAL_CERTIFICATE := platform

LOCAL_OVERRIDES_PACKAGES := platform

LOCAL_PROGUARD_ENABLED := disabled

ALL_DEFAULT_INSTALLED_MODULES += $(LOCAL_PACKAGE_NAME)

include $(BUILD_PACKAGE)
include $(call all-makefiles-under,$(LOCAL_PATH))