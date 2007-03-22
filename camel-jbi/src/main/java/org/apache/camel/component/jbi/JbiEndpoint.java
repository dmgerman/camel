begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jbi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jbi
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
name|impl
operator|.
name|DefaultEndpoint
import|;
end_import

begin_comment
comment|/**  * Represents an {@link Endpoint} for interacting with JBI  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|JbiEndpoint
specifier|public
class|class
name|JbiEndpoint
extends|extends
name|DefaultEndpoint
argument_list|<
name|JbiExchange
argument_list|>
block|{
DECL|field|binding
specifier|private
name|JbiBinding
name|binding
decl_stmt|;
DECL|method|JbiEndpoint (String endpointUri, CamelContext container)
specifier|protected
name|JbiEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|CamelContext
name|container
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|container
argument_list|)
expr_stmt|;
block|}
DECL|method|onExchange (JbiExchange exchange)
specifier|public
name|void
name|onExchange
parameter_list|(
name|JbiExchange
name|exchange
parameter_list|)
block|{
comment|// TODO
comment|// lets create a JBI MessageExchange and dispatch into JBI...
block|}
annotation|@
name|Override
DECL|method|doActivate ()
specifier|protected
name|void
name|doActivate
parameter_list|()
block|{
name|super
operator|.
name|doActivate
argument_list|()
expr_stmt|;
name|Processor
argument_list|<
name|JbiExchange
argument_list|>
name|processor
init|=
name|getInboundProcessor
argument_list|()
decl_stmt|;
comment|// lets now wire up the processor to the JBI stuff...
block|}
DECL|method|createExchange ()
specifier|public
name|JbiExchange
name|createExchange
parameter_list|()
block|{
return|return
operator|new
name|JbiExchange
argument_list|(
name|getContext
argument_list|()
argument_list|,
name|getBinding
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getBinding ()
specifier|public
name|JbiBinding
name|getBinding
parameter_list|()
block|{
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
name|binding
operator|=
operator|new
name|JbiBinding
argument_list|()
expr_stmt|;
block|}
return|return
name|binding
return|;
block|}
comment|/**      * Sets the binding on how Camel messages get mapped to JBI      *      * @param binding the new binding to use      */
DECL|method|setBinding (JbiBinding binding)
specifier|public
name|void
name|setBinding
parameter_list|(
name|JbiBinding
name|binding
parameter_list|)
block|{
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
block|}
end_class

end_unit

