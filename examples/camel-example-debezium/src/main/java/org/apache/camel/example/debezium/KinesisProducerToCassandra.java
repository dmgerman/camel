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
name|Arrays
import|;
end_import

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
comment|/**  * A simple example to sink data from Kinesis that produced by Debezium into Cassandra  */
end_comment

begin_class
DECL|class|KinesisProducerToCassandra
specifier|public
specifier|final
class|class
name|KinesisProducerToCassandra
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
name|KinesisProducerToCassandra
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
DECL|method|KinesisProducerToCassandra ()
specifier|private
name|KinesisProducerToCassandra
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
literal|"About to run Kinesis to Cassandra integration..."
argument_list|)
expr_stmt|;
comment|// add route
name|main
operator|.
name|addRouteBuilder
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
comment|// We set the CQL templates we need, note that an UPDATE in Cassandra means an UPSERT which is what we need
specifier|final
name|String
name|cqlUpdate
init|=
literal|"update products set name = ?, description = ?, weight = ? where id = ?"
decl_stmt|;
specifier|final
name|String
name|cqlDelete
init|=
literal|"delete from products where id = ?"
decl_stmt|;
name|from
argument_list|(
literal|"aws-kinesis:{{kinesis.streamName}}?accessKey=RAW({{kinesis.accessKey}})"
operator|+
literal|"&secretKey=RAW({{kinesis.secretKey}})"
operator|+
literal|"&region={{kinesis.region}}"
argument_list|)
comment|// Since we expect the data of the body to be ByteArr, we convert it to String using Kinesis
comment|// Type Converter, in order to unmarshal later from JSON to Map
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
comment|// Unmarshal our body, it will convert it from JSON to Map
operator|.
name|unmarshal
argument_list|()
operator|.
name|json
argument_list|(
name|JsonLibrary
operator|.
name|Jackson
argument_list|)
comment|// In order not to lose the operation that we set in Debezium, we set it as a property or you can as
comment|// as well set it to a header
operator|.
name|setProperty
argument_list|(
literal|"DBOperation"
argument_list|,
name|simple
argument_list|(
literal|"${body[operation]}"
argument_list|)
argument_list|)
operator|.
name|choice
argument_list|()
comment|// If we have a INSERT or UPDATE, we will need to set the body with with the CQL query parameters since we are using
comment|// camel-cassandraql component
operator|.
name|when
argument_list|(
name|exchangeProperty
argument_list|(
literal|"DBOperation"
argument_list|)
operator|.
name|in
argument_list|(
literal|"c"
argument_list|,
literal|"u"
argument_list|)
argument_list|)
operator|.
name|setBody
argument_list|(
name|exchange
lambda|->
block|{
specifier|final
name|Map
name|body
init|=
operator|(
name|Map
operator|)
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
specifier|final
name|Map
name|value
init|=
operator|(
name|Map
operator|)
name|body
operator|.
name|get
argument_list|(
literal|"value"
argument_list|)
decl_stmt|;
specifier|final
name|Map
name|key
init|=
operator|(
name|Map
operator|)
name|body
operator|.
name|get
argument_list|(
literal|"key"
argument_list|)
decl_stmt|;
comment|// We as well check for nulls
specifier|final
name|String
name|name
init|=
name|value
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
operator|!=
literal|null
condition|?
name|value
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
operator|.
name|toString
argument_list|()
else|:
literal|""
decl_stmt|;
specifier|final
name|String
name|description
init|=
name|value
operator|.
name|get
argument_list|(
literal|"description"
argument_list|)
operator|!=
literal|null
condition|?
name|value
operator|.
name|get
argument_list|(
literal|"description"
argument_list|)
operator|.
name|toString
argument_list|()
else|:
literal|""
decl_stmt|;
specifier|final
name|float
name|weight
init|=
name|value
operator|.
name|get
argument_list|(
literal|"weight"
argument_list|)
operator|!=
literal|null
condition|?
name|Float
operator|.
name|parseFloat
argument_list|(
name|value
operator|.
name|get
argument_list|(
literal|"weight"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
else|:
literal|0
decl_stmt|;
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|name
argument_list|,
name|description
argument_list|,
name|weight
argument_list|,
name|key
operator|.
name|get
argument_list|(
literal|"id"
argument_list|)
argument_list|)
return|;
block|}
argument_list|)
comment|// We set the appropriate query in the header so we don't run the same route twice
operator|.
name|setHeader
argument_list|(
literal|"CQLQuery"
argument_list|,
name|constant
argument_list|(
name|cqlUpdate
argument_list|)
argument_list|)
comment|// If we have a DELETE, then we just set the id as a query parameter in the body
operator|.
name|when
argument_list|(
name|exchangeProperty
argument_list|(
literal|"DBOperation"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"d"
argument_list|)
argument_list|)
operator|.
name|setBody
argument_list|(
name|exchange
lambda|->
block|{
specifier|final
name|Map
name|body
init|=
operator|(
name|Map
operator|)
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
specifier|final
name|Map
name|key
init|=
operator|(
name|Map
operator|)
name|body
operator|.
name|get
argument_list|(
literal|"key"
argument_list|)
decl_stmt|;
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|key
operator|.
name|get
argument_list|(
literal|"id"
argument_list|)
argument_list|)
return|;
block|}
argument_list|)
comment|// We set the appropriate query in the header so we don't run the same route twice
operator|.
name|setHeader
argument_list|(
literal|"CQLQuery"
argument_list|,
name|constant
argument_list|(
name|cqlDelete
argument_list|)
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|choice
argument_list|()
comment|// We just make sure we ONLY handle INSERT, UPDATE and DELETE and nothing else
operator|.
name|when
argument_list|(
name|exchangeProperty
argument_list|(
literal|"DBOperation"
argument_list|)
operator|.
name|in
argument_list|(
literal|"c"
argument_list|,
literal|"u"
argument_list|,
literal|"d"
argument_list|)
argument_list|)
comment|// Send query to Cassandra
operator|.
name|recipientList
argument_list|(
name|simple
argument_list|(
literal|"cql:{{cassandra.host}}/{{cassandra.keyspace}}?cql=RAW(${header.CQLQuery})"
argument_list|)
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

