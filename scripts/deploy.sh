#!/bin/bash

IS_GREEN_EXIST=$(docker ps | grep green)
IS_REDIS_EXIST=$(docker ps | grep redis)

if [ -z $IS_REDIS_EXIST ];then
	echo "### REDIS ###"
	echo ">>> pull redis image"
	docker-compose pull redis
	echo ">>> up redis container"
	docker-compose up -d redis
fi

# green up
if [ -z $IS_GREEN_EXIST ];then
  echo "### BLUE -> GREEN ####"
  echo ">>> pull green image"
  docker-compose pull green
  echo ">>> up green container"
  docker-compose up -d green
  while [ 1 = 1 ]; do
  echo ">>> green health check ..."
  sleep 3
  REQUEST=$(curl http://127.0.0.1:8082)
    if [ -n "$REQUEST" ]; then
      echo ">>> health check success !"
      break;
    fi
  done;
  sleep 3
  echo ">>> reload nginx"
  sudo cp /etc/nginx/conf.d/green-url.inc /etc/nginx/conf.d/service-url.inc
  sudo nginx -s reload
  echo ">>> down blue container"
  docker-compose stop blue

# blue up
else
  echo "### GREEN -> BLUE ###"
  echo ">>> pull blue image"
  docker-compose pull blue
  echo ">>> up blue container"
  docker-compose up -d blue
  while [ 1 = 1 ]; do
    echo ">>> blue health check ..."
    sleep 3
    REQUEST=$(curl http://127.0.0.1:8081)
    if [ -n "$REQUEST" ]; then
      echo ">>> health check success !"
      break;
    fi
  done;
  sleep 3
  echo ">>> reload nginx"
  sudo cp /etc/nginx/conf.d/blue-url.inc /etc/nginx/conf.d/service-url.inc
  sudo nginx -s reload
  echo ">>> down green container"
  docker-compose stop green
fi