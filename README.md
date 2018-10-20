# Spain IT Project

To edit:
1. Clone repository:
    git clone https://github.com/COMP30022-18/Spain.git

2. Open Android Studio
    Download link: https://developer.android.com/studio/
    Install instructions: https://developer.android.com/studio/install

2. Open project
    'Open an existing Android Studio project'
    Navigate to the cloned repository
    Open ./Spain/SpainItproject

3. Click the 'Build' button on the toolbar

Dependencies:
- Firebase API
- Sinch API
- Google Maps API

Tests location:
- Integrated tests: 
    - ./Spain/SpainITproject/app/src/androidTest
- Unit tests:
    - ./Spain/SpainITproject/app/src/test

# Spain Server

Dependencies:
- Yarn
- NodeJS
- MongoDB

To install the server:
- cd into the 'Spain-Server' directory.
- run 'yarn install'
- Go into the mongo shell and type
'use spain-server'
- Then run db.placeholder.insert({placeholder: 'hello-world'})
- Check if the database was created by running 'show dbs' in mongo
- If using Windows, run npm install -g win-node-env

To run the server:
- Run 'yarn nodemon-babel'
It should be available in your browser on localhost:3000.

To run tests:
- Run 'yarn test'
