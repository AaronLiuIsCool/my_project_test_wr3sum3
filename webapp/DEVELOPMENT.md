# Development guidelines

It is very important that we keep our code repo with good quality for our daily development. This means both good code hygiene and feature quality assurance.

Hence we are using following techniques:

## Code splitting

Not all of your code needs to loaded right away at initialization. Using code splitting for each feature will help us load the more important resources(the one user is currently using) first.

Please see [code](src/features/SmartResume/index.js) as an exmaple.

## CSS modules

Dead CSS code is one of the pain points web development is facing with the project ages. CSS modules is a way to help that. Please see [this post](https://css-tricks.com/css-modules-part-1-need/) for more details on its pros and cons.

Please see [code](src/features/SmartResume/styles/Experience.module.css) as an example.

## Bootstrap Wrapper

Being able to reuse code is important for software engineering. We are adding a wrapper on top of react-bootstrap, so we can have a unified style for our components.

Please see [code](src/components/Button.jsx) as an example.

## Internationalization

Always use internationalization files for user facing strings. This will help us easily update and translate our strings across the application.

Please see [code](src/features/SmartResume/i18n/zh.json) as an example.

## Unit tests

We want to have 100% unit tests coverage for important business logics, for UI related we are using Jest Snapshots.

Please note with code spitting, the top level might get a the fallback loading component. In this case, we want to test the snapshots at feature level without the code spitting lazy load.