begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atomix.client
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
operator|.
name|client
package|;
end_package

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|AtomixClient
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
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|AbstractAtomixClientEndpoint
specifier|public
specifier|abstract
class|class
name|AbstractAtomixClientEndpoint
parameter_list|<
name|T
extends|extends
name|AbstractAtomixClientComponent
parameter_list|,
name|C
extends|extends
name|AtomixClientConfiguration
parameter_list|>
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"The distributed resource name"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|resourceName
specifier|private
specifier|final
name|String
name|resourceName
decl_stmt|;
DECL|field|atomix
specifier|private
name|AtomixClient
name|atomix
decl_stmt|;
DECL|method|AbstractAtomixClientEndpoint (String uri, T component, String resourceName)
specifier|protected
name|AbstractAtomixClientEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|T
name|component
parameter_list|,
name|String
name|resourceName
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|resourceName
operator|=
name|resourceName
expr_stmt|;
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Producer not supported"
argument_list|)
throw|;
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
literal|"Consumer not supported"
argument_list|)
throw|;
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
name|atomix
operator|==
literal|null
condition|)
block|{
specifier|final
name|C
name|configuration
init|=
name|getConfiguration
argument_list|()
decl_stmt|;
specifier|final
name|CamelContext
name|context
init|=
name|getCamelContext
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|configuration
argument_list|,
literal|"Configuration"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|context
argument_list|,
literal|"CamelContext"
argument_list|)
expr_stmt|;
name|atomix
operator|=
name|AtomixClientHelper
operator|.
name|createClient
argument_list|(
name|context
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
name|atomix
operator|.
name|connect
argument_list|(
name|configuration
operator|.
name|getNodes
argument_list|()
argument_list|)
operator|.
name|join
argument_list|()
expr_stmt|;
block|}
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|atomix
operator|!=
literal|null
condition|)
block|{
name|atomix
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|// **********************************
comment|// Helpers for implementations
comment|// **********************************
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getAtomixComponent ()
specifier|public
name|T
name|getAtomixComponent
parameter_list|()
block|{
return|return
operator|(
name|T
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
DECL|method|getAtomix ()
specifier|public
name|AtomixClient
name|getAtomix
parameter_list|()
block|{
return|return
name|atomix
return|;
block|}
DECL|method|getResourceName ()
specifier|public
name|String
name|getResourceName
parameter_list|()
block|{
return|return
name|resourceName
return|;
block|}
comment|// **********************************
comment|// Abstract
comment|// **********************************
DECL|method|getConfiguration ()
specifier|public
specifier|abstract
name|C
name|getConfiguration
parameter_list|()
function_decl|;
DECL|method|setConfiguration (C configuration)
specifier|public
specifier|abstract
name|void
name|setConfiguration
parameter_list|(
name|C
name|configuration
parameter_list|)
function_decl|;
block|}
end_class

end_unit

