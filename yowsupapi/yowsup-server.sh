#!/bin/sh
python /home/yash/work/git/whatsapp3c/yowsupapi/run.py
while true 
do
	for i in `ps -ef | grep defunc | grep run.py |  tr -s ' ' | cut  -d' ' -f3 ` ;
	 do 
	kill $i
	 done
	if[$? -eq 0];
	then
	python /home/yash/work/git/whatsapp3c/yowsupapi/run.py
	fi
	sleep 2s

done
