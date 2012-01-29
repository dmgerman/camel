begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_comment
comment|/**  * Represents the status of a {@link Service} instance  *  * @version   */
end_comment

begin_enum
DECL|enum|ServiceStatus
specifier|public
enum|enum
name|ServiceStatus
implements|implements
name|Serializable
block|{
DECL|enumConstant|Starting
DECL|enumConstant|Started
DECL|enumConstant|Stopping
DECL|enumConstant|Stopped
DECL|enumConstant|Suspending
DECL|enumConstant|Suspended
name|Starting
block|,
name|Started
block|,
name|Stopping
block|,
name|Stopped
block|,
name|Suspending
block|,
name|Suspended
block|;
DECL|method|isStartable ()
specifier|public
name|boolean
name|isStartable
parameter_list|()
block|{
return|return
name|this
operator|==
name|Stopped
operator|||
name|this
operator|==
name|Suspended
return|;
block|}
DECL|method|isStoppable ()
specifier|public
name|boolean
name|isStoppable
parameter_list|()
block|{
return|return
name|this
operator|==
name|Started
operator|||
name|this
operator|==
name|Suspended
return|;
block|}
DECL|method|isSuspendable ()
specifier|public
name|boolean
name|isSuspendable
parameter_list|()
block|{
return|return
name|this
operator|==
name|Started
return|;
block|}
DECL|method|isStarting ()
specifier|public
name|boolean
name|isStarting
parameter_list|()
block|{
return|return
name|this
operator|==
name|Starting
return|;
block|}
DECL|method|isStarted ()
specifier|public
name|boolean
name|isStarted
parameter_list|()
block|{
return|return
name|this
operator|==
name|Started
return|;
block|}
DECL|method|isStopping ()
specifier|public
name|boolean
name|isStopping
parameter_list|()
block|{
return|return
name|this
operator|==
name|Stopping
return|;
block|}
DECL|method|isStopped ()
specifier|public
name|boolean
name|isStopped
parameter_list|()
block|{
return|return
name|this
operator|==
name|Stopped
return|;
block|}
DECL|method|isSuspending ()
specifier|public
name|boolean
name|isSuspending
parameter_list|()
block|{
return|return
name|this
operator|==
name|Suspending
return|;
block|}
DECL|method|isSuspended ()
specifier|public
name|boolean
name|isSuspended
parameter_list|()
block|{
return|return
name|this
operator|==
name|Suspended
return|;
block|}
block|}
end_enum

end_unit

