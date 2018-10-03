begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.wordpress
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|wordpress
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
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|DefaultComponent
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
name|support
operator|.
name|IntrospectionSupport
import|;
end_import

begin_comment
comment|/**  * Represents the component that manages {@link WordpressEndpoint}.  */
end_comment

begin_class
DECL|class|WordpressComponent
specifier|public
class|class
name|WordpressComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|OP_SEPARATOR
specifier|private
specifier|static
specifier|final
name|String
name|OP_SEPARATOR
init|=
literal|":"
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Wordpress component configuration"
argument_list|)
DECL|field|configuration
specifier|private
name|WordpressComponentConfiguration
name|configuration
decl_stmt|;
DECL|method|WordpressComponent ()
specifier|public
name|WordpressComponent
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|WordpressComponentConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|WordpressComponent (WordpressComponentConfiguration configuration)
specifier|public
name|WordpressComponent
parameter_list|(
name|WordpressComponentConfiguration
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
DECL|method|WordpressComponent (CamelContext camelContext)
specifier|public
name|WordpressComponent
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
operator|new
name|WordpressComponentConfiguration
argument_list|()
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|WordpressComponentConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (WordpressComponentConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|WordpressComponentConfiguration
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
specifier|final
name|WordpressComponentConfiguration
name|endpointConfiguration
init|=
name|this
operator|.
name|copyComponentProperties
argument_list|()
decl_stmt|;
name|WordpressEndpoint
name|endpoint
init|=
operator|new
name|WordpressEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|endpointConfiguration
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|this
operator|.
name|discoverOperations
argument_list|(
name|endpoint
argument_list|,
name|remaining
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|configureProperties
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|discoverOperations (WordpressEndpoint endpoint, String remaining)
specifier|private
name|void
name|discoverOperations
parameter_list|(
name|WordpressEndpoint
name|endpoint
parameter_list|,
name|String
name|remaining
parameter_list|)
block|{
specifier|final
name|String
index|[]
name|operations
init|=
name|remaining
operator|.
name|split
argument_list|(
name|OP_SEPARATOR
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setOperation
argument_list|(
name|operations
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|operations
operator|.
name|length
operator|>
literal|1
condition|)
block|{
name|endpoint
operator|.
name|setOperationDetail
argument_list|(
name|operations
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|copyComponentProperties ()
specifier|private
name|WordpressComponentConfiguration
name|copyComponentProperties
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
name|componentProperties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|IntrospectionSupport
operator|.
name|getProperties
argument_list|(
name|configuration
argument_list|,
name|componentProperties
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// create endpoint configuration with component properties
name|WordpressComponentConfiguration
name|config
init|=
operator|new
name|WordpressComponentConfiguration
argument_list|()
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|config
argument_list|,
name|componentProperties
argument_list|)
expr_stmt|;
return|return
name|config
return|;
block|}
block|}
end_class

end_unit

