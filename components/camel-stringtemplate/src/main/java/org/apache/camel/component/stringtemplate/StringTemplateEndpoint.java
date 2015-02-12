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
name|component
operator|.
name|ResourceEndpoint
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
name|util
operator|.
name|ExchangeHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|stringtemplate
operator|.
name|v4
operator|.
name|NoIndentWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|stringtemplate
operator|.
name|v4
operator|.
name|ST
import|;
end_import

begin_import
import|import
name|org
operator|.
name|stringtemplate
operator|.
name|v4
operator|.
name|STGroup
import|;
end_import

begin_comment
comment|/**  * @version  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"stringtemplate"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"transformation"
argument_list|)
DECL|class|StringTemplateEndpoint
specifier|public
class|class
name|StringTemplateEndpoint
extends|extends
name|ResourceEndpoint
block|{
annotation|@
name|UriParam
DECL|field|delimiterStart
specifier|private
name|char
name|delimiterStart
init|=
name|STGroup
operator|.
name|defaultGroup
operator|.
name|delimiterStartChar
decl_stmt|;
annotation|@
name|UriParam
DECL|field|delimiterStop
specifier|private
name|char
name|delimiterStop
init|=
name|STGroup
operator|.
name|defaultGroup
operator|.
name|delimiterStopChar
decl_stmt|;
DECL|method|StringTemplateEndpoint ()
specifier|public
name|StringTemplateEndpoint
parameter_list|()
block|{     }
DECL|method|StringTemplateEndpoint (String endpointUri, Component component, String resourceUri)
specifier|public
name|StringTemplateEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|String
name|resourceUri
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
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
DECL|method|getDelimiterStart ()
specifier|public
name|char
name|getDelimiterStart
parameter_list|()
block|{
return|return
name|delimiterStart
return|;
block|}
DECL|method|setDelimiterStart (char delimiterStart)
specifier|public
name|void
name|setDelimiterStart
parameter_list|(
name|char
name|delimiterStart
parameter_list|)
block|{
name|this
operator|.
name|delimiterStart
operator|=
name|delimiterStart
expr_stmt|;
block|}
DECL|method|getDelimiterStop ()
specifier|public
name|char
name|getDelimiterStop
parameter_list|()
block|{
return|return
name|delimiterStop
return|;
block|}
DECL|method|setDelimiterStop (char delimiterStop)
specifier|public
name|void
name|setDelimiterStop
parameter_list|(
name|char
name|delimiterStop
parameter_list|)
block|{
name|this
operator|.
name|delimiterStop
operator|=
name|delimiterStop
expr_stmt|;
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|variableMap
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|StringTemplateConstants
operator|.
name|STRINGTEMPLATE_VARIABLE_MAP
argument_list|,
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|variableMap
operator|==
literal|null
condition|)
block|{
name|variableMap
operator|=
name|ExchangeHelper
operator|.
name|createVariableMap
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|// getResourceAsInputStream also considers the content cache
name|String
name|text
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|getResourceAsInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|ST
name|template
init|=
operator|new
name|ST
argument_list|(
name|text
argument_list|,
name|delimiterStart
argument_list|,
name|delimiterStop
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|variableMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|template
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"StringTemplate is writing using attributes: {}"
argument_list|,
name|variableMap
argument_list|)
expr_stmt|;
name|template
operator|.
name|write
argument_list|(
operator|new
name|NoIndentWriter
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
argument_list|()
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
name|setHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|setHeader
argument_list|(
name|StringTemplateConstants
operator|.
name|STRINGTEMPLATE_RESOURCE_URI
argument_list|,
name|getResourceUri
argument_list|()
argument_list|)
expr_stmt|;
name|out
operator|.
name|setAttachments
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getAttachments
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

