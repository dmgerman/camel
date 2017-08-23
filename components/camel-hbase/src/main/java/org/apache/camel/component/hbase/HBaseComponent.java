begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hbase
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hbase
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executors
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
name|impl
operator|.
name|UriEndpointComponent
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
name|util
operator|.
name|IntrospectionSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|conf
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|HBaseConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|ConnectionFactory
import|;
end_import

begin_comment
comment|/**  * Represents the component that manages {@link HBaseEndpoint}.  */
end_comment

begin_class
DECL|class|HBaseComponent
specifier|public
class|class
name|HBaseComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|field|connection
specifier|private
name|Connection
name|connection
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|configuration
specifier|private
name|Configuration
name|configuration
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"10"
argument_list|)
DECL|field|poolMaxSize
specifier|private
name|int
name|poolMaxSize
init|=
literal|10
decl_stmt|;
DECL|method|HBaseComponent ()
specifier|public
name|HBaseComponent
parameter_list|()
block|{
name|super
argument_list|(
name|HBaseEndpoint
operator|.
name|class
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
if|if
condition|(
name|configuration
operator|==
literal|null
condition|)
block|{
name|configuration
operator|=
name|HBaseConfiguration
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getClassLoader
argument_list|()
operator|==
literal|null
condition|)
block|{
name|ClassLoader
name|applicationContextClassLoader
init|=
name|getCamelContext
argument_list|()
operator|.
name|getApplicationContextClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|applicationContextClassLoader
operator|!=
literal|null
condition|)
block|{
name|configuration
operator|.
name|setClassLoader
argument_list|(
name|applicationContextClassLoader
argument_list|)
expr_stmt|;
name|HBaseConfiguration
operator|.
name|addHbaseResources
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
block|}
name|connection
operator|=
name|ConnectionFactory
operator|.
name|createConnection
argument_list|(
name|configuration
argument_list|,
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
name|poolMaxSize
argument_list|)
argument_list|)
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
if|if
condition|(
name|connection
operator|!=
literal|null
condition|)
block|{
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|HBaseEndpoint
name|endpoint
init|=
operator|new
name|HBaseEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|connection
argument_list|,
name|remaining
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|mapping
init|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|parameters
argument_list|,
literal|"row."
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setRowMapping
argument_list|(
name|mapping
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|Configuration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
comment|/**      * To use the shared configuration      */
DECL|method|setConfiguration (Configuration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|Configuration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getPoolMaxSize ()
specifier|public
name|int
name|getPoolMaxSize
parameter_list|()
block|{
return|return
name|poolMaxSize
return|;
block|}
comment|/**      * Maximum number of references to keep for each table in the HTable pool.      * The default value is 10.      */
DECL|method|setPoolMaxSize (int poolMaxSize)
specifier|public
name|void
name|setPoolMaxSize
parameter_list|(
name|int
name|poolMaxSize
parameter_list|)
block|{
name|this
operator|.
name|poolMaxSize
operator|=
name|poolMaxSize
expr_stmt|;
block|}
block|}
end_class

end_unit

