#!/bin/bash
cd `dirname $0`
./shutdown.sh $*
./start.sh $*
