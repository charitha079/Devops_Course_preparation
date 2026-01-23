### Module 4: Docker Networking


	Topics:
		- Docker default networks: bridge, host, none
		- Custom bridge networks
		- Container-to-container communication
		- Exposing ports vs publishing ports

	Hands-on:
		- Run multiple containers and enable communication
		- Link containers using Docker network
	
###	1)Networking :

Networking refers to the interconnection of computing devices (such as computers, servers, or containers) that enables them to exchange data, communicate, and share resources efficiently.

Docker networking is the mechanism that allows containers to communicate with each other, the host, and external networks. Networking is critical because containers are isolated by default, but real-world applications often need communication across multiple services.

###	why containers need to communicate ?

Containers need to communicate because modern applications are rarely a single monolith—they’re usually composed of multiple services that must work together. In Docker and microservices architecture, each container typically runs one process or service, so inter-container communication is essential for functionality.