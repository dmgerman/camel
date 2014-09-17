begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.github.producer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|github
operator|.
name|producer
package|;
end_package

begin_enum
DECL|enum|ProducerType
specifier|public
enum|enum
name|ProducerType
block|{
DECL|enumConstant|PULLREQUESTCOMMENT
DECL|enumConstant|UNKNOWN
name|PULLREQUESTCOMMENT
block|,
name|UNKNOWN
block|;
DECL|method|fromUri (String uri)
specifier|public
specifier|static
name|ProducerType
name|fromUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
for|for
control|(
name|ProducerType
name|producerType
range|:
name|ProducerType
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|producerType
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
name|producerType
return|;
block|}
block|}
return|return
name|ProducerType
operator|.
name|UNKNOWN
return|;
block|}
block|}
end_enum

end_unit

