begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.config
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|config
package|;
end_package

begin_class
DECL|class|MyDependsOnBean
specifier|public
class|class
name|MyDependsOnBean
block|{
DECL|field|time
specifier|private
name|long
name|time
decl_stmt|;
DECL|field|endpointName
specifier|private
name|String
name|endpointName
decl_stmt|;
DECL|method|getEndpointName ()
specifier|public
name|String
name|getEndpointName
parameter_list|()
block|{
return|return
name|endpointName
return|;
block|}
DECL|method|setEndpointName (String endpointName)
specifier|public
name|void
name|setEndpointName
parameter_list|(
name|String
name|endpointName
parameter_list|)
block|{
comment|// time when we was invoked by Spring
name|this
operator|.
name|time
operator|=
name|System
operator|.
name|nanoTime
argument_list|()
expr_stmt|;
name|this
operator|.
name|endpointName
operator|=
name|endpointName
expr_stmt|;
block|}
DECL|method|getTime ()
specifier|public
name|long
name|getTime
parameter_list|()
block|{
return|return
name|time
return|;
block|}
block|}
end_class

end_unit

