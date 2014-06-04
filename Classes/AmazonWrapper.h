//
//  VungleWrapper.h
//  PowersOfTwo
//
//  Created by Freeman on 14-4-19.
//
//

#ifndef AMAZON_WRAPPER_H_
#define AMAZON_WRAPPER_H_

class AmazonWrapper{
    
public:
    static AmazonWrapper * getInstance();
    
    // show banner ad
    void showAd(const char * app_key);
    // post achievement
    void postAchievement(const char * id, float value);
    
};

#endif /* defined(AMAZON_WRAPPER_H_) */
