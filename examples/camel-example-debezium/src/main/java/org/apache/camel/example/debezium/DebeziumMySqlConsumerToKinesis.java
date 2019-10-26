begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.debezium
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
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
name|aws
operator|.
name|kinesis
operator|.
name|KinesisConstants
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
name|DebeziumConstants
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
name|main
operator|.
name|Main
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
name|model
operator|.
name|dataformat
operator|.
name|JsonLibrary
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|connect
operator|.
name|data
operator|.
name|Struct
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A simple example to consume data from Debezium and send it to Kinesis  */
end_comment

begin_class
DECL|class|DebeziumMySqlConsumerToKinesis
specifier|public
specifier|final
class|class
name|DebeziumMySqlConsumerToKinesis
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DebeziumMySqlConsumerToKinesis
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// use Camel Main to setup and run Camel
DECL|field|main
specifier|private
specifier|static
name|Main
name|main
init|=
operator|new
name|Main
argument_list|()
decl_stmt|;
DECL|method|DebeziumMySqlConsumerToKinesis ()
specifier|private
name|DebeziumMySqlConsumerToKinesis
parameter_list|()
block|{     }
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"About to run Debezium integration..."
argument_list|)
expr_stmt|;
comment|// add route
name|main
operator|.
name|addRoutesBuilder
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// Initial Debezium route that will run and listens to the changes,
comment|// first it will perform an initial snapshot using (select * from) in case there are no offsets
comment|// exists for the connector and then it will listens to MySQL binlogs for any DB events such as (UPDATE, INSERT and DELETE)
name|from
argument_list|(
literal|"debezium:mysql?name={{debezium.mysql.name}}"
operator|+
literal|"&databaseServerId={{debezium.mysql.databaseServerId}}"
operator|+
literal|"&databaseHostname={{debezium.mysql.databaseHostName}}"
operator|+
literal|"&databaseUser={{debezium.mysql.databaseUser}}"
operator|+
literal|"&databasePassword={{debezium.mysql.databasePassword}}"
operator|+
literal|"&databaseServerName={{debezium.mysql.databaseServerName}}"
operator|+
literal|"&databaseHistoryFileFilename={{debezium.mysql.databaseHistoryFileName}}"
operator|+
literal|"&databaseWhitelist={{debezium.mysql.databaseWhitelist}}"
operator|+
literal|"&tableWhitelist={{debezium.mysql.tableWhitelist}}"
operator|+
literal|"&offsetStorageFileName={{debezium.mysql.offsetStorageFileName}}"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"FromDebeziumMySql"
argument_list|)
comment|// We will need to prepare the data for Kinesis, however we need to mention here is that Kinesis is bit different from Kafka in terms
comment|// of the key partition which only limited to 256 byte length, depending on the size of your key, that may not be optimal. Therefore, the safer option is to hash the key
comment|// and convert it to string, but that means we need to preserve the key information into the message body in order not to lose these information downstream.
comment|// Note: If you'd use Kafka, most probably you will not need these transformations as you can send the key as an object and Kafka will do
comment|// the rest to hash it in the broker in order to place it in the correct topic's partition.
operator|.
name|setBody
argument_list|(
name|exchange
lambda|->
block|{
comment|// Using Camel Data Format, we can retrieve our data in Map since Debezium component has a Type Converter from Struct to Map, you need to specify the Map.class
comment|// in order to convert the data from Struct to Map
specifier|final
name|Map
name|key
init|=
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|getHeader
argument_list|(
name|DebeziumConstants
operator|.
name|HEADER_KEY
argument_list|,
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|Map
name|value
init|=
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|getBody
argument_list|(
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Also, we need the operation in order to determine when an INSERT, UPDATE or DELETE happens
specifier|final
name|String
name|operation
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|getHeader
argument_list|(
name|DebeziumConstants
operator|.
name|HEADER_OPERATION
argument_list|)
decl_stmt|;
comment|// We we will put everything as nested Map in order to utilize Camel's Type Format
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|kinesisBody
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|kinesisBody
operator|.
name|put
argument_list|(
literal|"key"
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|kinesisBody
operator|.
name|put
argument_list|(
literal|"value"
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|kinesisBody
operator|.
name|put
argument_list|(
literal|"operation"
argument_list|,
name|operation
argument_list|)
expr_stmt|;
return|return
name|kinesisBody
return|;
block|}
argument_list|)
comment|// As we mentioned above, we will need to hash the key partition and set it into the headers
operator|.
name|process
argument_list|(
name|exchange
lambda|->
block|{
specifier|final
name|Struct
name|key
init|=
operator|(
name|Struct
operator|)
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|getHeader
argument_list|(
name|DebeziumConstants
operator|.
name|HEADER_KEY
argument_list|)
decl_stmt|;
specifier|final
name|String
name|hash
init|=
name|String
operator|.
name|valueOf
argument_list|(
name|key
operator|.
name|hashCode
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|setHeader
argument_list|(
name|KinesisConstants
operator|.
name|PARTITION_KEY
argument_list|,
name|hash
argument_list|)
expr_stmt|;
block|}
argument_list|)
comment|// Marshal everything to JSON, you can use any other data format such as Avro, Protobuf..etc, but in this example we will keep it to JSON for simplicity
operator|.
name|marshal
argument_list|()
operator|.
name|json
argument_list|(
name|JsonLibrary
operator|.
name|Jackson
argument_list|)
comment|// Send our data to kinesis
operator|.
name|to
argument_list|(
literal|"aws-kinesis:{{kinesis.streamName}}?accessKey=RAW({{kinesis.accessKey}})"
operator|+
literal|"&secretKey=RAW({{kinesis.secretKey}})"
operator|+
literal|"&region={{kinesis.region}}"
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// start and run Camel (block)
name|main
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

