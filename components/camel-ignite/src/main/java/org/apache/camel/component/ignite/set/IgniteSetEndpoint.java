begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ignite.set
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ignite
operator|.
name|set
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
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
name|ignite
operator|.
name|AbstractIgniteEndpoint
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
name|ignite
operator|.
name|IgniteComponent
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
name|util
operator|.
name|EndpointHelper
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
name|apache
operator|.
name|ignite
operator|.
name|configuration
operator|.
name|CollectionConfiguration
import|;
end_import

begin_comment
comment|/**  * The Ignite Sets endpoint is one of camel-ignite endpoints which allows you to interact with  *<a href="https://apacheignite.readme.io/docs/queue-and-set">Ignite Set data structures</a>.  * This endpoint only supports producers.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.17.0"
argument_list|,
name|scheme
operator|=
literal|"ignite-set"
argument_list|,
name|title
operator|=
literal|"Ignite Sets"
argument_list|,
name|syntax
operator|=
literal|"ignite-set:name"
argument_list|,
name|label
operator|=
literal|"nosql,cache"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|)
DECL|class|IgniteSetEndpoint
specifier|public
class|class
name|IgniteSetEndpoint
extends|extends
name|AbstractIgniteEndpoint
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|configuration
specifier|private
name|CollectionConfiguration
name|configuration
init|=
operator|new
name|CollectionConfiguration
argument_list|()
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|operation
specifier|private
name|IgniteSetOperation
name|operation
decl_stmt|;
annotation|@
name|Deprecated
DECL|method|IgniteSetEndpoint (String endpointUri, URI remainingUri, Map<String, Object> parameters, IgniteComponent igniteComponent)
specifier|public
name|IgniteSetEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|URI
name|remainingUri
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|IgniteComponent
name|igniteComponent
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|igniteComponent
argument_list|)
expr_stmt|;
name|name
operator|=
name|remainingUri
operator|.
name|getHost
argument_list|()
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|name
argument_list|,
literal|"Set name"
argument_list|)
expr_stmt|;
comment|// Set the configuration values.
if|if
condition|(
operator|!
name|parameters
operator|.
name|containsKey
argument_list|(
literal|"configuration"
argument_list|)
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|configProps
init|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|parameters
argument_list|,
literal|"config."
argument_list|)
decl_stmt|;
name|EndpointHelper
operator|.
name|setReferenceProperties
argument_list|(
name|this
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|configProps
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|EndpointHelper
operator|.
name|setProperties
argument_list|(
name|this
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|configProps
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|IgniteSetEndpoint (String endpointUri, String remaining, Map<String, Object> parameters, IgniteSetComponent igniteComponent)
specifier|public
name|IgniteSetEndpoint
parameter_list|(
name|String
name|endpointUri
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
parameter_list|,
name|IgniteSetComponent
name|igniteComponent
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|igniteComponent
argument_list|)
expr_stmt|;
name|name
operator|=
name|remaining
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|name
argument_list|,
literal|"Set name"
argument_list|)
expr_stmt|;
comment|// Set the configuration values.
if|if
condition|(
operator|!
name|parameters
operator|.
name|containsKey
argument_list|(
literal|"configuration"
argument_list|)
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|configProps
init|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|parameters
argument_list|,
literal|"config."
argument_list|)
decl_stmt|;
name|EndpointHelper
operator|.
name|setReferenceProperties
argument_list|(
name|this
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|configProps
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|EndpointHelper
operator|.
name|setProperties
argument_list|(
name|this
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|configProps
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
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
name|IgniteSetProducer
argument_list|(
name|this
argument_list|,
name|ignite
argument_list|()
operator|.
name|set
argument_list|(
name|name
argument_list|,
name|configuration
argument_list|)
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
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The Ignite Sets endpoint doesn't support consumers."
argument_list|)
throw|;
block|}
comment|/**      * Gets the set name.      *       * @return      */
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * The set name.      *       * @param name      */
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
comment|/**      * Gets the collection configuration. Default: empty configuration.      *       * @return      */
DECL|method|getConfiguration ()
specifier|public
name|CollectionConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
comment|/**      * The collection configuration. Default: empty configuration.      *<p>      * You can also conveniently set inner properties by using<tt>configuration.xyz=123</tt> options.      *       * @param configuration      */
DECL|method|setConfiguration (CollectionConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|CollectionConfiguration
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
comment|/**      * Gets the set operation to perform.      *       * @return      */
DECL|method|getOperation ()
specifier|public
name|IgniteSetOperation
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
comment|/**      * The operation to invoke on the Ignite Set.      * Superseded by the IgniteConstants.IGNITE_SETS_OPERATION header in the IN message.      * Possible values: CONTAINS, ADD, SIZE, REMOVE, ITERATOR, CLEAR, RETAIN_ALL, ARRAY.The set operation to perform.      *       * @param operation      */
DECL|method|setOperation (IgniteSetOperation operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|IgniteSetOperation
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

