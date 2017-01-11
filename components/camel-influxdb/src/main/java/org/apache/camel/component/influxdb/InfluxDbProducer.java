begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.influxdb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|influxdb
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
name|Exchange
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
name|InvalidPayloadException
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
name|DefaultProducer
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|influxdb
operator|.
name|InfluxDB
import|;
end_import

begin_import
import|import
name|org
operator|.
name|influxdb
operator|.
name|dto
operator|.
name|BatchPoints
import|;
end_import

begin_import
import|import
name|org
operator|.
name|influxdb
operator|.
name|dto
operator|.
name|Point
import|;
end_import

begin_import
import|import
name|org
operator|.
name|influxdb
operator|.
name|dto
operator|.
name|Query
import|;
end_import

begin_import
import|import
name|org
operator|.
name|influxdb
operator|.
name|dto
operator|.
name|QueryResult
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
comment|/**  * Producer for the InfluxDB components  *  */
end_comment

begin_class
DECL|class|InfluxDbProducer
specifier|public
class|class
name|InfluxDbProducer
extends|extends
name|DefaultProducer
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
name|InfluxDbProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
name|InfluxDbEndpoint
name|endpoint
decl_stmt|;
DECL|field|connection
name|InfluxDB
name|connection
decl_stmt|;
DECL|method|InfluxDbProducer (InfluxDbEndpoint endpoint)
specifier|public
name|InfluxDbProducer
parameter_list|(
name|InfluxDbEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|endpoint
operator|.
name|getInfluxDB
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Can't create a producer when the database connection is null"
argument_list|)
throw|;
block|}
name|this
operator|.
name|connection
operator|=
name|endpoint
operator|.
name|getInfluxDB
argument_list|()
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
comment|/**      * Processes the message exchange      *      * @param exchange the message exchange      * @throws Exception if an internal processing error has occurred.      */
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|dataBaseName
init|=
name|calculateDatabaseName
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|String
name|retentionPolicy
init|=
name|calculateRetentionPolicy
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|endpoint
operator|.
name|getOperation
argument_list|()
condition|)
block|{
case|case
name|InfluxDbOperations
operator|.
name|INSERT
case|:
name|doInsert
argument_list|(
name|exchange
argument_list|,
name|dataBaseName
argument_list|,
name|retentionPolicy
argument_list|)
expr_stmt|;
break|break;
case|case
name|InfluxDbOperations
operator|.
name|QUERY
case|:
name|doQuery
argument_list|(
name|exchange
argument_list|,
name|dataBaseName
argument_list|,
name|retentionPolicy
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The operation "
operator|+
name|endpoint
operator|.
name|getOperation
argument_list|()
operator|+
literal|" is not supported"
argument_list|)
throw|;
block|}
block|}
DECL|method|doInsert (Exchange exchange, String dataBaseName, String retentionPolicy)
specifier|private
name|void
name|doInsert
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|dataBaseName
parameter_list|,
name|String
name|retentionPolicy
parameter_list|)
throws|throws
name|InvalidPayloadException
block|{
if|if
condition|(
operator|!
name|endpoint
operator|.
name|isBatch
argument_list|()
condition|)
block|{
name|Point
name|p
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|Point
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Writing point {}"
argument_list|,
name|p
operator|.
name|lineProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|connection
operator|.
name|write
argument_list|(
name|dataBaseName
argument_list|,
name|retentionPolicy
argument_list|,
name|p
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|CamelInfluxDbException
argument_list|(
name|ex
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|BatchPoints
name|batchPoints
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|BatchPoints
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Writing BatchPoints {}"
argument_list|,
name|batchPoints
operator|.
name|lineProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|connection
operator|.
name|write
argument_list|(
name|batchPoints
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|CamelInfluxDbException
argument_list|(
name|ex
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|doQuery (Exchange exchange, String dataBaseName, String retentionPolicy)
specifier|private
name|void
name|doQuery
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|dataBaseName
parameter_list|,
name|String
name|retentionPolicy
parameter_list|)
block|{
name|String
name|query
init|=
name|calculateQuery
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|Query
name|influxdbQuery
init|=
operator|new
name|Query
argument_list|(
name|query
argument_list|,
name|dataBaseName
argument_list|)
decl_stmt|;
name|QueryResult
name|resultSet
init|=
name|connection
operator|.
name|query
argument_list|(
name|influxdbQuery
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|resultSet
argument_list|)
expr_stmt|;
block|}
DECL|method|calculateRetentionPolicy (Exchange exchange)
specifier|private
name|String
name|calculateRetentionPolicy
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|retentionPolicy
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|InfluxDbConstants
operator|.
name|RETENTION_POLICY_HEADER
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|retentionPolicy
argument_list|)
condition|)
block|{
return|return
name|retentionPolicy
return|;
block|}
return|return
name|endpoint
operator|.
name|getRetentionPolicy
argument_list|()
return|;
block|}
DECL|method|calculateDatabaseName (Exchange exchange)
specifier|private
name|String
name|calculateDatabaseName
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|dbName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|InfluxDbConstants
operator|.
name|DBNAME_HEADER
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|dbName
argument_list|)
condition|)
block|{
return|return
name|dbName
return|;
block|}
return|return
name|endpoint
operator|.
name|getDatabaseName
argument_list|()
return|;
block|}
DECL|method|calculateQuery (Exchange exchange)
specifier|private
name|String
name|calculateQuery
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|query
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|InfluxDbConstants
operator|.
name|INFLUXDB_QUERY
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|query
argument_list|)
condition|)
block|{
return|return
name|query
return|;
block|}
else|else
block|{
name|query
operator|=
name|endpoint
operator|.
name|getQuery
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|query
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The query option must be set if you want to run a query operation"
argument_list|)
throw|;
block|}
return|return
name|query
return|;
block|}
block|}
end_class

end_unit

