/**
 *  The one thermostat to rule them all
 *
 *  Copyright 2014 Eric Roberts
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "The one thermostat to rule them all",
    namespace: "baldeagle072",
    author: "Eric Roberts",
    description: "A thermostat for everything in your house",
    category: "Green Living",
    iconUrl: "http://www.williamsmusic.org/images/thermostat.png",
    iconX2Url: "http://www.williamsmusic.org/images/thermostat@2x.png")


preferences {
	page name:"setupInit"
    page name:"setupWelcome"
    page name:"setupConfigure"
    page name:"setupRooms"
    page name:"setupProgram"
    page name:"setupControlPanel"
}

def setupInit() {
	TRACE("setupInit()")
    
    if (state.installed) {
    	return setupControlPanel()
    } else {
    	return setupWelcome()
    }
}


def setupWelcome() {
	TRACE("setupWelcome()")
    def textIntro =
    	"The one thermostat to rule them all is used to control all the heating " +
        "and cooling in your house from one place."
    
    def textNext =
    	"Hit the 'Next' button at the top of the page to continue"
    
    def pageProperties=[
    	name:		"setupWelcome",
        title:		"Welcome!",
        nextPage:	"setupConfigure",
        uninstall:	false
    ]
    
    return dynamicPage(pageProperties) {
    	section("Introduction") {
        	paragraph textIntro
            paragraph textNext
        }
    }
}

def setupConfigure() {
	TRACE("setupConfigure()")
    def helpPage =
    	"Hit the 'Next' button when done."
        
    def helpNumRooms = 
    	"You can have as many rooms as you need. Each room can control an air " +
        "conditioner, a heater, a thermostat, and have a temperature sensor."
    
    def helpModes =
    	"Each mode can have a different set temperature."
    
    def inputNumRooms = [
    	name:			"numRooms",
        type:			"number",
        title:			"How many rooms?",
        defaultValue:	"1",
        required: 		true
    ]
    
    def inputHomeModes = [
    	name:			"homeModes",
        type:			"mode",
        title:			"Home temperature for these modes",
        multiple: 		true,
        required:		true
    ]
    
    def inputAwayModes = [
    	name:			"awayModes",
        type:			"mode",
        title:			"Away temperature for these modes",
        multiple: 		true,
        required:		true
    ]
    
    def inputAsleepModes = [
    	name:			"asleepModes",
        type:			"mode",
        title:			"Asleep temperature for these modes",
        multiple: 		true,
        required:		false
    ]
    
    def pageProperties = [
    	name:		"setupConfigure",
        title:		"Configure",
        nextPage:	"setupRooms",
        uninstall:	state.installed
    ]
    
    return dynamicPage(pageProperties) {
    	section("Numbers") {
        	paragraph helpNumRooms
            input inputNumRooms
        }
        
        section("Modes") {
        	input inputHomeModes
            input inputAwayModes
            input inputAsleepModes
        }
    }
}

def setupRooms() {
	TRACE("setupRooms()")
    
    def helpName =
    	"Choose a name for the room to easily identify it."
        
    def helpThermostat =
    	"Pick a thermostat that controls either heat and/or air conditioning in this room. " +
        "The thermostat will be controlled by its own temperature sensor."
    
    def helpHeater = 
    	"Pick switches that control a heater in this room."
    
    def helpConditioner =
    	"Pick switches that control an air conditioner in this room."
    
    def helpTemp = 
    	"This device will monitor the temperature and control the separate heater and air conditioners." +
        "A temperature sensor is required. It can be the thermostat you used earlier."
    
    
    def pageProperties=[
    	name:		"setupRooms",
        title:		"Configure Rooms",
        install:	true,
        uninstall:	state.installed
    ]
    
    return dynamicPage(pageProperties) {
        
        for (int n = 1; n <= numRooms; n++) {
           section("Room ${n}", hideable:true, hidden:true) {
                paragraph helpName
                input "r${n}_name", "string", title:"Room name", defaultValue:"Room ${n}"
                paragraph helpThermostat
                input "r${n}_thermostat", "capability.thermostat", title:"Which thermostat?", multiple:false, required:false
                paragraph helpHeater
                input "r${n}_heater", "capability.switch", title:"Which heater?", multiple:true, required:false
               	paragraph helpConditioner
                input "r${n}_conditioner", "capability.switch", title:"Which air conditioner?", multiple:true, required:false
               	paragraph helpTemp
                input "r${n}_temp", "capability.temperatureMeasurement", title:"Which temperature sensor?", multiple:false, required:true
             
           }
        }
    }
}

def setupProgram() {
	TRACE("setupProgram()")
    
    def pageProperties=[
    	name:		"setupProgram",
        title:		"Program",
        install:	true,
        uninstall:	state.installed
    ]
    
    return dynamicPage(pageProperties) {
        for (int n = 0; n < state.numRooms; n++) {
        	
            def r = n + 1
            
            def room = state.rooms[n]
            
            def homeSetTemp = room.homeSetTemp
            def awaySetTemp = room.awaySetTemp
            def asleepSetTemp = room.asleepSetTemp
            def homeRun = room.homeRun
            def awayRun = room.awayRun
            def asleepRun = room.asleepRun
            def time1 = room.time1
            def time2 = room.time2
            def time3 = room.time3
            def timeSetTemp1 = room.timeSetTemp1
            def timeSetTemp2 = room.timeSetTemp2
            def timeSetTemp3 = room.timeSetTemp3
            def timeRun1 = room.timeRun1
            def timeRun2 = room.timeRun2
            def timeRun3 = room.timeRun3
            
            def inputHomeSetNewTemp = [
                name:           "r${r}_homeSetTemp",
                type:           "number",
                title:          "Set Home Temperature",
                defaultValue:   homeSetTemp,
                required:       true
            ]
            
            def inputAwaySetNewTemp = [
                name:           "r${r}_awaySetTemp",
                type:           "number",
                title:          "Set Away Temperature",
                defaultValue:   awaySetTemp,
                required:       true
            ]
            
            def inputAsleepSetNewTemp = [
                name:           "r${r}_asleepSetTemp",
                type:           "number",
                title:          "Set Asleep Temperature",
                defaultValue:   asleepSetTemp,
                required:       true
            ]
            
            def inputHomeRun = [
                name:           "r${r}_homeRun",
                type:           "bool",
                title:          "Run home",
                defaultValue:   homeRun,
                required:       true
            ]	
            
            def inputAwayRun = [
                name:           "r${r}_awayRun",
                type:           "bool",
                title:          "Run away",
                defaultValue:   awayRun,
                required:       true
            ]
            
            def inputAsleepRun = [
                name:           "r${r}_asleepRun",
                type:           "bool",
                title:          "Run asleep",
                defaultValue:   asleepRun,
                required:       true
            ]
            
            section(room.name) {
            	input inputHomeSetNewTemp
                input inputHomeRun
                input inputAwaySetNewTemp
                input inputAwayRun
                input inputAsleepSetNewTemp
                input inputAsleepRun
            }
             
            def inputTime1 = [
                name:           "r${r}_time1",
                type:           "time",
                title:          "Set time for time 1",
                defaultValue:   time1,
                required:       false
            ]
            
            def inputTime2 = [
                name:           "r${r}_time2",
                type:           "time",
                title:          "Set time for time 2",
                defaultValue:   time2,
                required:       false
            ]
            
            def inputTime3 = [
                name:           "r${r}_time3",
                type:           "time",
                title:          "Set time for time 3",
                defaultValue:   time3,
                required:       false
            ]
            
            def inputTimeSetTemp1 = [
                name:           "r${r}_timeSetTemp1",
                type:           "number",
                title:          "Set temperature for time 1",
                defaultValue:   timeSetTemp1,
                required:       false
            ]
            
            def inputTimeSetTemp2 = [
                name:           "r${r}_timeSetTemp2",
                type:           "number",
                title:          "Set temperature for time 2",
                defaultValue:   timeSetTemp2,
                required:       false
            ]
            
            def inputTimeSetTemp3 = [
                name:           "r${r}_timeSetTemp3",
                type:           "number",
                title:          "Set temperature for time 3",
                defaultValue:   timeSetTemp3,
                required:       false
            ]
            
            def inputTimeRun1 = [
                name:           "r${r}_timeRun1",
                type:           "bool",
                title:          "Run time 1",
                defaultValue:   timeRun1,
                required:       false
            ]
            
            def inputTimeRun2 = [
                name:           "r${r}_timeRun2",
                type:           "bool",
                title:          "Run time 2",
                defaultValue:   timeRun2,
                required:       false
            ]
            
            def inputTimeRun3 = [
                name:           "r${r}_timeRun3",
                type:           "bool",
                title:          "Run time 3",
                defaultValue:   timeRun3,
                required:       false
            ]
            
           /* section("Set at specific times", hideable:true, hidden:true) {
            	input inputTime1
                input inputTimeSetTemp1
                input inputTimeRun1
                
                input inputTime2
                input inputTimeSetTemp2
                input inputTimeRun2
                
                input inputTime3
                input inputTimeSetTemp3
                input inputTimeRun3
            } */
        }
    }
}

