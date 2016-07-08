begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.lumberjack
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|lumberjack
package|;
end_package

begin_comment
comment|/**  * The class is the Camel component for the Lumberjack server with SSL  */
end_comment

begin_class
DECL|class|LumberjacksComponent
specifier|public
class|class
name|LumberjacksComponent
extends|extends
name|LumberjackComponent
block|{
DECL|method|LumberjacksComponent ()
specifier|public
name|LumberjacksComponent
parameter_list|()
block|{
name|super
argument_list|(
name|LumberjacksEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String host, int port)
specifier|protected
name|LumberjacksEndpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|)
block|{
return|return
operator|new
name|LumberjacksEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|host
argument_list|,
name|port
argument_list|)
return|;
block|}
block|}
end_class

end_unit

