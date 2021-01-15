Aufgabe 1
sudo ./mininet_1.py
ssh sv1
ssh c1
iperf3 -s
iperf3 -c 10.11.0.3 -Z -t 60
cpunetlog -l --nics sv1-eth1
summary
cnl-plot.py -nsc 0.001

Aufgabe 2
iperf3 -c 10.11.0.3 -Z -t 60
iperf3 -c 10.12.0.3 -Z -t 60
iperf3 -c 10.11.0.3 -Z -t 60 -u -b 1M
cpunetlog -l -- sv1-eth1 sv1-eth2

Aufgabe 3
sudo ./mininet_2.py

Aufgabe 4
sudo ./mininet_3.py
cpunetlog -l --nics c1-et
cpunetlog -l --nics c1-eth1

