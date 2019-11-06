begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kudu
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kudu
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriPath
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
name|support
operator|.
name|DefaultEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kudu
operator|.
name|client
operator|.
name|KuduClient
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
comment|/**  * Represents a Kudu endpoint. A kudu endpoint allows you to interact with  *<a href="https://kudu.apache.org/">Apache Kudu</a>, a free and open source  * column-oriented data store of the Apache Hadoop ecosystem.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"3.0"
argument_list|,
name|scheme
operator|=
literal|"kudu"
argument_list|,
name|title
operator|=
literal|"Apache Kudu"
argument_list|,
name|syntax
operator|=
literal|"kudu:host:port/tableName"
argument_list|,
name|label
operator|=
literal|"cloud,database,iot"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|)
DECL|class|KuduEndpoint
specifier|public
class|class
name|KuduEndpoint
extends|extends
name|DefaultEndpoint
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
name|KuduEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|kuduClient
specifier|private
name|KuduClient
name|kuduClient
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|name
operator|=
literal|"host"
argument_list|,
name|displayName
operator|=
literal|"Host"
argument_list|,
name|label
operator|=
literal|"common"
argument_list|,
name|description
operator|=
literal|"Host of the server to connect to"
argument_list|)
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|name
operator|=
literal|"port"
argument_list|,
name|displayName
operator|=
literal|"Port"
argument_list|,
name|label
operator|=
literal|"common"
argument_list|,
name|description
operator|=
literal|"Port of the server to connect to"
argument_list|)
DECL|field|port
specifier|private
name|String
name|port
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Operation to perform"
argument_list|)
DECL|field|operation
specifier|private
name|KuduOperations
name|operation
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|name
operator|=
literal|"tableName"
argument_list|,
name|displayName
operator|=
literal|"Table Name"
argument_list|,
name|label
operator|=
literal|"common"
argument_list|,
name|description
operator|=
literal|"Table to connect to"
argument_list|)
DECL|field|tableName
specifier|private
name|String
name|tableName
decl_stmt|;
DECL|method|KuduEndpoint (String uri, KuduComponent component)
specifier|public
name|KuduEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|KuduComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|Pattern
name|p
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^(\\S+)\\:(\\d+)\\/(\\S+)$"
argument_list|)
decl_stmt|;
name|Matcher
name|m
init|=
name|p
operator|.
name|matcher
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unrecognizable url: "
operator|+
name|uri
argument_list|)
throw|;
block|}
name|this
operator|.
name|setHost
argument_list|(
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|setPort
argument_list|(
name|m
operator|.
name|group
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|setTableName
argument_list|(
name|m
operator|.
name|group
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Connection: {}, {}"
argument_list|,
name|getHost
argument_list|()
argument_list|,
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
comment|//To facilitate tests, if the client is already created, do not recreate.
if|if
condition|(
name|this
operator|.
name|getKuduClient
argument_list|()
operator|==
literal|null
condition|)
block|{
name|setKuduClient
argument_list|(
operator|new
name|KuduClient
operator|.
name|KuduClientBuilder
argument_list|(
name|getHost
argument_list|()
operator|+
literal|":"
operator|+
name|getPort
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Resolved the host with the name {} as {}"
argument_list|,
name|getHost
argument_list|()
argument_list|,
name|getKuduClient
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"doStop()"
argument_list|)
expr_stmt|;
name|getKuduClient
argument_list|()
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Unable to shutdown kudu client"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|host
return|;
block|}
comment|/**      * Kudu master to connect to      */
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|public
name|String
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|getKuduClient ()
specifier|public
name|KuduClient
name|getKuduClient
parameter_list|()
block|{
return|return
name|kuduClient
return|;
block|}
comment|/**      * Set the client to connect to a kudu resource      */
DECL|method|setKuduClient (KuduClient kuduClient)
specifier|public
name|void
name|setKuduClient
parameter_list|(
name|KuduClient
name|kuduClient
parameter_list|)
block|{
name|this
operator|.
name|kuduClient
operator|=
name|kuduClient
expr_stmt|;
block|}
comment|/**      * Port where kudu service is listening      */
DECL|method|setPort (String port)
specifier|public
name|void
name|setPort
parameter_list|(
name|String
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
block|{
return|return
operator|new
name|KuduProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|UnsupportedOperationException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"You cannot create consumers on this endpoint"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getTableName ()
specifier|public
name|String
name|getTableName
parameter_list|()
block|{
return|return
name|tableName
return|;
block|}
comment|/**      * The name of the table where the rows are stored      */
DECL|method|setTableName (String tableName)
specifier|public
name|void
name|setTableName
parameter_list|(
name|String
name|tableName
parameter_list|)
block|{
name|this
operator|.
name|tableName
operator|=
name|tableName
expr_stmt|;
block|}
DECL|method|getOperation ()
specifier|public
name|KuduOperations
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
comment|/**      * What kind of operation is to be performed in the table      */
DECL|method|setOperation (KuduOperations operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|KuduOperations
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
block|}
end_class

end_unit

