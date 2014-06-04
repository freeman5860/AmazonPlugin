/**
 * © 2012-2013 Amazon Digital Services, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy
 * of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

/**
 * PlayerClientInterface.h
 *
 * Client interface class for requesting information from the Amazon Games Profiles service.
 */

#ifndef __PLAYER_CLIENT_INTERFACE_H_INCLUDED__
#define __PLAYER_CLIENT_INTERFACE_H_INCLUDED__

#include "AGSClientCommonInterface.h"

namespace AmazonGames {

    //************************************
    // Data access structures
    //************************************
    struct PlayerInfo {
        const char* playerId;
        const char* alias;
        const char* avatarUrl;

        PlayerInfo()
            : alias(0), playerId(0), avatarUrl(0)
        {}
    };

    //************************************
    // Callback classes
    //************************************
    class IGetLocalPlayerCb : public ICallback {
    public:
        virtual void onGetLocalPlayerCb(
                    ErrorCode errorCode,
                    const PlayerInfo* responseStruct,
                    int developerTag) = 0;
    };

    //************************************
    // Handle classes
    //************************************

    // All Handle classes have these functions:
    //    HandleStatus getHandleStatus();
    //    ErrorCode getErrorCode();
    //    int getDeveloperTag();

    class IGetLocalPlayerHandle : public IHandle {
    public:
        virtual const AmazonGames::PlayerInfo* getResponseData() = 0;

        virtual IGetLocalPlayerHandle* clone() const = 0;
    };

    //************************************
    // Listener classes
    //************************************

    class ISignedInStateChangedListener : public ICallback {
    public:
        virtual void onSignedInStateChanged(bool isSignedIn) = 0;
    };

    //************************************
    // Player Client Interface
    //************************************

    class PlayerClientInterface {

    public:
        //************************************
        // Callbacks
        //************************************
        static void getLocalPlayer(
                IGetLocalPlayerCb* const callback,
                int developerTag = 0);

        //************************************
        // Handles
        //************************************
        static HandleWrapper<IGetLocalPlayerHandle> getLocalPlayer(
                int developerTag = 0);

        //************************************
        // Listeners
        //************************************
        static void setSignedInStateChangedListener(
                ISignedInStateChangedListener* signedInStateChangedListener);

        //************************************
        // Poll function for signed in
        //************************************
        static bool isSignedIn();
    };
}

#endif
