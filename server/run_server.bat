@echo off
echo Starting server now...
START /min lt --port 3000 --subdomain dylan
nodemon server.js