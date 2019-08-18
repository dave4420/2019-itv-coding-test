#!/bin/bash
set -euo pipefail
IFS=$'\n\t'
# Unofficial bash strict mode <http://redsymbol.net/articles/unofficial-bash-strict-mode/>

dir="$(dirname "$0")"

java -jar "$dir"/../target/scala-2.13/dave-hinton-2019-itv-coding-test-assembly-0.1.jar "$@"
