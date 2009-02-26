begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * Reresents the status of a {@link Service} instance  *  * @version $Revision: 1.1 $  */
end_comment

begin_enum
DECL|enum|ServiceStatus
specifier|public
enum|enum
name|ServiceStatus
block|{
DECL|enumConstant|Created
DECL|enumConstant|Starting
DECL|enumConstant|Started
DECL|enumConstant|Stopping
DECL|enumConstant|Stopped
name|Created
block|,
name|Starting
block|,
name|Started
block|,
name|Stopping
block|,
name|Stopped
block|;
DECL|method|isStartable ()
specifier|public
name|boolean
name|isStartable
parameter_list|()
block|{
return|return
name|this
operator|!=
name|Starting
operator|&&
name|this
operator|!=
name|Started
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
operator|!=
name|Stopping
operator|&&
name|this
operator|!=
name|Stopped
return|;
block|}
block|}
end_enum

end_unit

