
//API routes
//POST http://localhost:3000/login
//POST http://localhost:3000/signup
//POST http://localhost:3000/addstring

/*
TODO
fix undefined fields
fix app crashes
encryption on passwords (salt+hash)
improve documentation
*/


const express = require('express');
const jwt = require('jsonwebtoken');
const config = require('./config');
const bodyParser = require('body-parser');

//configuration
var port = 3000
var app = express();
app.use(bodyParser.urlencoded({extended: false}));
app.set('secret', config.secret);

var userDatabase = {"dylan": {password: "hunter2", strings: ["Hello dylan", "yes", "a", "b", "c"], current: 4}, "debarati": {password: "hunter3", strings: ["Hello debarati", "no"], current: 1}};

//routing
app.get('/', (req, res) => {
    res.send('This is a REST API.')
});

app.post('/login', (req, res) => {
    var newUser = req.body.username;
    var newPass = req.body.password;
    //find user in database
    //compare username + password
    console.log("Logging in user " + newUser);

    res.setHeader('Content-Type', 'application/json');
    if (userDatabase[newUser] === undefined) {
        res.json({
            success: false,
            message: "Username or password does not match."
        });
    }else if (userDatabase[newUser].password !== newPass) { //decrypt here
        res.json({
            success: false,
            message: "Username or password does not match.2"
        });
    }else { //Log user in. Give authentication token
        var payload = {
            name: newUser,
            iss: 'Dylan Paddock',
            iat: Math.floor(Date.now()/1000)
        };
        var token = jwt.sign(payload, app.get('secret'), {expiresIn: '1h'});
        res.json({
            success: true,
            message: "Log in complete. Token valid for one hour.",
            token: token,
            strings: getStrings(userDatabase, newUser)
        });
    }
});

app.post('/signup', (req, res) => {
    res.setHeader('Content-Type', 'application/json');
    var newUser = req.body.username;
    var newPass = req.body.password;

    console.log("Signing up new user " + newUser);


    //Username is not unique. Send failure.
    if (userDatabase[newUser] !== undefined) {
        res.json({
            success: false,
            message: "This username is already taken.",
        });
    }else{
        //Store user information in database for later authentication
        userDatabase[newUser] = {password: newPass, strings: [], current: 0};
        addString(userDatabase, newUser, "Hello, " + newUser);
        //log user in and give authentication token
        var payload = {
            name: newUser,
            iss: 'Dylan Paddock',
            iat: Math.floor(Date.now()/1000)
        };
        var token = jwt.sign(payload, app.get('secret'), {expiresIn: '1h'});
        res.json({
            success: true,
            message: "Welcome! Sign in complete. Token valid for one hour.",
            token: token,
            strings: getStrings(userDatabase, newUser)
        })
    }
});

app.post('/addstring', (req, res) => {

    console.log("Adding a string: " + req.body.text);
});

app.listen(port);
console.log('Server is listening at http://localhost:', port + '/');


/*
data:text/html,<body onload="document.body.firstChild.submit()"><form method="post" action="http://dylan.localtunnel.me/login">
data:text/html,<body onload="document.body.firstChild.submit()"><form method="post" action="http://192.168.1.194:3000/login"><input value="dylan" name="username"><input value="hunter2" name="password">
*/

/*
The data structure for storing strings is an array of at most 10 strings. The
strings are ordered chronologically starting from @current, moving to the end,
then looping back to the beginning of the array.
getStrings produces a single formatted string containing all strings in the
database with line breaks between.
addString places a new string into the list, possibly overwriting an older
string.
*/
function getStrings(userDatabase, user){
    var strings = userDatabase[user].strings;
    var current = userDatabase[user].current;
    combinedString = '';
    if (strings.length < 10){
        for(i = 0; i != current; i++){
            combinedString = '\n' + strings[i] + combinedString;
        }
    }else {
        for (i = (current+1)%10; i != current; i = (i+1)%10) {
            combinedString = '\n' + strings[i] + combinedString;
        }
    }
    return strings[current] + combinedString;
}

//
function addString(userDatabase, user, newString){
    var strings = userDatabase[user].strings;
    var current = userDatabase[user].current;
    if (strings.length < 10) { //space remaining in array
        current++;
    }else {
        current = (current+1)%10; //overwrite existing
    }
    strings[current] = newString; //add new string in appropriate place
    userDatabase[user].strings = strings;
    userDatabase[user].current = current;
}