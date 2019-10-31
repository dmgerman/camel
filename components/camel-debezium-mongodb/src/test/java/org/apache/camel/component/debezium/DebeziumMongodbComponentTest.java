begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.debezium
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|debezium
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

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
name|component
operator|.
name|debezium
operator|.
name|configuration
operator|.
name|MongoDbConnectorEmbeddedDebeziumConfiguration
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
name|junit
operator|.
name|Test
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
name|assertNotNull
import|;
end_import

begin_class
DECL|class|DebeziumMongodbComponentTest
specifier|public
class|class
name|DebeziumMongodbComponentTest
block|{
annotation|@
name|Test
DECL|method|testIfConnectorEndpointCreatedWithConfig ()
specifier|public
name|void
name|testIfConnectorEndpointCreatedWithConfig
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
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
literal|"offsetStorageFileName"
argument_list|,
literal|"/offset_test_file"
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"mongodbHosts"
argument_list|,
literal|"localhost"
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"mongodbUser"
argument_list|,
literal|"dbz"
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"mongodbPassword"
argument_list|,
literal|"pwd"
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"mongodbName"
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"databaseHistoryFileFilename"
argument_list|,
literal|"/db_history_file_test"
argument_list|)
expr_stmt|;
specifier|final
name|String
name|remaining
init|=
literal|"test_name"
decl_stmt|;
specifier|final
name|String
name|uri
init|=
literal|"debezium?name=test_name&offsetStorageFileName=/test&"
operator|+
literal|"databaseHostName=localhost&databaseServerId=1234&databaseUser=dbz&databasePassword=pwd&"
operator|+
literal|"databaseServerName=test&databaseHistoryFileName=/test"
decl_stmt|;
specifier|final
name|DebeziumComponent
name|debeziumComponent
init|=
operator|new
name|DebeziumMongodbComponent
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|DebeziumEndpoint
name|debeziumEndpoint
init|=
name|debeziumComponent
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
name|debeziumEndpoint
argument_list|)
expr_stmt|;
comment|// test for config
specifier|final
name|MongoDbConnectorEmbeddedDebeziumConfiguration
name|configuration
init|=
operator|(
name|MongoDbConnectorEmbeddedDebeziumConfiguration
operator|)
name|debeziumEndpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"test_name"
argument_list|,
name|configuration
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/offset_test_file"
argument_list|,
name|configuration
operator|.
name|getOffsetStorageFileName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"localhost"
argument_list|,
name|configuration
operator|.
name|getMongodbHosts
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"dbz"
argument_list|,
name|configuration
operator|.
name|getMongodbUser
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"pwd"
argument_list|,
name|configuration
operator|.
name|getMongodbPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|configuration
operator|.
name|getMongodbName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/db_history_file_test"
argument_list|,
name|configuration
operator|.
name|getDatabaseHistoryFileFilename
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIfCreatesComponentWithExternalConfiguration ()
specifier|public
name|void
name|testIfCreatesComponentWithExternalConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|MongoDbConnectorEmbeddedDebeziumConfiguration
name|configuration
init|=
operator|new
name|MongoDbConnectorEmbeddedDebeziumConfiguration
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|setName
argument_list|(
literal|"test_config"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setMongodbUser
argument_list|(
literal|"test_db"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setMongodbPassword
argument_list|(
literal|"pwd"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setOffsetStorageFileName
argument_list|(
literal|"/offset/file"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setMongodbName
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
specifier|final
name|String
name|uri
init|=
literal|"debezium:dummy"
decl_stmt|;
specifier|final
name|DebeziumComponent
name|debeziumComponent
init|=
operator|new
name|DebeziumMongodbComponent
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
decl_stmt|;
comment|// set configurations
name|debeziumComponent
operator|.
name|setConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
specifier|final
name|DebeziumEndpoint
name|debeziumEndpoint
init|=
name|debeziumComponent
operator|.
name|createEndpoint
argument_list|(
name|uri
argument_list|,
literal|null
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|debeziumEndpoint
argument_list|)
expr_stmt|;
comment|// assert configurations
specifier|final
name|MongoDbConnectorEmbeddedDebeziumConfiguration
name|actualConfigurations
init|=
operator|(
name|MongoDbConnectorEmbeddedDebeziumConfiguration
operator|)
name|debeziumEndpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|actualConfigurations
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|configuration
operator|.
name|getName
argument_list|()
argument_list|,
name|actualConfigurations
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|configuration
operator|.
name|getMongodbUser
argument_list|()
argument_list|,
name|actualConfigurations
operator|.
name|getMongodbUser
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|configuration
operator|.
name|getConnectorClass
argument_list|()
argument_list|,
name|actualConfigurations
operator|.
name|getConnectorClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|testIfItHandlesNullExternalConfigurations ()
specifier|public
name|void
name|testIfItHandlesNullExternalConfigurations
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|remaining
init|=
literal|""
decl_stmt|;
specifier|final
name|String
name|uri
init|=
literal|"debezium:"
decl_stmt|;
specifier|final
name|DebeziumComponent
name|debeziumComponent
init|=
operator|new
name|DebeziumMongodbComponent
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
decl_stmt|;
comment|// set configurations
name|debeziumComponent
operator|.
name|setConfiguration
argument_list|(
literal|null
argument_list|)
expr_stmt|;
specifier|final
name|DebeziumEndpoint
name|debeziumEndpoint
init|=
name|debeziumComponent
operator|.
name|createEndpoint
argument_list|(
name|uri
argument_list|,
name|remaining
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
decl_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|testIfItHandlesNullExternalConfigurationsWithValidUri ()
specifier|public
name|void
name|testIfItHandlesNullExternalConfigurationsWithValidUri
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|remaining
init|=
literal|"dummy"
decl_stmt|;
specifier|final
name|String
name|uri
init|=
literal|"debezium:dummy"
decl_stmt|;
specifier|final
name|DebeziumComponent
name|debeziumComponent
init|=
operator|new
name|DebeziumMongodbComponent
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
decl_stmt|;
comment|// set configurations
name|debeziumComponent
operator|.
name|setConfiguration
argument_list|(
literal|null
argument_list|)
expr_stmt|;
specifier|final
name|DebeziumEndpoint
name|debeziumEndpoint
init|=
name|debeziumComponent
operator|.
name|createEndpoint
argument_list|(
name|uri
argument_list|,
name|remaining
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
decl_stmt|;
block|}
block|}
end_class

end_unit

