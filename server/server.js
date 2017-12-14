const http = require('http');

const hostname = '0.0.0.0';
const port = 3000;

var server = http.createServer((req, res) => { //set up server with REST API
    req.on('signup', () => {
        console.log('signing up')
        response.write("");
    }).on('login', () => {
        console.log('logging in')
    }).on('addstring', () => {
        console.log('adding string:')
    });

    res.statusCode = 200; //change this for issues, e.g. 404
    res.setHeader('Content-Type', 'text/plain');
    res.end('{name: Dylan, event: test, success: true, text: "Hello, world!"}');

    //don't forget to account for status code 400 (bad request) and 404 (not found), on('error, ()=>()) + console.error(err)
});

server.listen(port, hostname, () => { //server begins listening to requests
    console.log(`Server running at http://${hostname}:${port}/`);
});