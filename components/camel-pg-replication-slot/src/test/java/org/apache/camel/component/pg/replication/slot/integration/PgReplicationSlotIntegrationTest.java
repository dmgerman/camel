begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pg.replication.slot.integration
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pg
operator|.
name|replication
operator|.
name|slot
operator|.
name|integration
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|DriverManager
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Statement
import|;
end_import

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
name|PropertyInject
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
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|properties
operator|.
name|PropertiesComponent
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
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
DECL|class|PgReplicationSlotIntegrationTest
specifier|public
class|class
name|PgReplicationSlotIntegrationTest
extends|extends
name|CamelTestSupport
block|{
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
name|EndpointInject
argument_list|(
literal|"pg-replication-slot://{{host}}:{{port}}/{{database}}/camel_test_slot:test_decoding?"
operator|+
literal|"user={{username}}&password={{password}}&slotOptions.skip-empty-xacts=true&slotOptions.include-xids=false"
argument_list|)
DECL|field|pgReplicationSlotEndpoint
specifier|private
name|Endpoint
name|pgReplicationSlotEndpoint
decl_stmt|;
annotation|@
name|PropertyInject
argument_list|(
literal|"host"
argument_list|)
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
annotation|@
name|PropertyInject
argument_list|(
literal|"port"
argument_list|)
DECL|field|port
specifier|private
name|String
name|port
decl_stmt|;
annotation|@
name|PropertyInject
argument_list|(
literal|"database"
argument_list|)
DECL|field|database
specifier|private
name|String
name|database
decl_stmt|;
annotation|@
name|PropertyInject
argument_list|(
literal|"username"
argument_list|)
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
annotation|@
name|PropertyInject
argument_list|(
literal|"password"
argument_list|)
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
DECL|field|connection
specifier|private
name|Connection
name|connection
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|String
name|url
init|=
name|String
operator|.
name|format
argument_list|(
literal|"jdbc:postgresql://%s:%s/%s"
argument_list|,
name|this
operator|.
name|host
argument_list|,
name|this
operator|.
name|port
argument_list|,
name|this
operator|.
name|database
argument_list|)
decl_stmt|;
name|Properties
name|props
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|setProperty
argument_list|(
literal|"user"
argument_list|,
name|username
argument_list|)
expr_stmt|;
name|props
operator|.
name|setProperty
argument_list|(
literal|"password"
argument_list|,
name|password
argument_list|)
expr_stmt|;
name|this
operator|.
name|connection
operator|=
name|DriverManager
operator|.
name|getConnection
argument_list|(
name|url
argument_list|,
name|props
argument_list|)
expr_stmt|;
try|try
init|(
name|Statement
name|statement
init|=
name|this
operator|.
name|connection
operator|.
name|createStatement
argument_list|()
init|)
block|{
name|statement
operator|.
name|execute
argument_list|(
literal|"CREATE TABLE IF NOT EXISTS camel_test_table(id int);"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
try|try
init|(
name|Statement
name|statement
init|=
name|this
operator|.
name|connection
operator|.
name|createStatement
argument_list|()
init|)
block|{
name|statement
operator|.
name|execute
argument_list|(
literal|"DROP TABLE IF EXISTS camel_test_table;"
argument_list|)
expr_stmt|;
name|statement
operator|.
name|execute
argument_list|(
literal|"SELECT pg_drop_replication_slot('camel_test_slot');"
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|connection
operator|.
name|close
argument_list|()
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
name|CamelContext
name|camelContext
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|PropertiesComponent
name|component
init|=
operator|new
name|PropertiesComponent
argument_list|(
literal|"classpath:/test-options.properties"
argument_list|)
decl_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"properties"
argument_list|,
name|component
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
name|createRouteBuilder
parameter_list|()
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
name|pgReplicationSlotEndpoint
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
annotation|@
name|Test
DECL|method|canReceiveFromSlot ()
specifier|public
name|void
name|canReceiveFromSlot
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|SQLException
block|{
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// test_decoding plugin writes each change in a separate message. Some other plugins can have different behaviour,
comment|// wal2json default behaviour is to write the whole transaction in one message.
name|mockEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"BEGIN"
argument_list|,
literal|"table public.camel_test_table: INSERT: id[integer]:1984"
argument_list|,
literal|"COMMIT"
argument_list|,
literal|"BEGIN"
argument_list|,
literal|"table public.camel_test_table: INSERT: id[integer]:1998"
argument_list|,
literal|"COMMIT"
argument_list|)
expr_stmt|;
try|try
init|(
name|Statement
name|statement
init|=
name|this
operator|.
name|connection
operator|.
name|createStatement
argument_list|()
init|)
block|{
name|statement
operator|.
name|execute
argument_list|(
literal|"INSERT INTO camel_test_table(id) VALUES(1984);"
argument_list|)
expr_stmt|;
name|statement
operator|.
name|execute
argument_list|(
literal|"INSERT INTO camel_test_table(id) VALUES(1998);"
argument_list|)
expr_stmt|;
block|}
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

