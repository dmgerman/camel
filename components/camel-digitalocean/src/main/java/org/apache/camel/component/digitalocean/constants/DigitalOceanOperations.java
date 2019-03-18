begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.digitalocean.constants
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|digitalocean
operator|.
name|constants
package|;
end_package

begin_enum
DECL|enum|DigitalOceanOperations
specifier|public
enum|enum
name|DigitalOceanOperations
block|{
DECL|enumConstant|create
name|create
block|,
DECL|enumConstant|update
name|update
block|,
DECL|enumConstant|delete
name|delete
block|,
DECL|enumConstant|list
name|list
block|,
DECL|enumConstant|ownList
name|ownList
block|,
DECL|enumConstant|get
name|get
block|,
DECL|enumConstant|listBackups
name|listBackups
block|,
DECL|enumConstant|listActions
name|listActions
block|,
DECL|enumConstant|listNeighbors
name|listNeighbors
block|,
DECL|enumConstant|listSnapshots
name|listSnapshots
block|,
DECL|enumConstant|listKernels
name|listKernels
block|,
DECL|enumConstant|listAllNeighbors
name|listAllNeighbors
block|,
DECL|enumConstant|enableBackups
name|enableBackups
block|,
DECL|enumConstant|disableBackups
name|disableBackups
block|,
DECL|enumConstant|reboot
name|reboot
block|,
DECL|enumConstant|powerCycle
name|powerCycle
block|,
DECL|enumConstant|shutdown
name|shutdown
block|,
DECL|enumConstant|powerOn
name|powerOn
block|,
DECL|enumConstant|powerOff
name|powerOff
block|,
DECL|enumConstant|restore
name|restore
block|,
DECL|enumConstant|resetPassword
name|resetPassword
block|,
DECL|enumConstant|resize
name|resize
block|,
DECL|enumConstant|rebuild
name|rebuild
block|,
DECL|enumConstant|rename
name|rename
block|,
DECL|enumConstant|changeKernel
name|changeKernel
block|,
DECL|enumConstant|enableIpv6
name|enableIpv6
block|,
DECL|enumConstant|enablePrivateNetworking
name|enablePrivateNetworking
block|,
DECL|enumConstant|takeSnapshot
name|takeSnapshot
block|,
DECL|enumConstant|transfer
name|transfer
block|,
DECL|enumConstant|convert
name|convert
block|,
DECL|enumConstant|attach
name|attach
block|,
DECL|enumConstant|detach
name|detach
block|,
DECL|enumConstant|assign
name|assign
block|,
DECL|enumConstant|unassign
name|unassign
block|,
DECL|enumConstant|tag
name|tag
block|,
DECL|enumConstant|untag
name|untag
block|}
end_enum

end_unit

