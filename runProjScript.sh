#!/bin/bash

# Run db
pg_ctl -D /usr/local/var/postgres start

# Run server
cd Server-side/project 
sudo npm run dev -s 
