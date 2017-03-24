begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|com.viwilo.camel.constants
package|package
name|com
operator|.
name|viwilo
operator|.
name|camel
operator|.
name|constants
package|;
end_package

begin_interface
DECL|interface|DigitalOceanHeaders
specifier|public
interface|interface
name|DigitalOceanHeaders
block|{
DECL|field|OPERATION
name|String
name|OPERATION
init|=
literal|"CamelDigitalOceanOperation"
decl_stmt|;
DECL|field|ID
name|String
name|ID
init|=
literal|"CamelDigitalOceanId"
decl_stmt|;
DECL|field|TYPE
name|String
name|TYPE
init|=
literal|"CamelDigitalOceanType"
decl_stmt|;
DECL|field|NAME
name|String
name|NAME
init|=
literal|"CamelDigitalOceanName"
decl_stmt|;
DECL|field|NEW_NAME
name|String
name|NEW_NAME
init|=
literal|"CamelDigitalOceanNewName"
decl_stmt|;
DECL|field|NAMES
name|String
name|NAMES
init|=
literal|"CamelDigitalOceanNames"
decl_stmt|;
DECL|field|REGION
name|String
name|REGION
init|=
literal|"CamelDigitalOceanRegion"
decl_stmt|;
DECL|field|DESCRIPTION
name|String
name|DESCRIPTION
init|=
literal|"CamelDigitalOceanDescription"
decl_stmt|;
DECL|field|DROPLET_SIZE
name|String
name|DROPLET_SIZE
init|=
literal|"CamelDigitalOceanDropletSize"
decl_stmt|;
DECL|field|DROPLET_IMAGE
name|String
name|DROPLET_IMAGE
init|=
literal|"CamelDigitalOceanDropletImage"
decl_stmt|;
DECL|field|DROPLET_KEYS
name|String
name|DROPLET_KEYS
init|=
literal|"CamelDigitalOceanDropletSSHKeys"
decl_stmt|;
DECL|field|DROPLET_ENABLE_BACKUPS
name|String
name|DROPLET_ENABLE_BACKUPS
init|=
literal|"CamelDigitalOceanDropletEnableBackups"
decl_stmt|;
DECL|field|DROPLET_ENABLE_IPV6
name|String
name|DROPLET_ENABLE_IPV6
init|=
literal|"CamelDigitalOceanDropletEnableIpv6"
decl_stmt|;
DECL|field|DROPLET_ENABLE_PRIVATE_NETWORKING
name|String
name|DROPLET_ENABLE_PRIVATE_NETWORKING
init|=
literal|"CamelDigitalOceanDropletEnablePrivateNetworking"
decl_stmt|;
DECL|field|DROPLET_USER_DATA
name|String
name|DROPLET_USER_DATA
init|=
literal|"CamelDigitalOceanDropletUserData"
decl_stmt|;
DECL|field|DROPLET_VOLUMES
name|String
name|DROPLET_VOLUMES
init|=
literal|"CamelDigitalOceanDropletVolumes"
decl_stmt|;
DECL|field|DROPLET_TAGS
name|String
name|DROPLET_TAGS
init|=
literal|"CamelDigitalOceanDropletTags"
decl_stmt|;
DECL|field|DROPLET_ID
name|String
name|DROPLET_ID
init|=
literal|"CamelDigitalOceanDropletId"
decl_stmt|;
DECL|field|IMAGE_ID
name|String
name|IMAGE_ID
init|=
literal|"CamelDigitalOceanImageId"
decl_stmt|;
DECL|field|KERNEL_ID
name|String
name|KERNEL_ID
init|=
literal|"CamelDigitalOceanKernelId"
decl_stmt|;
DECL|field|VOLUME_NAME
name|String
name|VOLUME_NAME
init|=
literal|"CamelDigitalOceanVolumeName"
decl_stmt|;
DECL|field|VOLUME_SIZE_GIGABYTES
name|String
name|VOLUME_SIZE_GIGABYTES
init|=
literal|"CamelDigitalOceanVolumeSizeGigabytes"
decl_stmt|;
DECL|field|FLOATING_IP_ADDRESS
name|String
name|FLOATING_IP_ADDRESS
init|=
literal|"CamelDigitalOceanFloatingIPAddress"
decl_stmt|;
DECL|field|KEY_FINGERPRINT
name|String
name|KEY_FINGERPRINT
init|=
literal|"CamelDigitalOceanKeyFingerprint"
decl_stmt|;
DECL|field|KEY_PUBLIC_KEY
name|String
name|KEY_PUBLIC_KEY
init|=
literal|"CamelDigitalOceanKeyPublicKey"
decl_stmt|;
block|}
end_interface

end_unit

