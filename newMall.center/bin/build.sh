#!/usr/bin/env bash

CURRENT_DIR=$(dirname $0)

[ -d ${CURRENT_DIR}/newMall.tar.gz ] && rm ${CURRENT_DIR}/newMall.tar.gz
cp ${CURRENT_DIR}/../newMall.center.jar newMall.center.jar
tar -zcvf ${CURRENT_DIR}/newMall.tar.gz ${CURRENT_DIR}/run.conf ${CURRENT_DIR}/run.sh ${CURRENT_DIR}/newMall.center.jar
rm -rf ${CURRENT_DIR}/newMall.center.jar
