# SmartCart-Application
SmartCart - it's a user friendly application allows him to purchase the most compatible grocery shopping cart. 

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

