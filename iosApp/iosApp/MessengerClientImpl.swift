import Foundation
import ComposeApp
import TDLibKit
import Combine

final class MessengerClientImpl: MessengerClient {
    
    private let tdClient = TDLibAuthManualClient()
    
    var authState: Value<MessengerClientAuthenticationState> {
        return tdClient.clientState
    }

    func insertPhoneNumber(number: String) async throws {
        Task {
            await tdClient.sendPhoneNumber(number)
        }
    }

    func insertCode(code: String) async throws  {
        Task {
            await tdClient.sendCode(code)
        }
    }

    func insertPassword(password: String) async throws  {
        Task {
            await tdClient.sendPassword(password)
        }
    }

    func close() {
        tdClient.close()
    }


}

