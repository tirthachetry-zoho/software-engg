# Cloud (AWS) — Interview Q&A

## 1. What are the core AWS compute/storage services?
**Answer:** **EC2** (virtual servers), **ECS** (container orchestration on EC2), **EKS** (managed Kubernetes), **Lambda** (serverless functions), **S3** (object storage), **RDS** (managed relational DB), **DynamoDB** (managed NoSQL), **CloudWatch** (monitoring/logs).

## 2. What is the difference between EC2, ECS, EKS, and Lambda?
**Answer:** **EC2** = raw VMs (you manage OS). **ECS** = AWS's Docker orchestrator (simpler than K8s). **EKS** = managed Kubernetes. **Lambda** = serverless, no servers, pay-per-invoke, scales to zero. Choose by control vs ops overhead vs event-driven needs.

## 3. What is S3 and its key features?
**Answer:** **S3** is object storage (buckets + keys). Features: versioning, lifecycle policies (transition/expire), encryption (SSE/KE), static website hosting, cross-region replication, and event notifications. Great for files, backups, and static sites — not a filesystem/DB.

## 4. What is the difference between RDS and DynamoDB?
**Answer:** **RDS** = managed relational (Postgres/MySQL) with SQL, ACID, joins — for structured, transactional data. **DynamoDB** = managed NoSQL KV/document, single-digit ms, auto-scales, eventual/strong consistency — for high-throughput, schema-flexible workloads.

## 5. What is Lambda and when do you use it?
**Answer:** **Lambda** runs code on events (S3 upload, API Gateway, queue) with no server management, auto-scaling, and per-ms billing. Ideal for event-driven, spiky, or low-traffic workloads. Limits: max 15-min timeout, cold starts, and no persistent local state.

## 6. What is the difference between vertical and horizontal scaling on AWS?
**Answer:** **Vertical** = bigger instance (more CPU/RAM) — simple, but caps out and needs restart. **Horizontal** = more instances behind a load balancer / auto-scaling group — scales further, resilient. Cloud favors horizontal (stateless + ASG + ALB).

## 7. What is an Auto Scaling Group (ASG)?
**Answer:** An **ASG** maintains a desired count of EC2 instances, scaling out/in based on metrics (CPU, request count) or schedules. Paired with an **ALB** and health checks, it provides resilience and cost-efficient capacity.

## 8. What is the difference between a Load Balancer and API Gateway on AWS?
**Answer:** **ALB/NLB** (ELB) distributes traffic to EC2/tasks at L7/L4. **API Gateway** is for managed REST/WebSocket APIs — auth, throttling, request transformation, and direct Lambda integration. Gateway = API management; LB = raw traffic distribution.

## 9. What is the Well-Architected framework's pillars?
**Answer:** **Operational Excellence, Security, Reliability, Performance Efficiency, Cost Optimization, Sustainability.** Use it to review architectures. E.g., Reliability = multi-AZ, backups; Cost = right-sizing, autoscaling.

## 10. What is the difference between IAM roles, users, and policies?
**Answer:** **User** = an identity (person/service) with long-term creds. **Role** = assumable temporary creds (EC2 assumes a role, cross-account). **Policy** = JSON permissions attached to users/roles/groups. Prefer roles over embedding keys.

## 11. How do you secure data in AWS?
**Answer:** Encrypt at rest (S3 SSE-KMS, RDS encryption) and in transit (TLS). Use **KMS** for key management, **IAM** least-privilege, **Security Groups** (stateful firewall), **VPC** isolation, and **Secrets Manager** for credentials. Never hardcode keys.

## 12. What is the difference between a VPC, subnet, and security group?
**Answer:** **VPC** = isolated virtual network. **Subnet** = a range within a VPC (public via IGW, private). **Security Group** = stateful firewall on instances (allow rules only). NACLs are subnet-level stateless. VPC = network boundary; SG = instance access control.