def setupControlPanel() {
	TRACE("setupControlPanel()")
    
    def pageProperties=[
    	name:		"setupControlPanel",
        title:		"Control Panel",
        install:	true,
        uninstall:	true
    ]
    
    return dynamicPage(pageProperties) {
        for (int n = 0; n < state.numRooms; n++) {
        	
            def r = n + 1
            
            def room = state.rooms[n]
            def devices = getRoomDevices(n)
            
            def setTemp = room.setTemp
            def setMode = room.setMode
            
            def currentTemp = 0
            
            if (devices.thermostat) {
            	currentTemp = devices.thermostat.currentTemperature
            } else if (devices.tempMonitor) {
            	currentTemp = devices.tempMonitor.currentTemperature
            }
            
            //log.debug(currentTemp)
            
            def textCurrentTemp = "Current Temperature: ${currentTemp}"
            
            def inputSetNewTemp = [
                name:           "r${r}_setTemp",
                type:           "number",
                title:          "Set Temperature",
                defaultValue:   setTemp,
                required:       true
            ]
            
            def inputSetNewMode = [
                name:           "r${r}_setMode",
                type:           "enum",
                title:          "Mode",
                metadata:       [values:["heat", "cool", "off"]],
                defaultValue:   setMode,
                required:       true
            ]
            
            section(room.name) {
            	paragraph textCurrentTemp
                input inputSetNewTemp
                input inputSetNewMode
            }
        }
        
        section {
            href "setupConfigure", title:"Setup system", description:"Tap to open"
            href "setupRooms", title:"Configure rooms", description:"Tap to open"
            href "setupProgram", title:"Set program", description:"Tap to open"
        }
    }
}

