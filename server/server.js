
//API routes
//GET http://localhost:3000/
//POST http://localhost:3000/login {username, password}
//POST http://localhost:3000/signup {username, password}
//POST http://localhost:3000/addstring {authToken, string}

/*
TODO
encryption on passwords (salt+hash)
improve documentation
make procedures asynchronous
*/


//configuration and initialization
const express = require('express');
var app = express();
const bodyParser = require('body-parser');
const jwt = require('jsonwebtoken');
const config = require('./config');

var port = 3000
app.set('secret', config.secret);
app.use(bodyParser.urlencoded({extended: false}));
app.use(bodyParser.json());

var userDatabase = {};

//routing
app.get('/', (req, res) => {
    res.send('This is a REST API.')
});

app.post('/login', (req, res) => {
    var user = req.body.username;
    var pass = req.body.password;

    console.log("Logging in user " + user);

    //find user in database
    //compare username and password to stored versions
    res.setHeader('Content-Type', 'application/json');
    if (userDatabase[user] === undefined) {
        res.json({
            success: false,
            message: "Username or password does not match."
        });
    }else if (!userDatabase[user].password.equals(pass)) { //decrypt here
        res.json({
            success: false,
            message: "Username or password does not match."
        });
    }else { //Log user in. Give authentication token
        var payload = {
            name: user,
            iss: 'Dylan Paddock',
            iat: Math.floor(Date.now()/1000),
            sub: user
        };
        var token = jwt.sign(payload, app.get('secret'), {expiresIn: '1h'});
        res.json({
            success: true,
            message: "Log in complete. Token valid for one hour.",
            token: token,
            strings: getStrings(userDatabase, user)
        });
    }
});

app.post('/signup', (req, res) => {
    res.setHeader('Content-Type', 'application/json');
    var user = req.body.username;
    var pass = req.body.password;

    console.log("Signing up new user " + user);

    //Username is not unique. Send failure.
    if (userDatabase[user] !== undefined) {
        res.json({
            success: false,
            message: "This username is already taken.",
        });
    }else{
        //Store user information in database for later authentication
        userDatabase[user] = {password: pass, strings: ["Hello, " + user], current: 0};
        //addString(userDatabase, user, "Hello, " + user);
        //log user in and give authentication token
        var payload = {
            name: user,
            iss: 'Dylan Paddock',
            iat: Math.floor(Date.now()/1000),
            sub: user
        };
        var token = jwt.sign(payload, app.get('secret'), {expiresIn: '1h'});
        res.json({
            success: true,
            message: "Welcome! Sign up complete. Token valid for one hour.",
            token: token,
            strings: getStrings(userDatabase, user)
        })
    }
});

app.post('/addstring', (req, res) => {
    res.setHeader('Content-Type', 'application/json');
    var newString = req.body.text.toString();
    var token = req.body.token;

    console.log("Adding a string: " + req.body.text);

    //authenticate token
    jwt.verify(token, app.get('secret'), (err, decoded) => {
        if (err) {
            res.json({
                success: false,
                auth: false,
                message: "Authentication failed. Please log in again.",
            });
        }else {
            //todo: make asynchronous.
            var user = decoded.name;
            if (hasDuplicate(userDatabase, user, newString)){
            //New string is a duplicate of existing string
                res.json({
                    success: false,
                    auth: true,
                    message: "Duplicates are not allowed!"
                })
            }else {
                addString(userDatabase, user, newString);
                res.json({
                    success: true,
                    message: "You have added a string to the list.",
                    strings: getStrings(userDatabase, user)
                })
            }

        }
    });
});

//start server
app.listen(port);
console.log('Server is listening at http://localhost:'+ port + '/');

/*
The data structure for storing strings is an array of at most 10 strings. The
strings are ordered chronologically starting from @current, moving to the end,
then looping back to the beginning of the array.
getStrings produces a single formatted string containing all strings in the
database with line breaks between.
addString places a new string into the list, possibly overwriting an older
string.
hadDuplicate tells if a string already exists in the database for a given user.
*/
function getStrings(userDatabase, user) {
    var strings = userDatabase[user].strings;
    var current = userDatabase[user].current;
    combinedString = '';
    if (strings.length === 1) {
        return strings[0];
    }else if (strings.length < 10) {
        for(i = 0; i != current; i++) {
            combinedString = '\n' + strings[i] + combinedString;
        }
    }else {
        for (i = (current+1)%10; i != current; i = (i+1)%10) {
            combinedString = '\n' + strings[i] + combinedString;
        }
    }
    return strings[current] + combinedString;
}

function addString(userDatabase, user, newString) {
    var strings = userDatabase[user].strings;
    var current = userDatabase[user].current;
    if (strings.length < 10) { //if array is not full, add to end
        current++;
    }else {
        current = (current+1)%10; //overwrite existing string
    }
    strings[current] = newString; //add new string in appropriate place
    userDatabase[user].strings = strings;
    userDatabase[user].current = current;
}

function hasDuplicate(userDatabase, user, newString) {
    strings = userDatabase[user].strings;
    for (var i = 0; i < strings.length; i++) {
        if (newString === strings[i]) {
            return true;
        }
    }
    return false;
}