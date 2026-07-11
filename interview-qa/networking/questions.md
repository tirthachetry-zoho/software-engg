# Networking — Interview Q&A

## 1. Explain the TCP 3-way handshake.
**Answer:** Client sends **SYN** (seq=x); server replies **SYN-ACK** (seq=y, ack=x+1); client sends **ACK** (ack=y+1). This establishes a connection and synchronizes sequence numbers. Tear-down is a 4-way FIN/ACK exchange.

## 2. What is the difference between TCP and UDP?
**Answer:** **TCP** is connection-oriented, reliable (acknowledgments, retransmit, ordering), with flow/congestion control — used for HTTP, DB, streams. **UDP** is connectionless, unreliable, low-latency, no ordering — used for DNS, video, games. TCP = correctness; UDP = speed.

## 3. What is the difference between HTTP and HTTPS?
**Answer:** **HTTPS** = HTTP over **TLS** — encrypts the payload, provides authentication (certificate) and integrity. HTTP is plaintext (sniffable). HTTPS uses port 443, requires a CA-signed cert, and enables HSTS. Essential for any auth/data.

## 4. Explain the TLS handshake (simplified).
**Answer:** ClientHello (supported ciphers) → ServerHello + **certificate** (public key, signed by CA) → client verifies cert, generates a **pre-master secret** encrypted with server's public key → both derive a **symmetric session key**. Subsequent traffic is symmetrically encrypted (fast).

## 5. What is DNS and how does resolution work?
**Answer:** DNS maps domain names → IPs. Resolution: app → **recursive resolver** → root → TLD (.com) → authoritative nameserver → returns IP, cached at each level (TTL). Uses UDP/53 (TCP for large). Critical for every connection.

## 6. What is a load balancer and the algorithms?
**Answer:** Distributes traffic across servers for scalability/HA. Algorithms: **Round Robin**, **Least Connections**, **IP/Hash** (sticky), **Weighted**, **Least Response Time**. L4 (TCP) vs L7 (HTTP, URL/path-aware). Placed in front of services/API gateways.

## 7. What is the difference between HTTP/1.1, HTTP/2, and HTTP/3?
**Answer:** **1.1** — one request per TCP connection (pipelining limited). **2** — multiplexed streams over one connection, header compression, server push. **3 (QUIC)** — runs over **UDP**, 0-RTT, better loss recovery/mobility. Each reduces latency.

## 8. What is the difference between a proxy and a reverse proxy?
**Answer:** A **forward proxy** sits in front of clients (hides client identity, corporate filtering). A **reverse proxy** (nginx, ALB) sits in front of servers — TLS termination, load balancing, caching, routing. Reverse proxies are standard in backend infra.

## 9. What is flow control and congestion control in TCP?
**Answer:** **Flow control** (sliding window) prevents a fast sender overwhelming a slow receiver. **Congestion control** (slow start, congestion avoidance) prevents overwhelming the network. Both use window sizes; essential for stable throughput.

## 10. What is the difference between a router, switch, and gateway?
**Answer:** **Switch** operates at L2 (MAC) within a LAN. **Router** at L3 (IP) routes between networks. **Gateway** connects different protocol networks (often the router to the internet). Each handles a different network layer.

## 11. What are common HTTP status code classes?
**Answer:** **1xx** informational, **2xx** success (200, 201), **3xx** redirect (301, 304), **4xx** client error (400, 401, 403, 404, 429), **5xx** server error (500, 502, 503, 504). Know which to return in API design.

## 12. What is the difference between a socket, port, and connection?
**Answer:** A **port** is a 16-bit endpoint identifier (0–65535). A **socket** is an OS abstraction = IP+port (one per side). A **connection** is the 4-tuple (src IP:port, dst IP:port) identifying a TCP session. Ports multiplex apps on one host.