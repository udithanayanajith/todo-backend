#!/bin/sh
host="$1"
shift
cmd="$@"

echo "Waiting for $host..."
while ! nc -z "$host" 3306; do
  echo "Waiting..."
  sleep 2
done

echo "$host is up! Starting app..."
exec $cmd
