begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.nsq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nsq
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|FixedHostPortGenericContainer
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

begin_import
import|import
name|org
operator|.
name|testcontainers
operator|.
name|containers
operator|.
name|Network
import|;
end_import

begin_class
DECL|class|NsqTestSupport
specifier|public
class|class
name|NsqTestSupport
extends|extends
name|ContainerAwareTestSupport
block|{
DECL|field|CONTAINER_NSQLOOKUPD_IMAGE
specifier|public
specifier|static
specifier|final
name|String
name|CONTAINER_NSQLOOKUPD_IMAGE
init|=
literal|"nsqio/nsq:v1.1.0"
decl_stmt|;
DECL|field|CONTAINER_NSQLOOKUPD_NAME
specifier|public
specifier|static
specifier|final
name|String
name|CONTAINER_NSQLOOKUPD_NAME
init|=
literal|"nsqlookupd"
decl_stmt|;
DECL|field|CONTAINER_NSQD_IMAGE
specifier|public
specifier|static
specifier|final
name|String
name|CONTAINER_NSQD_IMAGE
init|=
literal|"nsqio/nsq:v1.1.0"
decl_stmt|;
DECL|field|CONTAINER_NSQD_NAME
specifier|public
specifier|static
specifier|final
name|String
name|CONTAINER_NSQD_NAME
init|=
literal|"nsqd"
decl_stmt|;
DECL|field|network
name|Network
name|network
decl_stmt|;
annotation|@
name|Override
DECL|method|createContainers ()
specifier|protected
name|List
argument_list|<
name|GenericContainer
argument_list|<
name|?
argument_list|>
argument_list|>
name|createContainers
parameter_list|()
block|{
name|network
operator|=
name|Network
operator|.
name|newNetwork
argument_list|()
expr_stmt|;
return|return
operator|new
name|ArrayList
argument_list|<
name|GenericContainer
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|nsqlookupdContainer
argument_list|(
name|network
argument_list|)
argument_list|,
name|nsqdContainer
argument_list|(
name|network
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|nsqlookupdContainer (Network network)
specifier|public
specifier|static
name|GenericContainer
argument_list|<
name|?
argument_list|>
name|nsqlookupdContainer
parameter_list|(
name|Network
name|network
parameter_list|)
block|{
return|return
operator|new
name|FixedHostPortGenericContainer
argument_list|<>
argument_list|(
name|CONTAINER_NSQLOOKUPD_IMAGE
argument_list|)
operator|.
name|withFixedExposedPort
argument_list|(
literal|4160
argument_list|,
literal|4160
argument_list|)
operator|.
name|withFixedExposedPort
argument_list|(
literal|4161
argument_list|,
literal|4161
argument_list|)
operator|.
name|withNetworkAliases
argument_list|(
name|CONTAINER_NSQLOOKUPD_NAME
argument_list|)
operator|.
name|withCommand
argument_list|(
literal|"/nsqlookupd"
argument_list|)
operator|.
name|withNetwork
argument_list|(
name|network
argument_list|)
operator|.
name|waitingFor
argument_list|(
name|Wait
operator|.
name|forLogMessageContaining
argument_list|(
literal|"TCP: listening on"
argument_list|,
literal|1
argument_list|)
argument_list|)
return|;
block|}
DECL|method|nsqdContainer (Network network)
specifier|public
specifier|static
name|GenericContainer
argument_list|<
name|?
argument_list|>
name|nsqdContainer
parameter_list|(
name|Network
name|network
parameter_list|)
block|{
return|return
operator|new
name|FixedHostPortGenericContainer
argument_list|<>
argument_list|(
name|CONTAINER_NSQD_IMAGE
argument_list|)
operator|.
name|withFixedExposedPort
argument_list|(
literal|4150
argument_list|,
literal|4150
argument_list|)
operator|.
name|withFixedExposedPort
argument_list|(
literal|4151
argument_list|,
literal|4151
argument_list|)
operator|.
name|withNetworkAliases
argument_list|(
name|CONTAINER_NSQD_NAME
argument_list|)
operator|.
name|withCommand
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"/nsqd --broadcast-address=%s --lookupd-tcp-address=%s:4160"
argument_list|,
literal|"localhost"
argument_list|,
name|CONTAINER_NSQLOOKUPD_NAME
argument_list|)
argument_list|)
operator|.
name|withNetwork
argument_list|(
name|network
argument_list|)
operator|.
name|waitingFor
argument_list|(
name|Wait
operator|.
name|forLogMessageContaining
argument_list|(
literal|"TCP: listening on"
argument_list|,
literal|1
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getNsqConsumerUrl ()
specifier|public
name|String
name|getNsqConsumerUrl
parameter_list|()
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%s:%d"
argument_list|,
literal|"localhost"
argument_list|,
literal|4161
argument_list|)
return|;
block|}
DECL|method|getNsqProducerUrl ()
specifier|public
name|String
name|getNsqProducerUrl
parameter_list|()
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%s:%d"
argument_list|,
literal|"localhost"
argument_list|,
literal|4150
argument_list|)
return|;
block|}
block|}
end_class

end_unit

