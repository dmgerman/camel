begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.debezium
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|debezium
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
name|component
operator|.
name|debezium
operator|.
name|configuration
operator|.
name|MySqlConnectorEmbeddedDebeziumConfiguration
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
name|annotations
operator|.
name|Component
import|;
end_import

begin_class
annotation|@
name|Component
argument_list|(
literal|"debezium-mysql"
argument_list|)
DECL|class|DebeziumMySqlComponent
specifier|public
specifier|final
class|class
name|DebeziumMySqlComponent
extends|extends
name|DebeziumComponent
argument_list|<
name|MySqlConnectorEmbeddedDebeziumConfiguration
argument_list|>
block|{
DECL|field|configuration
specifier|private
name|MySqlConnectorEmbeddedDebeziumConfiguration
name|configuration
decl_stmt|;
DECL|method|DebeziumMySqlComponent ()
specifier|public
name|DebeziumMySqlComponent
parameter_list|()
block|{     }
DECL|method|DebeziumMySqlComponent (final CamelContext context)
specifier|public
name|DebeziumMySqlComponent
parameter_list|(
specifier|final
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
comment|/**      * Allow pre-configured Configurations to be set.      *      * @return {@link MySqlConnectorEmbeddedDebeziumConfiguration}      */
annotation|@
name|Override
DECL|method|getConfiguration ()
specifier|public
name|MySqlConnectorEmbeddedDebeziumConfiguration
name|getConfiguration
parameter_list|()
block|{
if|if
condition|(
name|configuration
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|MySqlConnectorEmbeddedDebeziumConfiguration
argument_list|()
return|;
block|}
return|return
name|configuration
return|;
block|}
annotation|@
name|Override
DECL|method|setConfiguration (MySqlConnectorEmbeddedDebeziumConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|MySqlConnectorEmbeddedDebeziumConfiguration
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
annotation|@
name|Override
DECL|method|initializeDebeziumEndpoint (String uri, MySqlConnectorEmbeddedDebeziumConfiguration configuration)
specifier|protected
name|DebeziumEndpoint
name|initializeDebeziumEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|MySqlConnectorEmbeddedDebeziumConfiguration
name|configuration
parameter_list|)
block|{
return|return
operator|new
name|DebeziumMySqlEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|configuration
argument_list|)
return|;
block|}
block|}
end_class

end_unit

