#!/bin/sh
# wait-for-mysql.sh
# Usage: ./wait-for-mysql.sh <host> -- <command>

host="$1"
shift
cmd="$@"

echo "Waiting for $host to be ready on port 3306..."

# Wait until MySQL is up
while ! nc -z "$host" 3306; do
  echo "Waiting..."
  sleep 2
done

echo "$host is up! Starting app..."
exec $cmd
