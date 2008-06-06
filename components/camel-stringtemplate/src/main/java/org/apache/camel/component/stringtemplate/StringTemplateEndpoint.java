begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.stringtemplate
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|stringtemplate
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|antlr
operator|.
name|stringtemplate
operator|.
name|AutoIndentWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|antlr
operator|.
name|stringtemplate
operator|.
name|StringTemplate
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
name|Exchange
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
name|Message
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
name|component
operator|.
name|ResourceBasedEndpoint
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
name|converter
operator|.
name|IOConverter
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
name|ExchangeHelper
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|StringTemplateEndpoint
specifier|public
class|class
name|StringTemplateEndpoint
extends|extends
name|ResourceBasedEndpoint
block|{
DECL|method|StringTemplateEndpoint (String uri, StringTemplateComponent component, String resourceUri, Map parameters)
specifier|public
name|StringTemplateEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|StringTemplateComponent
name|component
parameter_list|,
name|String
name|resourceUri
parameter_list|,
name|Map
name|parameters
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|resourceUri
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|StringTemplateEndpoint (String endpointUri, Processor processor, String resourceUri)
specifier|public
name|StringTemplateEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|String
name|resourceUri
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|processor
argument_list|,
name|resourceUri
argument_list|)
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
DECL|method|getExchangePattern ()
specifier|public
name|ExchangePattern
name|getExchangePattern
parameter_list|()
block|{
return|return
name|ExchangePattern
operator|.
name|InOut
return|;
block|}
annotation|@
name|Override
DECL|method|onExchange (Exchange exchange)
specifier|protected
name|void
name|onExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|StringWriter
name|buffer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|Map
name|variableMap
init|=
name|ExchangeHelper
operator|.
name|createVariableMap
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
comment|// getResourceAsInputStream also considers the content cache
name|String
name|text
init|=
name|IOConverter
operator|.
name|toString
argument_list|(
name|getResourceAsInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|StringTemplate
name|template
init|=
operator|new
name|StringTemplate
argument_list|(
name|text
argument_list|)
decl_stmt|;
name|template
operator|.
name|setAttributes
argument_list|(
name|variableMap
argument_list|)
expr_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"StringTemplate is writing using attributes: "
operator|+
name|variableMap
argument_list|)
expr_stmt|;
block|}
name|template
operator|.
name|write
argument_list|(
operator|new
name|AutoIndentWriter
argument_list|(
name|buffer
argument_list|)
argument_list|)
expr_stmt|;
comment|// now lets output the results to the exchange
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|out
operator|.
name|setBody
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|setHeader
argument_list|(
literal|"org.apache.camel.stringtemplate.resource"
argument_list|,
name|getResource
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

