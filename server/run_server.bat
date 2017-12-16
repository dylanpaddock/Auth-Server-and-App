@echo off
echo Starting server now...
START /min ngrok http 3000
node server.js
