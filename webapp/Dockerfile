FROM node:12-alpine as builder

WORKDIR '/usr/app'

# install dependencies
COPY package.json ./
COPY yarn.lock ./
RUN yarn install --silent

# add app and run
COPY . ./
RUN yarn build

CMD ["yarn", "serve"]