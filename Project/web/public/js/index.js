function GUID() {
	return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
		s4() + '-' + s4() + s4() + s4();
}

function s4() {
	return Math.floor((1 + Math.random()) * 0x10000)
		.toString(16)
		.substring(1);
}

var queryString = (function(a) {
	if (a == "") return {};
	var b = {};
	for (var i = 0; i < a.length; ++i)
	{
		var p=a[i].split('=', 2);
		if (p.length == 1)
			b[p[0]] = "";
		else
			b[p[0]] = decodeURIComponent(p[1].replace(/\+/g, " "));
	}
	return b;
})(window.location.search.substr(1).split('&'));

var UID, TID, dbRefStr, dbReference;

// Initialize Firebase
var config = {
	apiKey: "AIzaSyA_SCSnNrGDpDOggc29OjDEhCc22ih-b8I",
	authDomain: "a-la-orden-e8295.firebaseapp.com",
	databaseURL: "https://a-la-orden-e8295.firebaseio.com",
	projectId: "a-la-orden-e8295",
	storageBucket: "a-la-orden-e8295.appspot.com",
	messagingSenderId: "400035215795"
};

(function(){
	UID = queryString['uid'] || 'ydB1rBeAy6PTaYn8AAsUqWmPzOJ2' || 'Xlv9H6WBHcQXJprMTwhDeAxj4Aq2';
	TID = queryString['mesa'] || '1';
	dbRefStr = 'user/' + UID;
	dbReference = null;

	document.querySelector('#btnFacture').addEventListener('click', event => {
		dbReference.child('table/'+TID+'/service').set('facture');
	});
	document.querySelector('#btnService').addEventListener('click', event => {
		dbReference.child('table/'+TID+'/service').set('order');
	});
	document.querySelector('#btnCheck').addEventListener('click', checkDB);
	document.querySelector('#btnReset').addEventListener('click', resetDB);
})();

function checkDB()
{
	dbReference.child('table/'+TID+'/free').set('false');
	dbReference.child('table/'+TID+'/service').set('check');
}

function serveDB()
{
	dbReference.child('table/'+TID+'/service').set('serve');
}

function factureDB()
{
	dbReference.child('table/'+TID+'/free').set(true);
	dbReference.child('table/'+TID+'/service').set('');
}

function resetDB()
{
	dbReference.set({
		email: 'ad@min.com',
		tableCount: 2,
		table: {
			1: {
				capacity: 4,
				free: true,
				id: 1,
				name: 'Mesa 1',
				service: ''
			},
			2: {
				capacity: 4,
				free: true,
				id: 2,
				name: 'Mesa 2',
				service: ''
			}
		}
	});
}