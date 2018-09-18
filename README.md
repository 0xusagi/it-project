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

To run the server:
- Run 'yarn nodemon-babel'
It should be available in your browser on localhost:3000.

To run tests:
- Run 'yarn test'
