# 1) Flow Logs : 
VPC Flow Logs capture IP traffic metadata for inbound and outbound network traffic at the VPC, subnet, or ENI level.

## 2) What do Flow Logs record?
Flow logs does not capture packet content, only metadata
		- Source IP & Destination IP
		- Source port & Destination port
		- Protocol (TCP/UDP/ICMP)
		- Bytes & packets transferred	
		- ACCEPT or REJECT (by SG/NACL)
		- Start & end time


# 3) Where can you enable flow logs ?	
   - VPC level → all traffic in the VPC
   - Subnet level → traffic for that subnet
   - ENI level (Elastic Network Interface) 
		EC2, ALB, RDS , NAT Gateways, Lambda - vpc attached



# 4) Where are Flow Logs stored?
   - CloudWatch Logs → real-time monitoring & alerts
   - S3 → long-term storage, Athena queries
   - Kinesis Data Firehose → analytics pipelines


