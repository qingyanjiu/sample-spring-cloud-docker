#!/usr/bin/env /bin/sh
estomorrow=`date -d "@$(($(date +%s) + 86400))" "+%Y-%m-%d"`
indexname=zipkin:span-$estomorrow
curl -X PUT "http://elasticsearch/$indexname" -H 'Content-Type: application/json;charset=utf-8' -d "@/jobfile/es-mapping.json"

eslastmonth=`date -d "@$(($(date +%s) - 86400*30*6))" "+%Y-%m-%d"`
indexnamelastmonth=zipkin:span-$eslastmonth
curl -X DELETE "http://elasticsearch/$indexnamelastmonth"
