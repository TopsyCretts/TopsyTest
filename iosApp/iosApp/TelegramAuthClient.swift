//
//  TelegramAuthClient.swift
//  iosApp
//
//  Created by Gleb Borisov on 23.07.2025.
//


import Foundation
import TDLibKit
import UIKit
import ComposeApp

class TDLibAuthManualClient {
    private let manager = TDLibClientManager()
    private var client: TDLibClient!
    
    var clientState: MutableValue<MessengerClientAuthenticationState> = mutableValue(MessengerClientAuthenticationState.UNKNOWN())

    init() {
        client = manager.createClient { [weak self] data, _ in
            self?.handleUpdate(data: data)
        }
    }

     private func sendTdlibParameters() async {
        let fileManager = FileManager.default
        let docsURL = fileManager.urls(for: .documentDirectory, in: .userDomainMask)[0]
        let tdlibDir = docsURL.appendingPathComponent("tdlib")
        let filesDir = tdlibDir.appendingPathComponent("files")
        
        let apiIdString = Bundle.main.infoDictionary?["TDLIB_API_ID"] as? String ?? "0"
        let apiId = Int(apiIdString) ?? 0
        let apiHash = Bundle.main.infoDictionary?["TDLIB_API_HASH"] as? String ?? ""
         
         try? await client.setTdlibParameters(
            apiHash: apiHash,
            apiId: apiId,
            applicationVersion: "1.0",
            databaseDirectory: tdlibDir.path,
            databaseEncryptionKey: Data(),
            deviceModel: UIDevice.current.model,
            filesDirectory: filesDir.path,
            systemLanguageCode: Locale.current.languageCode ?? "en",
            systemVersion: UIDevice.current.systemVersion,
            useChatInfoDatabase: true,
            useFileDatabase: true,
            useMessageDatabase: true,
            useSecretChats: false,
            useTestDc: false,
            completion: { result in
                
            }
         )
    }

    func sendPhoneNumber(_ phoneNumber: String) async {
        try?  client.setAuthenticationPhoneNumber(
            phoneNumber: phoneNumber,
            settings: PhoneNumberAuthenticationSettings(allowFlashCall: false, allowMissedCall: false, allowSmsRetrieverApi: false, authenticationTokens: [], firebaseAuthenticationSettings: nil, hasUnknownPhoneNumber: false, isCurrentPhoneNumber:  false),
            completion: { result in
                
            })
    }

    func sendCode(_ code: String) async {
        try? client.checkAuthenticationCode(
            code: code,
            completion: { result in
                
            }
        )
    }


    func sendPassword(_ password: String) async {
        try? client.checkAuthenticationPassword(
            password: password,
            completion: { result in
                
            }
        )
    }


    private func handleUpdate(data: Data) {
        do {
            let update = try client.decoder.decode(Update.self, from: data)

            switch update {
            case .updateAuthorizationState(let state):
                handleAuthState(state.authorizationState)

            default:
                break
            }

        } catch {
            print("❌ Update parse error: \(error)")
        }
    }
    
    func close(){
        Task{
            try? await client.close()
        }
    }

    private func handleAuthState(_ state: AuthorizationState) {
        switch state {
        case .authorizationStateWaitTdlibParameters:
            print("⚙️ Waiting TDLib parameters...")
            clientState.value = MessengerClientAuthenticationState.UNAUTHENTICATED()
            Task {
                await sendTdlibParameters()
            }

        case .authorizationStateWaitPhoneNumber:
            print("📱 Enter phone number (call `sendPhoneNumber()` in code)")
            clientState.value = MessengerClientAuthenticationState.WAIT_FOR_NUMBER()
            Task {
                await sendPhoneNumber("")
            }
            

        case .authorizationStateWaitCode(let info):
            print("📩 Enter code (via `sendCode()`) — sent via \(info.codeInfo.type)")
            clientState.value = MessengerClientAuthenticationState.WAIT_FOR_CODE()
            Task {
                await sendCode("")
            }

        case .authorizationStateWaitPassword:
            print("🔒 Enter 2FA password (via `sendPassword()`)")
            clientState.value = MessengerClientAuthenticationState.WAIT_FOR_PASSWORD()
            Task {
                await sendPassword("" )
            }

        case .authorizationStateReady:
            clientState.value = MessengerClientAuthenticationState.AUTHENTICATED()
            print("✅ Authorized successfully!")

        case .authorizationStateClosed:
            clientState.value = MessengerClientAuthenticationState.UNAUTHENTICATED()
            print("❌ Client closed")

        default:
            clientState.value = MessengerClientAuthenticationState.UNAUTHENTICATED()
            print("Unhandled auth state: \(state)")
        }
    }

}

