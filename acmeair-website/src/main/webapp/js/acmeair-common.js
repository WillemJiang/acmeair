/*******************************************************************************
* Copyright (c) 2013-2015 IBM Corp.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*******************************************************************************/
function showLoginDialog() {
	dijit.byId('loginDialog').show();
}

function hideLoginDialog() {
	dijit.byId('loginDialog').hide();
}

function showLoginWaitDialog() {
	dijit.byId('loginWaitDialog').show();
}

function hideLoginWaitDialog() {
	dijit.byId('loginWaitDialog').hide();
}

function showWaitDialog() {
	dijit.byId('waitDialog').show();
}

function hideWaitDialog() {
	dijit.byId('waitDialog').hide();
}


function updateLoggedInUserWelcome() {
	var loggedinuser = dojo.cookie("loggedinuser");
	if (loggedinuser == null) {
		dojo.byId("loggedinwelcome").innerHTML = '';
	}
	else {
		dojo.byId("loggedinwelcome").innerHTML = 'Welcome Back ' + loggedinuser;
	}
}

function login() {
	hideLoginDialog();
	showLoginWaitDialog();
	
	var userString = document.getElementById('userId').value;
	dojo.xhrPost({
		content : {
			login: userString,
			password: document.getElementById('password').value
		},
		url: 'customers/rest/api/login',
		load: function(response, ioArgs) {
			hideLoginWaitDialog();
			if (response != 'logged in') {
				// TODO: why isn't error function being called in this case
				alert('error logging in, response: ' + response);
				return;
			}
			dojo.cookie("loggedinuser", userString, {expires: 5});
			updateLoggedInUserWelcome();
		},
		error: function(response, ioArgs) {
			hideLoginWaitDialog();
			alert('error logging in, response: ' + response);
		}
	});
}

function logout() {
	updateLoggedInUserWelcome();
	var loggedinuser = dojo.cookie("loggedinuser");
	if (loggedinuser == null) {
		return;
	}
	
	dojo.xhrGet({
		content : {
			login: loggedinuser
		},
		url: 'customers/rest/api/login/logout',
		load: function(response, ioArgs) {
			if (response != 'logged out') {
				// TODO: why isn't error function being called in this case
				alert('error logging out, response: ' + response);
				return;
			}
			dojo.cookie("loggedinuser", null, {expires: -1});
			updateLoggedInUserWelcome();
		},
		error: function(response, ioArgs) {
			alert('error logging out, response: ' + response);
		}
	});
}

function dateFormatter(data) {
	var d = new Date(data);
	return dojo.date.locale.format(d, {selector: 'date', datePattern: 'MMMM d, yyyy - hh:mm a'});
}

function currencyFormatter(data) {
	return dojo.currency.format(data, {currency: "USD"});
}

// genned from mongo by:  db.airportcodes.find({}, {airportCode:1, airportName:1}).forEach(function(f){print(tojson(f, '', true));});
// switch airportCode to id
var airportCodes = [
	{ airportName : "北京 Beijing", id : "PEK" },
	{ airportName : "上海 Shanghai", id : "SHA" },
	{ airportName : "法兰克福 Frankfurt", id : "FRA" },
	{ airportName : "香港 Hong Kong", id : "HKG" },
	{ airportName : "伦敦 London", id : "LHR" },
	{ airportName : "蒙特利尔 Montreal", id : "YUL" },
	{ airportName : "莫斯科 Moscow", id : "SVO" },
	{ airportName : "纽约 New York", id : "JFK" },
	{ airportName : "巴黎 Paris", id : "CDG" },
	{ airportName : "罗马 Rome", id : "FCO" },
	{ airportName : "新加坡 Singapore", id : "SIN" },
	{ airportName : "悉尼 Sydney", id : "SYD" },
	{ airportName : "德黑兰 Tehran", id : "IKA" },
	{ airportName : "东京 Tokyo", id : "NRT" }
];

function airportCodeToAirportName(airportCode) {
	var airports = dojo.filter(airportCodes, function (item) { return item.id == airportCode; } );
	if (airports.length > 0) {
		return airports[0].airportName;
	}
	return airportCode;
}
