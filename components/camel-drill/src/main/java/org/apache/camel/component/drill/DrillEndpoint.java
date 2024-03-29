begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.drill
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|drill
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
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
name|util
operator|.
name|List
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
name|component
operator|.
name|drill
operator|.
name|util
operator|.
name|StringUtils
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
name|Metadata
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
name|DefaultPollingEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jdbc
operator|.
name|core
operator|.
name|ColumnMapRowMapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jdbc
operator|.
name|core
operator|.
name|RowMapperResultSetExtractor
import|;
end_import

begin_comment
comment|/**  * The drill component gives you the ability to quering into apache drill  * cluster.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.19.0"
argument_list|,
name|scheme
operator|=
literal|"drill"
argument_list|,
name|title
operator|=
literal|"Drill"
argument_list|,
name|syntax
operator|=
literal|"drill:host"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"database,sql"
argument_list|)
DECL|class|DrillEndpoint
specifier|public
class|class
name|DrillEndpoint
extends|extends
name|DefaultPollingEndpoint
block|{
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Host name or IP address"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Port number"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|defaultValue
operator|=
literal|"2181"
argument_list|)
DECL|field|port
specifier|private
name|Integer
name|port
init|=
literal|2181
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Drill directory"
argument_list|,
name|defaultValue
operator|=
literal|""
argument_list|)
DECL|field|directory
specifier|private
name|String
name|directory
init|=
literal|""
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|""
argument_list|)
DECL|field|clusterId
specifier|private
name|String
name|clusterId
init|=
literal|""
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"ZK"
argument_list|,
name|enums
operator|=
literal|"ZK,DRILLBIT"
argument_list|)
DECL|field|mode
specifier|private
name|DrillConnectionMode
name|mode
init|=
name|DrillConnectionMode
operator|.
name|ZK
decl_stmt|;
comment|/**      * creates a drill endpoint      *      * @param uri the endpoint uri      * @param component the component      */
DECL|method|DrillEndpoint (String uri, DrillComponent component)
specifier|public
name|DrillEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|DrillComponent
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
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"DrillConsumer is not supported!"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|DrillProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|toJDBCUri ()
specifier|public
name|String
name|toJDBCUri
parameter_list|()
block|{
name|String
name|url
init|=
literal|"jdbc:drill:"
operator|+
name|mode
operator|.
name|name
argument_list|()
operator|.
name|toLowerCase
argument_list|()
operator|+
literal|"="
operator|+
name|host
operator|+
literal|":"
operator|+
name|port
decl_stmt|;
if|if
condition|(
name|mode
operator|.
name|equals
argument_list|(
name|DrillConnectionMode
operator|.
name|ZK
argument_list|)
condition|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|directory
argument_list|)
condition|)
block|{
name|url
operator|+=
literal|"/"
operator|+
name|directory
expr_stmt|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|clusterId
argument_list|)
condition|)
block|{
name|url
operator|+=
literal|"/"
operator|+
name|clusterId
expr_stmt|;
block|}
block|}
return|return
name|url
return|;
block|}
DECL|method|queryForList (ResultSet rs)
specifier|public
name|List
argument_list|<
name|?
argument_list|>
name|queryForList
parameter_list|(
name|ResultSet
name|rs
parameter_list|)
throws|throws
name|SQLException
block|{
name|ColumnMapRowMapper
name|rowMapper
init|=
operator|new
name|ColumnMapRowMapper
argument_list|()
decl_stmt|;
name|RowMapperResultSetExtractor
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|mapper
init|=
operator|new
name|RowMapperResultSetExtractor
argument_list|<>
argument_list|(
name|rowMapper
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|data
init|=
name|mapper
operator|.
name|extractData
argument_list|(
name|rs
argument_list|)
decl_stmt|;
return|return
name|data
return|;
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
comment|/**      * ZooKeeper host name or IP address. Use local instead of a host name or IP      * address to connect to the local Drillbit      *       * @param host      */
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
name|Integer
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
comment|/**      * ZooKeeper port number      *       * @param port      */
DECL|method|setPort (Integer port)
specifier|public
name|void
name|setPort
parameter_list|(
name|Integer
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
DECL|method|getDirectory ()
specifier|public
name|String
name|getDirectory
parameter_list|()
block|{
return|return
name|directory
return|;
block|}
comment|/**      * Drill directory in ZooKeeper      *       * @param directory      */
DECL|method|setDirectory (String directory)
specifier|public
name|void
name|setDirectory
parameter_list|(
name|String
name|directory
parameter_list|)
block|{
name|this
operator|.
name|directory
operator|=
name|directory
expr_stmt|;
block|}
DECL|method|getClusterId ()
specifier|public
name|String
name|getClusterId
parameter_list|()
block|{
return|return
name|clusterId
return|;
block|}
comment|/**      * Cluster ID      * https://drill.apache.org/docs/using-the-jdbc-driver/#determining-the-cluster-id      *       * @param clusterId      */
DECL|method|setClusterId (String clusterId)
specifier|public
name|void
name|setClusterId
parameter_list|(
name|String
name|clusterId
parameter_list|)
block|{
name|this
operator|.
name|clusterId
operator|=
name|clusterId
expr_stmt|;
block|}
comment|/**      * Connection mode: zk: Zookeeper drillbit: Drillbit direct connection      * https://drill.apache.org/docs/using-the-jdbc-driver/      *       * @return      */
DECL|method|getMode ()
specifier|public
name|DrillConnectionMode
name|getMode
parameter_list|()
block|{
return|return
name|mode
return|;
block|}
DECL|method|setMode (DrillConnectionMode mode)
specifier|public
name|void
name|setMode
parameter_list|(
name|DrillConnectionMode
name|mode
parameter_list|)
block|{
name|this
operator|.
name|mode
operator|=
name|mode
expr_stmt|;
block|}
block|}
end_class

end_unit

