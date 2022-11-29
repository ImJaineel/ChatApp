package dev.jaineelpetiwale.chatapp

class User(var email: String?, var name: String?, var uid: String?, var displaypic: String?) {

    constructor() : this("", "", "", "")
}