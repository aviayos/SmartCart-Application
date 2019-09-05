# SmartCart-Application
a user friendly app which allows you to purchase the cheapest most compatible grocery shopping car

## Installation

Install Android Studio IDE.

install the package manager [npm] (https://www.npmjs.com/) to install
all require libraries.

Download/Clone the source code from this repository on Github,
to your project IDE:
https://github.com/OrElla/SmartCart-Application


First, go to:
..\SmartCart-Application \ Server-side> 
and run the command:
npm install


Later go to:
..\SmartCart-Application\DataBase> 
and run the command:
npm install


## Running 

Open two cmd's and run the folowing:

In the first:
# Run DataBase:
..\SmartCart-Application\DataBase> 
and run the command:
pg_ctl -D /postgres start

In the second:
#Staring the server
..\SmartCart-Application \ Server-side> 
and run the command:
sudo npm run dev -s


Then, open the android studio with the folder Client-side.