def installed() {
	state.installed = true
	initialize()
}

def updated() {
	unschedule()
	unsubscribe()
	initialize()
}

def initialize() {
	TRACE("initialize()")
    log.debug "settings: ${settings}"
    
    // set global settings
    
    state.numRooms = settings.numRooms.toInteger()
    state.homeModes = settings.homeModes
    state.awayModes = settings.awayModes
    state.asleepModes = settings.asleepModes
    state.rooms = []
    
    // set rooms
    
    for (int n = 0; n < state.numRooms; n++) {
    	state.rooms[n] = roomInit(n)
        makeSubscriptions(n)
    	//makeSchedule(n)
        onOrOffCheck(n)
    }
    
    // set for current mode and subscribe to mode changes
    
    subscribe(location, onLocation)
    
    
}

private def roomInit(n) {
	def r = n + 1
    
    def room = state.rooms[n]
    
    def handlers = [onRoom1, onRoom2, onRoom3, onRoom4, onRoom5, onRoom6, onRoom7, onRoom8]
    
    if (room == null) {
    	room = [:]}
    room.name			= settings."r${r}_name"
    
    if (settings."r${r}_homeSetTemp" == null) {
    	room.homeSetTemp = 72
    } else {
    	room.homeSetTemp = settings."r${r}_homeSetTemp"
    }
    
    if (settings."r${r}_awaySetTemp" == null) {
    	room.awaySetTemp = 72
    } else {
    	room.awaySetTemp = settings."r${r}_awaySetTemp"
    }
    
    if (settings."r${r}_asleepSetTemp" == null) {
    	room.asleepSetTemp = 72
    } else {
    	room.asleepSetTemp = settings."r${r}_asleepSetTemp"
    }
    
    if (settings."r${r}_homeRun" == null) {
    	room.homeRun = false
    } else {
    	room.homeRun = settings."r${r}_homeRun"
    }
    
    if (settings."r${r}_awayRun" == null) {
    	room.awayRun = false
    } else {
    	room.awayRun = settings."r${r}_awayRun"
    }
    
    if (settings."r${r}_asleepRun" == null) {
    	room.asleepRun = false
    } else {
    	room.asleepRun = settings."r${r}_asleepRun"
    }
    
    if (settings."r${r}_setTemp" == null) {
    	room.setTemp = 72
    } else {
    	room.setTemp = settings."r${r}_setTemp"
    }
    
    if (settings."r${r}_setMode" == null) {
    	room.setMode = "off"
    } else {
    	room.setMode = settings."r${r}_setMode"
    }
    
    room.time1 = settings."r${r}_time1"
    room.time2 = settings."r${r}_time2"
    room.time3 = settings."r${r}_time3"
    
    if (settings."r${r}_timeSetTemp1" == null) {
    	room.timeSetTemp1 = 72
    } else {
    	room.timeSetTemp1 = settings."r${r}_timeSetTemp1"
    }
    
    if (settings."r${r}_timeSetTemp2" == null) {
    	room.timeSetTemp2 = 72
    } else {
    	room.timeSetTemp2 = settings."r${r}_timeSetTemp2"
    }
    
    if (settings."r${r}_timeSetTemp3" == null) {
    	room.timeSetTemp3 = 72
    } else {
    	room.timeSetTemp3 = settings."r${r}_timeSetTemp3"
    }
    
    if (settings."r${r}_timeRun1" == null) {
    	room.timeRun1 = false
    } else {
    	room.timeRun1 = settings."r${r}_timeRun1"
    }
    
    if (settings."r${r}_timeRun2" == null) {
    	room.timeRun2 = false
    } else {
    	room.timeRun2 = settings."r${r}_timeRun2"
    }
    
    if (settings."r${r}_timeRun3" == null) {
    	room.timeRun3 = false
    } else {
    	room.timeRun3 = settings."r${r}_timeRun3"
    }
    
    room.handler = handlers[n]
    
    log.debug("Name: " + room.name)
    log.debug("setTemp: " + room.setTemp)
    log.debug("setMode: " + room.setMode)
    
    return room
}

