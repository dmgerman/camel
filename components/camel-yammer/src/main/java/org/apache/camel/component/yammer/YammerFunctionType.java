begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.yammer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|yammer
package|;
end_package

begin_enum
DECL|enum|YammerFunctionType
specifier|public
enum|enum
name|YammerFunctionType
block|{
DECL|enumConstant|MESSAGES
DECL|enumConstant|MY_FEED
DECL|enumConstant|ALGO
DECL|enumConstant|FOLLOWING
DECL|enumConstant|SENT
DECL|enumConstant|PRIVATE
DECL|enumConstant|RECEIVED
name|MESSAGES
block|,
name|MY_FEED
block|,
name|ALGO
block|,
name|FOLLOWING
block|,
name|SENT
block|,
name|PRIVATE
block|,
name|RECEIVED
block|,
DECL|enumConstant|USERS
DECL|enumConstant|CURRENT
name|USERS
block|,
name|CURRENT
block|;
DECL|method|fromUri (String uri)
specifier|public
specifier|static
name|YammerFunctionType
name|fromUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
for|for
control|(
name|YammerFunctionType
name|endpointType
range|:
name|YammerFunctionType
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|endpointType
operator|.
name|name
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|uri
argument_list|)
condition|)
block|{
return|return
name|endpointType
return|;
block|}
block|}
return|return
name|YammerFunctionType
operator|.
name|MESSAGES
return|;
block|}
block|}
end_enum

end_unit

