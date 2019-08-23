begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.reifier.transformer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|reifier
operator|.
name|transformer
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
name|ExchangePattern
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
name|transformer
operator|.
name|ProcessorTransformer
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
name|model
operator|.
name|transformer
operator|.
name|EndpointTransformerDefinition
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
name|model
operator|.
name|transformer
operator|.
name|TransformerDefinition
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
name|processor
operator|.
name|SendProcessor
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
name|Transformer
import|;
end_import

begin_class
DECL|class|EndpointTransformeReifier
specifier|public
class|class
name|EndpointTransformeReifier
extends|extends
name|TransformerReifier
argument_list|<
name|EndpointTransformerDefinition
argument_list|>
block|{
DECL|method|EndpointTransformeReifier (TransformerDefinition definition)
specifier|public
name|EndpointTransformeReifier
parameter_list|(
name|TransformerDefinition
name|definition
parameter_list|)
block|{
name|super
argument_list|(
operator|(
name|EndpointTransformerDefinition
operator|)
name|definition
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doCreateTransformer (CamelContext context)
specifier|protected
name|Transformer
name|doCreateTransformer
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|definition
operator|.
name|getUri
argument_list|()
operator|!=
literal|null
condition|?
name|context
operator|.
name|getEndpoint
argument_list|(
name|definition
operator|.
name|getUri
argument_list|()
argument_list|)
else|:
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|definition
operator|.
name|getRef
argument_list|()
argument_list|,
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|SendProcessor
name|processor
init|=
operator|new
name|SendProcessor
argument_list|(
name|endpoint
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
return|return
operator|new
name|ProcessorTransformer
argument_list|(
name|context
argument_list|)
operator|.
name|setProcessor
argument_list|(
name|processor
argument_list|)
operator|.
name|setModel
argument_list|(
name|definition
operator|.
name|getScheme
argument_list|()
argument_list|)
operator|.
name|setFrom
argument_list|(
name|definition
operator|.
name|getFromType
argument_list|()
argument_list|)
operator|.
name|setTo
argument_list|(
name|definition
operator|.
name|getToType
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

