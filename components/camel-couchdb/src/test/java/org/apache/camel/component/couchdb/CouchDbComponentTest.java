begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.couchdb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|couchdb
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|junit
operator|.
name|MockitoJUnitRunner
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
DECL|class|CouchDbComponentTest
specifier|public
class|class
name|CouchDbComponentTest
block|{
annotation|@
name|Mock
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
annotation|@
name|Test
DECL|method|testEndpointCreated ()
specifier|public
name|void
name|testEndpointCreated
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|String
name|uri
init|=
literal|"couchdb:http://localhost:5984/db"
decl_stmt|;
name|String
name|remaining
init|=
literal|"http://localhost:5984/db"
decl_stmt|;
name|Endpoint
name|endpoint
init|=
operator|new
name|CouchDbComponent
argument_list|(
name|context
argument_list|)
operator|.
name|createEndpoint
argument_list|(
name|uri
argument_list|,
name|remaining
argument_list|,
name|params
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPropertiesSet ()
specifier|public
name|void
name|testPropertiesSet
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"createDatabase"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"username"
argument_list|,
literal|"coldplay"
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"password"
argument_list|,
literal|"chrism"
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"heartbeat"
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"style"
argument_list|,
literal|"gothic"
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"deletes"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"updates"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
literal|"couchdb:http://localhost:14/db"
decl_stmt|;
name|String
name|remaining
init|=
literal|"http://localhost:14/db"
decl_stmt|;
name|CouchDbEndpoint
name|endpoint
init|=
operator|new
name|CouchDbComponent
argument_list|(
name|context
argument_list|)
operator|.
name|createEndpoint
argument_list|(
name|uri
argument_list|,
name|remaining
argument_list|,
name|params
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http"
argument_list|,
name|endpoint
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"localhost"
argument_list|,
name|endpoint
operator|.
name|getHostname
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"db"
argument_list|,
name|endpoint
operator|.
name|getDatabase
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"coldplay"
argument_list|,
name|endpoint
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"gothic"
argument_list|,
name|endpoint
operator|.
name|getStyle
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"chrism"
argument_list|,
name|endpoint
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|.
name|isCreateDatabase
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|endpoint
operator|.
name|isDeletes
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|endpoint
operator|.
name|isUpdates
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|14
argument_list|,
name|endpoint
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1000
argument_list|,
name|endpoint
operator|.
name|getHeartbeat
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

