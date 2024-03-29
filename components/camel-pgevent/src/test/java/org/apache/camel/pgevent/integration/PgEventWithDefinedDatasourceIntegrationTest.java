begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.pgevent.integration
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|pgevent
operator|.
name|integration
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|com
operator|.
name|impossibl
operator|.
name|postgres
operator|.
name|jdbc
operator|.
name|PGDataSource
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
name|BindToRegistry
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
name|EndpointInject
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
name|RoutesBuilder
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
name|builder
operator|.
name|RouteBuilder
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
name|mock
operator|.
name|MockEndpoint
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
DECL|class|PgEventWithDefinedDatasourceIntegrationTest
specifier|public
class|class
name|PgEventWithDefinedDatasourceIntegrationTest
extends|extends
name|AbstractPgEventIntegrationTest
block|{
annotation|@
name|EndpointInject
argument_list|(
literal|"pgevent:///{{database}}/testchannel?datasource=#pgDataSource"
argument_list|)
DECL|field|subscribeEndpoint
specifier|private
name|Endpoint
name|subscribeEndpoint
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"pgevent:///{{database}}/testchannel?datasource=#pgDataSource"
argument_list|)
DECL|field|notifyEndpoint
specifier|private
name|Endpoint
name|notifyEndpoint
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"timer://test?repeatCount=1&period=1"
argument_list|)
DECL|field|timerEndpoint
specifier|private
name|Endpoint
name|timerEndpoint
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:result"
argument_list|)
DECL|field|mockEndpoint
specifier|private
name|MockEndpoint
name|mockEndpoint
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"pgDataSource"
argument_list|)
DECL|method|loadDataSource ()
specifier|public
name|PGDataSource
name|loadDataSource
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|load
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/test-options.properties"
argument_list|)
argument_list|)
expr_stmt|;
name|PGDataSource
name|dataSource
init|=
operator|new
name|PGDataSource
argument_list|()
decl_stmt|;
name|dataSource
operator|.
name|setHost
argument_list|(
name|properties
operator|.
name|getProperty
argument_list|(
literal|"host"
argument_list|)
argument_list|)
expr_stmt|;
name|dataSource
operator|.
name|setPort
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|properties
operator|.
name|getProperty
argument_list|(
literal|"port"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|dataSource
operator|.
name|setDatabaseName
argument_list|(
name|properties
operator|.
name|getProperty
argument_list|(
literal|"database"
argument_list|)
argument_list|)
expr_stmt|;
name|dataSource
operator|.
name|setUser
argument_list|(
name|properties
operator|.
name|getProperty
argument_list|(
literal|"userName"
argument_list|)
argument_list|)
expr_stmt|;
name|dataSource
operator|.
name|setPassword
argument_list|(
name|properties
operator|.
name|getProperty
argument_list|(
literal|"password"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|dataSource
return|;
block|}
annotation|@
name|Test
DECL|method|testPgEventPublishSubscribeWithDefinedDatasource ()
specifier|public
name|void
name|testPgEventPublishSubscribeWithDefinedDatasource
parameter_list|()
throws|throws
name|Exception
block|{
name|mockEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|TEST_MESSAGE_BODY
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|timerEndpoint
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
name|TEST_MESSAGE_BODY
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|notifyEndpoint
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|subscribeEndpoint
argument_list|)
operator|.
name|to
argument_list|(
name|mockEndpoint
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

