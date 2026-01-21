###	1)Docker Engine
		Docker Engine is the core runtime that builds, runs, and manages containers by interacting with the host OS kernel and system resources.
		
		'''
				Docker CLI  →  Docker REST API  →  Docker Daemon (dockerd)
														|
														├── containerd
														├── runc
														├── Storage Drivers
														├── Volume Drivers
															
		'''
		
		Workflow: Docker Engine receives container requests via the Docker CLI using the REST API, pulls required images from 	a  registry, manages container lifecycle through containerd, executes containers using runc, and enforces isolation and resource limits using Linux namespaces and cgroups.
		
		Component			Responsibility
		=========			===================================================
		Docker CLI			Tool where you type Docker commands like docker run
		Docker Daemon		Main service that listens to commands and manages Docker
		containerd			Starts, stops, and manages containers
		runc				Actually creates containers using Linux features
		Storage Driver		Manages container and image files
		Volume Driver		Stores data safely outside containers
		
		
### 2)Docker storage 
		Docker storage defines how container data is stored, shared, and persisted beyond the container lifecycle.
		Since containers are ephemeral, Docker provides multiple storage mechanisms to manage application and stateful data.
		
		Containers use a layered filesystem where image layers are immutable and shared, and each container gets a thin writable layer using copy-on-write. This writable layer stores runtime changes like logs and temp files and is lost when the container is removed, which is why persistent data is handled using volumes in production.
			Docker Uses Layered Filesystems
			Image layers		Read-only
			Container layer		Writable
				
				
				Storage drivers manage image layers and writable layers.

### 3)Bind mounts vs volumes
		Containers are ephemeral, so Docker uses volumes for persistent storage
		
###	4)Named volumes
		Inspecting volumes