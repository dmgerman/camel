begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.connector
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|connector
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|Component
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
name|catalog
operator|.
name|CamelCatalog
import|;
end_import

begin_comment
comment|/**  * A component which is based from a Camel Connector.  */
end_comment

begin_interface
DECL|interface|ConnectorComponent
specifier|public
interface|interface
name|ConnectorComponent
extends|extends
name|Component
block|{
comment|/**      * Adds a new option to the connector's options.      *      * @param name     the name of the option      * @param value    the value of the option      */
DECL|method|addOption (String name, Object value)
name|void
name|addOption
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
function_decl|;
comment|/**      * Adds options to the connector's options.      *      * @param options  the options      */
DECL|method|addOptions (Map<String, Object> options)
name|void
name|addOptions
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
function_decl|;
comment|/**      * Creates the endpoint uri based on the options from the connector.      *      * @param scheme  the component name      * @param options the options to use for creating the endpoint      * @return the endpoint uri      * @throws URISyntaxException is thrown if error creating the endpoint uri.      */
DECL|method|createEndpointUri (String scheme, Map<String, String> options)
name|String
name|createEndpointUri
parameter_list|(
name|String
name|scheme
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|options
parameter_list|)
throws|throws
name|URISyntaxException
function_decl|;
comment|/**      * Gets the {@link CamelCatalog} which can be used by the connector to help create the component.      */
DECL|method|getCamelCatalog ()
name|CamelCatalog
name|getCamelCatalog
parameter_list|()
function_decl|;
comment|/**      * Gets the connector name (title)      */
DECL|method|getConnectorName ()
name|String
name|getConnectorName
parameter_list|()
function_decl|;
comment|/**      * Gets the connector component name      */
DECL|method|getComponentName ()
name|String
name|getComponentName
parameter_list|()
function_decl|;
comment|/**      * Gets the connector component scheme      */
DECL|method|getComponentScheme ()
name|String
name|getComponentScheme
parameter_list|()
function_decl|;
comment|/**      * Gets the camel-connector JSon file.      */
DECL|method|getCamelConnectorJSon ()
name|String
name|getCamelConnectorJSon
parameter_list|()
function_decl|;
comment|/**      * A set of additional component/endpoint options to use for the base component when creating connector endpoints.      *      * @deprecated use {@link #getOptions()} instead      */
annotation|@
name|Deprecated
DECL|method|getComponentOptions ()
specifier|default
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getComponentOptions
parameter_list|()
block|{
return|return
name|getOptions
argument_list|()
return|;
block|}
comment|/**      * A set of additional component/endpoint options to use for the base component when creating connector endpoints.      */
DECL|method|getOptions ()
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getOptions
parameter_list|()
function_decl|;
comment|/**      * A set of additional component/endpoint options to use for the base component when creating connector endpoints.      *      * @deprecated use {@link #setOptions(Map)} instead      */
DECL|method|setComponentOptions (Map<String, Object> options)
specifier|default
name|void
name|setComponentOptions
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
block|{
name|setOptions
argument_list|(
name|options
argument_list|)
expr_stmt|;
block|}
comment|/**      * A set of additional component/endpoint options to use for the base component when creating connector endpoints.      */
DECL|method|setOptions (Map<String, Object> options)
name|void
name|setOptions
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
function_decl|;
comment|/**      * To perform custom processing before the producer is sending the message.      */
DECL|method|setBeforeProducer (Processor processor)
name|void
name|setBeforeProducer
parameter_list|(
name|Processor
name|processor
parameter_list|)
function_decl|;
comment|/**      * Gets the processor used to perform custom processing before the producer is sending the message.      */
DECL|method|getBeforeProducer ()
name|Processor
name|getBeforeProducer
parameter_list|()
function_decl|;
comment|/**      * To perform custom processing after the producer has sent the message and received any reply (if InOut).      */
DECL|method|setAfterProducer (Processor processor)
name|void
name|setAfterProducer
parameter_list|(
name|Processor
name|processor
parameter_list|)
function_decl|;
comment|/**      * Gets the processor used to perform custom processing after the producer has sent the message and received any reply (if InOut).      */
DECL|method|getAfterProducer ()
name|Processor
name|getAfterProducer
parameter_list|()
function_decl|;
comment|/**      * To perform custom processing when the consumer has just received a new incoming message.      */
DECL|method|setBeforeConsumer (Processor processor)
name|void
name|setBeforeConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
function_decl|;
comment|/**      * Gets the processor used to perform custom processing when the consumer has just received a new incoming message.      */
DECL|method|getBeforeConsumer ()
name|Processor
name|getBeforeConsumer
parameter_list|()
function_decl|;
comment|/**      * To perform custom processing when the consumer is about to send back a reply message to the caller (if InOut).      */
DECL|method|setAfterConsumer (Processor processor)
name|void
name|setAfterConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
function_decl|;
comment|/**      * Gets the processor used to perform custom processing when the consumer is about to send back a reply message to the caller (if InOut).      */
DECL|method|getAfterConsumer ()
name|Processor
name|getAfterConsumer
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

