FROM node:alpine as builder
WORKDIR '/build'
#SY please fill-out the copy as you want to architecture.
COPY A ./A

WORKDIR '/build/A'

RUN yarn build
#RUN TBD

#SY please fill-out the copy as you want to architecture.
RUN ls /build/A/dist

# EXPOSE 80 as you want