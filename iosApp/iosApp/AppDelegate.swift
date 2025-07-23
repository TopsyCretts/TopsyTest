//
//  AppDelegate.swift
//  iosApp
//
//  Created by Gleb Borisov on 23.07.2025.
//

import Foundation
import ComposeApp
import UIKit

class AppDelegate: NSObject, UIApplicationDelegate {
    
    override init(){
        MessengerComponent_iosKt.messengerClient = MessengerClientImpl()
    }
    
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        // Здесь можно инициализировать что угодно, например TDLib или DI
        print("AppDelegate didFinishLaunchingWithOptions")

        return true
    }

    func applicationWillTerminate(_ application: UIApplication) {
        // Вызывается при завершении приложения
        print("AppDelegate applicationWillTerminate")
        // telegramClient.close() — если доступен глобально или через shared singleton
    }
}