def onRoom1(evt) { tempHandler(0) }
def onRoom2(evt) { tempHandler(1) }
def onRoom3(evt) { tempHandler(2) }
def onRoom4(evt) { tempHandler(3) }
def onRoom5(evt) { tempHandler(4) }
def onRoom6(evt) { tempHandler(5) }
def onRoom7(evt) { tempHandler(6) }
def onRoom8(evt) { tempHandler(7) }

private def onOrOffCheck(n) {
	log.debug("Checking temperature")
    
    def room = state.rooms[n]
    def devices = getRoomDevices(n)
    def setTemp = room.setTemp.toInteger()
    
    log.debug("onoff devices")
    log.debug(devices.conditioner)
    
    log.debug("Room: ${room.name}, SetTemp: ${setTemp}, CurrentTemp: ${devices.tempMonitor.currentTemperature}")
    
    if (room.setMode == "cool") {
    	if (devices.tempMonitor) {
    		if (devices.tempMonitor.currentTemperature >= (setTemp + 1)) {
            	devices.conditioner?.on()
                log.debug("turning on")
                log.debug(devices.conditioner)
            } else if (devices.tempMonitor.currentTemperature <= (setTemp - 1)) {
            	devices.conditioner?.off()
                log.debug("turning off")
            }
    	} else if (devices.thermostat) {
    		if (devices.thermostat.currentTemperature >= (setTemp + 1)) {
            	devices.conditioner?.on()
            } else if (devices.thermostat.currentTemperature <= (setTemp - 1)) {
            	devices.conditioner?.off()
            }
    	} else {
        	log.debug("No way to tell temp")
        }
    } else if (room.setMode == "heat") {
    	if (devices.tempMonitor) {
    		if (devices.tempMonitor.currentTemperature.toInteger() <= (setTemp - 1)) {
            	devices.heater?.on()
            } else if (devices.tempMonitor.currentTemperature.toInteger() >= (setTemp + 1)) {
            	devices.heater?.off()
            }
    	} else if (devices.thermostat) {
    		if (devices.thermostat.currentTemperature.toInteger() <= (setTemp - 1)) {
            	devices.heater?.on()
            } else if (devices.thermostat.currentTemperature.toInteger() >= (setTemp + 1)) {
            	devices.heater?.off()
            }
    	} else {
        	log.debug("No way to tell temp")
        }
    } else {
    	log.debug("Mode set to off")
        devices.heater?.off()
        devices.conditioner?.off()
    }
}

