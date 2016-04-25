begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|ActiveMQConnectionFactory
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
name|CamelContext
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
name|Endpoint
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
name|component
operator|.
name|sjms
operator|.
name|SjmsEndpoint
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
name|component
operator|.
name|sjms
operator|.
name|jms
operator|.
name|ConnectionFactoryResource
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
name|component
operator|.
name|sjms
operator|.
name|jms
operator|.
name|ConnectionResource
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
name|impl
operator|.
name|DefaultCamelContext
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
name|impl
operator|.
name|SimpleRegistry
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
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|SjmsEndpointConnectionSettingsTest
specifier|public
class|class
name|SjmsEndpointConnectionSettingsTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|connectionFactory
specifier|private
specifier|static
specifier|final
name|ActiveMQConnectionFactory
name|connectionFactory
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
literal|"vm://broker?broker.persistent=false&broker.useJmx=false"
argument_list|)
decl_stmt|;
DECL|field|connectionResource
specifier|private
specifier|static
specifier|final
name|ConnectionResource
name|connectionResource
init|=
operator|new
name|ConnectionFactoryResource
argument_list|(
literal|2
argument_list|,
name|connectionFactory
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testConnectionFactory ()
specifier|public
name|void
name|testConnectionFactory
parameter_list|()
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"sjms:queue:test?connectionFactory=activemq"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|instanceof
name|SjmsEndpoint
argument_list|)
expr_stmt|;
name|SjmsEndpoint
name|qe
init|=
operator|(
name|SjmsEndpoint
operator|)
name|endpoint
decl_stmt|;
name|assertEquals
argument_list|(
name|connectionFactory
argument_list|,
name|qe
operator|.
name|getConnectionFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConnectionResource ()
specifier|public
name|void
name|testConnectionResource
parameter_list|()
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"sjms:queue:test?connectionResource=connresource"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|instanceof
name|SjmsEndpoint
argument_list|)
expr_stmt|;
name|SjmsEndpoint
name|qe
init|=
operator|(
name|SjmsEndpoint
operator|)
name|endpoint
decl_stmt|;
name|assertEquals
argument_list|(
name|connectionResource
argument_list|,
name|qe
operator|.
name|getConnectionResource
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConnectionCount ()
specifier|public
name|void
name|testConnectionCount
parameter_list|()
block|{
name|Random
name|random
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
name|int
name|poolSize
init|=
name|random
operator|.
name|nextInt
argument_list|(
literal|100
argument_list|)
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"sjms:queue:test?connectionCount="
operator|+
name|poolSize
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|instanceof
name|SjmsEndpoint
argument_list|)
expr_stmt|;
name|SjmsEndpoint
name|qe
init|=
operator|(
name|SjmsEndpoint
operator|)
name|endpoint
decl_stmt|;
name|assertEquals
argument_list|(
name|poolSize
argument_list|,
name|qe
operator|.
name|getConnectionCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|put
argument_list|(
literal|"activemq"
argument_list|,
name|connectionFactory
argument_list|)
expr_stmt|;
name|registry
operator|.
name|put
argument_list|(
literal|"connresource"
argument_list|,
name|connectionResource
argument_list|)
expr_stmt|;
return|return
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
return|;
block|}
block|}
end_class

end_unit

