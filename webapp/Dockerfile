FROM node:12-alpine as builder

WORKDIR '/usr/app'

ARG REACT_APP_ENV
ENV REACT_APP_ENV $REACT_APP_ENV

RUN echo "Running on $REACT_APP_ENV"

# install dependencies
COPY package.json ./
COPY yarn.lock ./
RUN yarn install --silent

# add app and run
COPY . ./
RUN yarn build

CMD ["yarn", "serve"]