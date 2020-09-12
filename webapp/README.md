#Kuaidao-webapp

This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app), using the [Redux](https://redux.js.org/) and [Redux Toolkit](https://redux-toolkit.js.org/).

## First time
Make sure you have yarn and node v10.21.0+ install.

In `kuaidao/webapp`, you can install dependencies by run

### `yarn`

## Ongoing Development

Please use [this guideline](DEVELOPMENT.md) to as initial help. You can also run below commands to run/test/build the application

### `yarn start`

Runs the app in the development mode.<br />
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.<br />
You will also see any lint errors in the console.

### `yarn test`

Launches the test runner in the interactive watch mode.<br />
See the section about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.

### `yarn build`

Builds the app for production to the `build` folder.<br />
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.<br />
Your app is ready to be deployed!

## Other useful documentations
- [Create React App documentation](https://facebook.github.io/create-react-app/docs/getting-started).
- [React documentation](https://reactjs.org/).
- [React hooks intro](https://reactjs.org/docs/hooks-intro.html)
- [Redux and React hooks](https://react-redux.js.org/api/hooks)




## Google Analytics Tracking
We have some Google Analytics tracking functions under `utils/GATracking.js`

### To install

`yarn add react-ga`

### To use

`import { funcName } from 'path/to/utils/GATracking'`

 - Initialization:

        // In the root component, initialize by running:
        GAInitialize()

        // Note: We only need to called once (Called in App/index.jsx)

 - To report page view:

        GApageView()

 - Add custom tracking event.

        GAEvent(category, action)
        // Note: Category is one of four apps, example: "Resume Edit", 
        // Note: Action example: "Save work form"