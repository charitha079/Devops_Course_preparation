############################################
# PROVIDER CONFIGURATION
############################################
# This tells Terraform which cloud provider to use
# and in which AWS region resources should be created
provider "aws" {
  region = "us-east-1"
}

############################################
# VPC (Virtual Private Cloud)
############################################
# Creates a private network in AWS
resource "aws_vpc" "main" {
  cidr_block = "10.0.0.0/16"

  tags = {
    Name = "Mr_cloud_vpc"
  }
}

############################################
# PUBLIC SUBNET
############################################
# Subnet with internet access (for EC2)
resource "aws_subnet" "public" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.2.0/24"
  availability_zone = "us-east-1a"

  tags = {
    Name = "Mr_cloudd_public_subnet"
  }
}

############################################
# PRIVATE SUBNET
############################################
# Subnet without direct internet access
resource "aws_subnet" "private" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.3.0/24"
  availability_zone = "us-east-1a"

  tags = {
    Name = "Mr_cloud_private_subnet"
  }
}

############################################
# INTERNET GATEWAY
############################################
# Allows internet access to the VPC
resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.main.id

  tags = {
    Name = "Mr_cloud_internet_gateway"
  }
}

############################################
# ROUTE TABLE FOR PUBLIC SUBNET
############################################
# Routes internet traffic via Internet Gateway
resource "aws_route_table" "public_rt" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }

  tags = {
    Name = "Mr_cloud_public_route_table"
  }
}

############################################
# ROUTE TABLE ASSOCIATION
############################################
# Connects public subnet to public route table
resource "aws_route_table_association" "public_assoc" {
  subnet_id      = aws_subnet.public.id
  route_table_id = aws_route_table.public_rt.id
}

############################################
# SECURITY GROUP
############################################
# Firewall rules for EC2 instance
resource "aws_security_group" "web_sg" {
  name        = "web-sg"
  description = "Allow SSH and HTTP"
  vpc_id      = aws_vpc.main.id

  # Allow SSH access
  ingress {
    description = "SSH"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # Allow HTTP access
  ingress {
    description = "HTTP"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # Allow all outbound traffic
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "web-sg"
  }
}

############################################
# EC2 INSTANCE
############################################
# Launches a web server in public subnet
resource "aws_instance" "web" {
  ami                    = "ami-0532be01f26a3de55"
  instance_type          = "t2.micro"
  subnet_id              = aws_subnet.public.id
  vpc_security_group_ids = [aws_security_group.web_sg.id]
  key_name               = "Minikube_setup"

  # IAM role attached so EC2 can access S3 securely
  iam_instance_profile = aws_iam_instance_profile.ec2_profile.name
}

############################################
# S3 BUCKET
############################################
# Storage bucket to be accessed by EC2
resource "aws_s3_bucket" "demo_bucket" {
  bucket = "mr-cloud-bucket-989"

  tags = {
    Name = "Mr_cloud_storage"
  }
}

############################################
# IAM ROLE FOR EC2
############################################
# Allows EC2 to assume this role
resource "aws_iam_role" "ec2_role" {
  name = "ec2-s3-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect = "Allow"
      Principal = {
        Service = "ec2.amazonaws.com"
      }
      Action = "sts:AssumeRole"
    }]
  })
}

############################################
# IAM POLICY (S3 READ ACCESS)
############################################
# Grants EC2 permission to read from S3
resource "aws_iam_policy" "s3_read_policy" {
  name = "s3-read-policy"

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect   = "Allow"
      Action   = ["s3:GetObject", "s3:ListBucket"]
      Resource = [
        aws_s3_bucket.demo_bucket.arn,
        "${aws_s3_bucket.demo_bucket.arn}/*"
      ]
    }]
  })
}

############################################
# ATTACH POLICY TO ROLE
############################################
resource "aws_iam_role_policy_attachment" "attach_s3_policy" {
  role       = aws_iam_role.ec2_role.name
  policy_arn = aws_iam_policy.s3_read_policy.arn
}

############################################
# IAM INSTANCE PROFILE
############################################
# Required to attach IAM role to EC2
resource "aws_iam_instance_profile" "ec2_profile" {
  name = "ec2-instance-profile"
  role = aws_iam_role.ec2_role.name
} 
