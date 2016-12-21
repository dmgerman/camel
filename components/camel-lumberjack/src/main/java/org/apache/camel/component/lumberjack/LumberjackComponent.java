begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.lumberjack
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|lumberjack
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
name|jsse
operator|.
name|SSLContextParameters
import|;
end_import

begin_comment
comment|/**  * The class is the Camel component for the Lumberjack server  */
end_comment

begin_class
DECL|class|LumberjackComponent
specifier|public
class|class
name|LumberjackComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|field|DEFAULT_PORT
specifier|static
specifier|final
name|int
name|DEFAULT_PORT
init|=
literal|5044
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
DECL|method|LumberjackComponent ()
specifier|public
name|LumberjackComponent
parameter_list|()
block|{
name|this
argument_list|(
name|LumberjackEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|LumberjackComponent (Class<? extends LumberjackEndpoint> endpointClass)
specifier|protected
name|LumberjackComponent
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|LumberjackEndpoint
argument_list|>
name|endpointClass
parameter_list|)
block|{
name|super
argument_list|(
name|endpointClass
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
comment|// Extract host and port
name|String
name|host
decl_stmt|;
name|int
name|port
decl_stmt|;
name|int
name|separatorIndex
init|=
name|remaining
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
name|separatorIndex
operator|>=
literal|0
condition|)
block|{
name|host
operator|=
name|remaining
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|separatorIndex
argument_list|)
expr_stmt|;
name|port
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|remaining
operator|.
name|substring
argument_list|(
name|separatorIndex
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|host
operator|=
name|remaining
expr_stmt|;
name|port
operator|=
name|DEFAULT_PORT
expr_stmt|;
block|}
comment|// Create the endpoint
name|Endpoint
name|answer
init|=
operator|new
name|LumberjackEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|host
argument_list|,
name|port
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|answer
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|getSslContextParameters ()
specifier|public
name|SSLContextParameters
name|getSslContextParameters
parameter_list|()
block|{
return|return
name|sslContextParameters
return|;
block|}
comment|/**      * Sets the default SSL configuration to use for all the endpoints. You can also configure it directly at      * the endpoint level.      */
DECL|method|setSslContextParameters (SSLContextParameters sslContextParameters)
specifier|public
name|void
name|setSslContextParameters
parameter_list|(
name|SSLContextParameters
name|sslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|sslContextParameters
operator|=
name|sslContextParameters
expr_stmt|;
block|}
block|}
end_class

end_unit

