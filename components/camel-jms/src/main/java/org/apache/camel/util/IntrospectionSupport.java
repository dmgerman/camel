begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Bridge class for ActiveMQ component  */
end_comment

begin_class
DECL|class|IntrospectionSupport
specifier|public
class|class
name|IntrospectionSupport
block|{
DECL|method|extractProperties (Map<String, Object> properties, String optionPrefix)
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|extractProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|,
name|String
name|optionPrefix
parameter_list|)
block|{
return|return
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|properties
argument_list|,
name|optionPrefix
argument_list|)
return|;
block|}
block|}
end_class

end_unit