private def makeSubscriptions(n) {
	def room = state.rooms[n]
    def devices = getRoomDevices(n)
    
    subscribe(devices.tempMonitor, "temperature", room.handler)
}

private def makeSchedule(n) {
	def room = state.rooms[n]
    
    def time1 = room.time1
    def time2 = room.time2
    def time3 = room.time3
    def timeSetTemp1 = room.timeSetTemp1
    def timeSetTemp2 = room.timeSetTemp2
    def timeSetTemp3 = room.timeSetTemp3
    def timeRun1 = room.timeRun1
    def timeRun2 = room.timeRun2
    def timeRun3 = room.timeRun3
    
    
    
    
    
    onOrOffCheck(n)
}
    
def tempHandler(n) {
	onOrOffCheck(n)
}

def onLocation(evt) {
	TRACE("onLocation(${evt})")
    
    def mode = evt.value
    if (settings.homeModes?.contains(mode)) {
    	for (int n = 0; n < state.numRooms; n++) {
            def room = state.rooms[n]
            if (room.homeRun) {
                room.setTemp = room.homeSetTemp
            }
            onOrOffCheck(n)
        }
    } else if (settings.awayModes?.contains(mode)) {
    	for (int n = 0; n < state.numRooms; n++) {
            def room = state.rooms[n]
            if (room.awayRun) {
                room.setTemp = room.awaySetTemp
            }
            onOrOffCheck(n)
        }
    } else if (settings.asleepModes?.contains(mode)) {
    	for (int n = 0; n < state.numRooms; n++) {
            def room = state.rooms[n]
            if (room.asleepRun) {
                room.setTemp = room.asleepSetTemp
            }
            onOrOffCheck(n)
        }
    }
}

def getRoomDevices(n) {
	if (n >= state.numRooms) {
    	return null
    }
    
    n++
    
    def devices = [:]
    
    devices.thermostat	= settings."r${n}_thermostat"
    devices.heater		= settings."r${n}_heater"
    devices.conditioner	= settings."r${n}_conditioner"
    devices.tempMonitor	= settings."r${n}_temp"
    
    log.debug("devices")
    log.debug(devices)
    
    return devices
}

private def TRACE(message) {
    log.debug message
    //log.debug "state: ${state}"
}
