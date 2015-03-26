begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.stax
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|stax
package|;
end_package

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|ContentHandler
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
name|ProcessorEndpoint
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

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"stax"
argument_list|,
name|title
operator|=
literal|"StAX"
argument_list|,
name|syntax
operator|=
literal|"stax:contentHandlerClass"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"transformation"
argument_list|)
DECL|class|StAXEndpoint
specifier|public
class|class
name|StAXEndpoint
extends|extends
name|ProcessorEndpoint
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
DECL|field|contentHandlerClass
specifier|private
name|String
name|contentHandlerClass
decl_stmt|;
DECL|method|StAXEndpoint (String endpointUri, CamelContext context)
specifier|public
name|StAXEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|context
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|getContentHandlerClass ()
specifier|public
name|String
name|getContentHandlerClass
parameter_list|()
block|{
return|return
name|contentHandlerClass
return|;
block|}
DECL|method|setContentHandlerClass (String contentHandlerClass)
specifier|public
name|void
name|setContentHandlerClass
parameter_list|(
name|String
name|contentHandlerClass
parameter_list|)
block|{
name|this
operator|.
name|contentHandlerClass
operator|=
name|contentHandlerClass
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|Processor
name|target
decl_stmt|;
if|if
condition|(
name|EndpointHelper
operator|.
name|isReferenceParameter
argument_list|(
name|contentHandlerClass
argument_list|)
condition|)
block|{
name|ContentHandler
name|handler
init|=
name|EndpointHelper
operator|.
name|resolveReferenceParameter
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|contentHandlerClass
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|,
name|ContentHandler
operator|.
name|class
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|target
operator|=
operator|new
name|StAXProcessor
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Class
argument_list|<
name|ContentHandler
argument_list|>
name|clazz
init|=
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|contentHandlerClass
argument_list|,
name|ContentHandler
operator|.
name|class
argument_list|)
decl_stmt|;
name|target
operator|=
operator|new
name|StAXProcessor
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
block|}
name|setProcessor
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

