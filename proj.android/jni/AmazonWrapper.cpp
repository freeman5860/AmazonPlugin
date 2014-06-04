#include "AmazonWrapper.h"
#include <jni.h>
#include "platform/android/jni/JniHelper.h"

using namespace cocos2d;

static AmazonWrapper * s_Instance = NULL;

AmazonWrapper * AmazonWrapper::getInstance(){
	if(s_Instance == NULL){
		s_Instance = new AmazonWrapper();
	}

	return s_Instance;
}

void AmazonWrapper::showAd(const char * app_key){
	JniMethodInfo minfo;
	bool isHave = JniHelper::getStaticMethodInfo(minfo,
			"com/freeman/cocos2dx/AmazonPlugin","showAd","(Ljava/lang/String;)V");
	if(!isHave){
			//CCLog("jni:openURL 函数不存在");
	}else{
        jstring jkey = minfo.env->NewStringUTF(app_key);
		minfo.env->CallStaticVoidMethod(minfo.classID,minfo.methodID,jkey);
	}
}

void AmazonWrapper::postAchievement(const char * id, float value){
    JniMethodInfo minfo;
	bool isHave = JniHelper::getStaticMethodInfo(minfo,
            "com/freeman/cocos2dx/AmazonPlugin","postAchievement","(Ljava/lang/String;F)V");
	if(!isHave){
        //CCLog("jni:openURL 函数不存在");
	}else{
        jstring jid = minfo.env->NewStringUTF(id);
        jfloat jval = value;
		minfo.env->CallStaticVoidMethod(minfo.classID,minfo.methodID,jid,jval);
	}
}