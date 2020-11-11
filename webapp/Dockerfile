FROM node:12-alpine as builder

WORKDIR '/usr/app'

ARG REACT_APP_ENV
ENV REACT_APP_ENV $REACT_APP_ENV

ENV AWS_ACCESS_KEY_ID AKIA3JBYJ73S4MS7BHPP
ENV AWS_SECRET_ACCESS_KEY 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
ENV GOOGLE_APPLICATION_CREDENTIALS '/usr/app/server/secrets/tailorurcv-goolge-talent-api-2a7446cacb54.json'

ENV RESUMESDK_UID=2009090
ENV RESUMESDK_PWD=7CX50B

RUN echo "Running on $REACT_APP_ENV"

# install dependencies
COPY package.json ./
COPY yarn.lock ./
RUN yarn install --silent

# add app and run
COPY . ./
RUN yarn build

CMD ["yarn", "serve"]