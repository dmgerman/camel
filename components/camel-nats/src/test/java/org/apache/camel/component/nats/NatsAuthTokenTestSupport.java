begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.nats
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nats
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|testcontainers
operator|.
name|ContainerAwareTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|testcontainers
operator|.
name|Wait
import|;
end_import

begin_import
import|import
name|org
operator|.
name|testcontainers
operator|.
name|containers
operator|.
name|GenericContainer
import|;
end_import

begin_class
DECL|class|NatsAuthTokenTestSupport
specifier|public
class|class
name|NatsAuthTokenTestSupport
extends|extends
name|ContainerAwareTestSupport
block|{
DECL|field|CONTAINER_IMAGE
specifier|public
specifier|static
specifier|final
name|String
name|CONTAINER_IMAGE
init|=
literal|"nats:2.0.0"
decl_stmt|;
DECL|field|CONTAINER_NAME
specifier|public
specifier|static
specifier|final
name|String
name|CONTAINER_NAME
init|=
literal|"nats-auth-token"
decl_stmt|;
DECL|field|TOKEN
specifier|public
specifier|static
specifier|final
name|String
name|TOKEN
init|=
literal|"!admin23456"
decl_stmt|;
annotation|@
name|Override
DECL|method|createContainer ()
specifier|protected
name|GenericContainer
argument_list|<
name|?
argument_list|>
name|createContainer
parameter_list|()
block|{
return|return
name|natsContainer
argument_list|()
return|;
block|}
DECL|method|natsContainer ()
specifier|public
specifier|static
name|GenericContainer
name|natsContainer
parameter_list|()
block|{
return|return
operator|new
name|GenericContainer
argument_list|(
name|CONTAINER_IMAGE
argument_list|)
operator|.
name|withNetworkAliases
argument_list|(
name|CONTAINER_NAME
argument_list|)
operator|.
name|waitingFor
argument_list|(
name|Wait
operator|.
name|forLogMessageContaining
argument_list|(
literal|"Server is ready"
argument_list|,
literal|1
argument_list|)
argument_list|)
operator|.
name|withCommand
argument_list|(
literal|"-DV"
argument_list|,
literal|"-auth"
argument_list|,
name|TOKEN
argument_list|)
return|;
block|}
DECL|method|getNatsUrl ()
specifier|public
name|String
name|getNatsUrl
parameter_list|()
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%s@%s:%d"
argument_list|,
name|TOKEN
argument_list|,
name|getContainerHost
argument_list|(
name|CONTAINER_NAME
argument_list|)
argument_list|,
name|getContainerPort
argument_list|(
name|CONTAINER_NAME
argument_list|,
literal|4222
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

