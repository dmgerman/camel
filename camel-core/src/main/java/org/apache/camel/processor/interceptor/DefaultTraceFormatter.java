begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.interceptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|interceptor
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
name|RouteNode
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
name|ProcessorDefinition
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
name|ProcessorDefinitionHelper
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
name|RouteDefinition
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
name|TracedRouteNodes
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
name|MessageHelper
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|DefaultTraceFormatter
specifier|public
class|class
name|DefaultTraceFormatter
implements|implements
name|TraceFormatter
block|{
DECL|field|LS
specifier|protected
specifier|static
specifier|final
name|String
name|LS
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
decl_stmt|;
DECL|field|SEPARATOR
specifier|private
specifier|static
specifier|final
name|String
name|SEPARATOR
init|=
literal|"###REPLACE_ME###"
decl_stmt|;
DECL|field|breadCrumbLength
specifier|private
name|int
name|breadCrumbLength
decl_stmt|;
DECL|field|nodeLength
specifier|private
name|int
name|nodeLength
decl_stmt|;
DECL|field|showBreadCrumb
specifier|private
name|boolean
name|showBreadCrumb
init|=
literal|true
decl_stmt|;
DECL|field|showNode
specifier|private
name|boolean
name|showNode
init|=
literal|true
decl_stmt|;
DECL|field|showExchangeId
specifier|private
name|boolean
name|showExchangeId
decl_stmt|;
DECL|field|showShortExchangeId
specifier|private
name|boolean
name|showShortExchangeId
decl_stmt|;
DECL|field|showExchangePattern
specifier|private
name|boolean
name|showExchangePattern
init|=
literal|true
decl_stmt|;
DECL|field|showProperties
specifier|private
name|boolean
name|showProperties
decl_stmt|;
DECL|field|showHeaders
specifier|private
name|boolean
name|showHeaders
init|=
literal|true
decl_stmt|;
DECL|field|showBody
specifier|private
name|boolean
name|showBody
init|=
literal|true
decl_stmt|;
DECL|field|showBodyType
specifier|private
name|boolean
name|showBodyType
init|=
literal|true
decl_stmt|;
DECL|field|showOutHeaders
specifier|private
name|boolean
name|showOutHeaders
decl_stmt|;
DECL|field|showOutBody
specifier|private
name|boolean
name|showOutBody
decl_stmt|;
DECL|field|showOutBodyType
specifier|private
name|boolean
name|showOutBodyType
decl_stmt|;
DECL|field|showException
specifier|private
name|boolean
name|showException
init|=
literal|true
decl_stmt|;
DECL|field|showRouteId
specifier|private
name|boolean
name|showRouteId
init|=
literal|true
decl_stmt|;
DECL|field|multiline
specifier|private
name|boolean
name|multiline
decl_stmt|;
DECL|field|maxChars
specifier|private
name|int
name|maxChars
init|=
literal|10000
decl_stmt|;
DECL|method|format (final TraceInterceptor interceptor, final ProcessorDefinition<?> node, final Exchange exchange)
specifier|public
name|Object
name|format
parameter_list|(
specifier|final
name|TraceInterceptor
name|interceptor
parameter_list|,
specifier|final
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|node
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Message
name|out
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|out
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
expr_stmt|;
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|multiline
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|SEPARATOR
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|extractBreadCrumb
argument_list|(
name|interceptor
argument_list|,
name|node
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|showExchangePattern
condition|)
block|{
if|if
condition|(
name|multiline
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|SEPARATOR
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|", Pattern:"
argument_list|)
operator|.
name|append
argument_list|(
name|exchange
operator|.
name|getPattern
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// only show properties if we have any
if|if
condition|(
name|showProperties
operator|&&
operator|!
name|exchange
operator|.
name|getProperties
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|multiline
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|SEPARATOR
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|", Properties:"
argument_list|)
operator|.
name|append
argument_list|(
name|exchange
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// only show headers if we have any
if|if
condition|(
name|showHeaders
operator|&&
operator|!
name|in
operator|.
name|getHeaders
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|multiline
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|SEPARATOR
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|", Headers:"
argument_list|)
operator|.
name|append
argument_list|(
name|in
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|showBodyType
condition|)
block|{
if|if
condition|(
name|multiline
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|SEPARATOR
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|", BodyType:"
argument_list|)
operator|.
name|append
argument_list|(
name|MessageHelper
operator|.
name|getBodyTypeName
argument_list|(
name|in
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|showBody
condition|)
block|{
if|if
condition|(
name|multiline
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|SEPARATOR
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|", Body:"
argument_list|)
operator|.
name|append
argument_list|(
name|MessageHelper
operator|.
name|extractBodyForLogging
argument_list|(
name|in
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|showOutHeaders
operator|&&
name|out
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|multiline
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|SEPARATOR
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|", OutHeaders:"
argument_list|)
operator|.
name|append
argument_list|(
name|out
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|showOutBodyType
operator|&&
name|out
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|multiline
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|SEPARATOR
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|", OutBodyType:"
argument_list|)
operator|.
name|append
argument_list|(
name|MessageHelper
operator|.
name|getBodyTypeName
argument_list|(
name|out
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|showOutBody
operator|&&
name|out
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|multiline
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|SEPARATOR
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|", OutBody:"
argument_list|)
operator|.
name|append
argument_list|(
name|MessageHelper
operator|.
name|extractBodyForLogging
argument_list|(
name|out
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|showException
operator|&&
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|multiline
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|SEPARATOR
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|", Exception:"
argument_list|)
operator|.
name|append
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// replace ugly<<<, with<<<
name|sb
operator|=
operator|new
name|StringBuilder
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
operator|.
name|replaceFirst
argument_list|(
literal|"<<<,"
argument_list|,
literal|"<<<"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|maxChars
operator|>
literal|0
condition|)
block|{
name|StringBuilder
name|answer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|sb
operator|.
name|toString
argument_list|()
operator|.
name|split
argument_list|(
name|SEPARATOR
argument_list|)
control|)
block|{
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|s
operator|.
name|length
argument_list|()
operator|>
name|maxChars
condition|)
block|{
name|s
operator|=
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|maxChars
argument_list|)
expr_stmt|;
name|answer
operator|.
name|append
argument_list|(
name|s
argument_list|)
operator|.
name|append
argument_list|(
literal|"..."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|multiline
condition|)
block|{
name|answer
operator|.
name|append
argument_list|(
name|LS
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// switch string buffer
name|sb
operator|=
name|answer
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|isShowBody ()
specifier|public
name|boolean
name|isShowBody
parameter_list|()
block|{
return|return
name|showBody
return|;
block|}
DECL|method|setShowBody (boolean showBody)
specifier|public
name|void
name|setShowBody
parameter_list|(
name|boolean
name|showBody
parameter_list|)
block|{
name|this
operator|.
name|showBody
operator|=
name|showBody
expr_stmt|;
block|}
DECL|method|isShowBodyType ()
specifier|public
name|boolean
name|isShowBodyType
parameter_list|()
block|{
return|return
name|showBodyType
return|;
block|}
DECL|method|setShowBodyType (boolean showBodyType)
specifier|public
name|void
name|setShowBodyType
parameter_list|(
name|boolean
name|showBodyType
parameter_list|)
block|{
name|this
operator|.
name|showBodyType
operator|=
name|showBodyType
expr_stmt|;
block|}
DECL|method|setShowOutBody (boolean showOutBody)
specifier|public
name|void
name|setShowOutBody
parameter_list|(
name|boolean
name|showOutBody
parameter_list|)
block|{
name|this
operator|.
name|showOutBody
operator|=
name|showOutBody
expr_stmt|;
block|}
DECL|method|isShowOutBody ()
specifier|public
name|boolean
name|isShowOutBody
parameter_list|()
block|{
return|return
name|showOutBody
return|;
block|}
DECL|method|setShowOutBodyType (boolean showOutBodyType)
specifier|public
name|void
name|setShowOutBodyType
parameter_list|(
name|boolean
name|showOutBodyType
parameter_list|)
block|{
name|this
operator|.
name|showOutBodyType
operator|=
name|showOutBodyType
expr_stmt|;
block|}
DECL|method|isShowOutBodyType ()
specifier|public
name|boolean
name|isShowOutBodyType
parameter_list|()
block|{
return|return
name|showOutBodyType
return|;
block|}
DECL|method|isShowBreadCrumb ()
specifier|public
name|boolean
name|isShowBreadCrumb
parameter_list|()
block|{
return|return
name|showBreadCrumb
return|;
block|}
DECL|method|setShowBreadCrumb (boolean showBreadCrumb)
specifier|public
name|void
name|setShowBreadCrumb
parameter_list|(
name|boolean
name|showBreadCrumb
parameter_list|)
block|{
name|this
operator|.
name|showBreadCrumb
operator|=
name|showBreadCrumb
expr_stmt|;
block|}
DECL|method|isShowExchangeId ()
specifier|public
name|boolean
name|isShowExchangeId
parameter_list|()
block|{
return|return
name|showExchangeId
return|;
block|}
DECL|method|setShowExchangeId (boolean showExchangeId)
specifier|public
name|void
name|setShowExchangeId
parameter_list|(
name|boolean
name|showExchangeId
parameter_list|)
block|{
name|this
operator|.
name|showExchangeId
operator|=
name|showExchangeId
expr_stmt|;
block|}
DECL|method|isShowHeaders ()
specifier|public
name|boolean
name|isShowHeaders
parameter_list|()
block|{
return|return
name|showHeaders
return|;
block|}
DECL|method|setShowHeaders (boolean showHeaders)
specifier|public
name|void
name|setShowHeaders
parameter_list|(
name|boolean
name|showHeaders
parameter_list|)
block|{
name|this
operator|.
name|showHeaders
operator|=
name|showHeaders
expr_stmt|;
block|}
DECL|method|isShowOutHeaders ()
specifier|public
name|boolean
name|isShowOutHeaders
parameter_list|()
block|{
return|return
name|showOutHeaders
return|;
block|}
DECL|method|setShowOutHeaders (boolean showOutHeaders)
specifier|public
name|void
name|setShowOutHeaders
parameter_list|(
name|boolean
name|showOutHeaders
parameter_list|)
block|{
name|this
operator|.
name|showOutHeaders
operator|=
name|showOutHeaders
expr_stmt|;
block|}
DECL|method|isShowProperties ()
specifier|public
name|boolean
name|isShowProperties
parameter_list|()
block|{
return|return
name|showProperties
return|;
block|}
DECL|method|setShowProperties (boolean showProperties)
specifier|public
name|void
name|setShowProperties
parameter_list|(
name|boolean
name|showProperties
parameter_list|)
block|{
name|this
operator|.
name|showProperties
operator|=
name|showProperties
expr_stmt|;
block|}
DECL|method|isShowNode ()
specifier|public
name|boolean
name|isShowNode
parameter_list|()
block|{
return|return
name|showNode
return|;
block|}
DECL|method|setShowNode (boolean showNode)
specifier|public
name|void
name|setShowNode
parameter_list|(
name|boolean
name|showNode
parameter_list|)
block|{
name|this
operator|.
name|showNode
operator|=
name|showNode
expr_stmt|;
block|}
DECL|method|isShowExchangePattern ()
specifier|public
name|boolean
name|isShowExchangePattern
parameter_list|()
block|{
return|return
name|showExchangePattern
return|;
block|}
DECL|method|setShowExchangePattern (boolean showExchangePattern)
specifier|public
name|void
name|setShowExchangePattern
parameter_list|(
name|boolean
name|showExchangePattern
parameter_list|)
block|{
name|this
operator|.
name|showExchangePattern
operator|=
name|showExchangePattern
expr_stmt|;
block|}
DECL|method|isShowException ()
specifier|public
name|boolean
name|isShowException
parameter_list|()
block|{
return|return
name|showException
return|;
block|}
DECL|method|setShowException (boolean showException)
specifier|public
name|void
name|setShowException
parameter_list|(
name|boolean
name|showException
parameter_list|)
block|{
name|this
operator|.
name|showException
operator|=
name|showException
expr_stmt|;
block|}
DECL|method|isShowRouteId ()
specifier|public
name|boolean
name|isShowRouteId
parameter_list|()
block|{
return|return
name|showRouteId
return|;
block|}
DECL|method|setShowRouteId (boolean showRouteId)
specifier|public
name|void
name|setShowRouteId
parameter_list|(
name|boolean
name|showRouteId
parameter_list|)
block|{
name|this
operator|.
name|showRouteId
operator|=
name|showRouteId
expr_stmt|;
block|}
DECL|method|isMultiline ()
specifier|public
name|boolean
name|isMultiline
parameter_list|()
block|{
return|return
name|multiline
return|;
block|}
DECL|method|setMultiline (boolean multiline)
specifier|public
name|void
name|setMultiline
parameter_list|(
name|boolean
name|multiline
parameter_list|)
block|{
name|this
operator|.
name|multiline
operator|=
name|multiline
expr_stmt|;
block|}
DECL|method|getBreadCrumbLength ()
specifier|public
name|int
name|getBreadCrumbLength
parameter_list|()
block|{
return|return
name|breadCrumbLength
return|;
block|}
DECL|method|setBreadCrumbLength (int breadCrumbLength)
specifier|public
name|void
name|setBreadCrumbLength
parameter_list|(
name|int
name|breadCrumbLength
parameter_list|)
block|{
name|this
operator|.
name|breadCrumbLength
operator|=
name|breadCrumbLength
expr_stmt|;
block|}
DECL|method|isShowShortExchangeId ()
specifier|public
name|boolean
name|isShowShortExchangeId
parameter_list|()
block|{
return|return
name|showShortExchangeId
return|;
block|}
DECL|method|setShowShortExchangeId (boolean showShortExchangeId)
specifier|public
name|void
name|setShowShortExchangeId
parameter_list|(
name|boolean
name|showShortExchangeId
parameter_list|)
block|{
name|this
operator|.
name|showShortExchangeId
operator|=
name|showShortExchangeId
expr_stmt|;
block|}
DECL|method|getNodeLength ()
specifier|public
name|int
name|getNodeLength
parameter_list|()
block|{
return|return
name|nodeLength
return|;
block|}
DECL|method|setNodeLength (int nodeLength)
specifier|public
name|void
name|setNodeLength
parameter_list|(
name|int
name|nodeLength
parameter_list|)
block|{
name|this
operator|.
name|nodeLength
operator|=
name|nodeLength
expr_stmt|;
block|}
DECL|method|getMaxChars ()
specifier|public
name|int
name|getMaxChars
parameter_list|()
block|{
return|return
name|maxChars
return|;
block|}
DECL|method|setMaxChars (int maxChars)
specifier|public
name|void
name|setMaxChars
parameter_list|(
name|int
name|maxChars
parameter_list|)
block|{
name|this
operator|.
name|maxChars
operator|=
name|maxChars
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|extractRoute (ProcessorDefinition<?> node)
specifier|protected
name|String
name|extractRoute
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|node
parameter_list|)
block|{
name|RouteDefinition
name|route
init|=
name|ProcessorDefinitionHelper
operator|.
name|getRoute
argument_list|(
name|node
argument_list|)
decl_stmt|;
if|if
condition|(
name|route
operator|!=
literal|null
condition|)
block|{
return|return
name|route
operator|.
name|getId
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|getBreadCrumbID (Exchange exchange)
specifier|protected
name|Object
name|getBreadCrumbID
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getExchangeId
argument_list|()
return|;
block|}
DECL|method|getNodeMessage (RouteNode entry, Exchange exchange)
specifier|protected
name|String
name|getNodeMessage
parameter_list|(
name|RouteNode
name|entry
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|message
init|=
name|entry
operator|.
name|getLabel
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|nodeLength
operator|>
literal|0
condition|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%1$-"
operator|+
name|nodeLength
operator|+
literal|"."
operator|+
name|nodeLength
operator|+
literal|"s"
argument_list|,
name|message
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|message
return|;
block|}
block|}
comment|/**      * Creates the breadcrumb based on whether this was a trace of      * an exchange coming out of or into a processing step. For example,       *<br/><tt>transform(body) -> ID-mojo/39713-1225468755256/2-0</tt>      *<br/>or      *<br/><tt>ID-mojo/39713-1225468755256/2-0 -> transform(body)</tt>      */
DECL|method|extractBreadCrumb (TraceInterceptor interceptor, ProcessorDefinition<?> currentNode, Exchange exchange)
specifier|protected
name|String
name|extractBreadCrumb
parameter_list|(
name|TraceInterceptor
name|interceptor
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|currentNode
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|id
init|=
literal|""
decl_stmt|;
name|String
name|result
decl_stmt|;
if|if
condition|(
operator|!
name|showBreadCrumb
operator|&&
operator|!
name|showExchangeId
operator|&&
operator|!
name|showShortExchangeId
operator|&&
operator|!
name|showNode
condition|)
block|{
return|return
literal|""
return|;
block|}
comment|// compute breadcrumb id
if|if
condition|(
name|showBreadCrumb
condition|)
block|{
name|id
operator|=
name|getBreadCrumbID
argument_list|(
name|exchange
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|showExchangeId
operator|||
name|showShortExchangeId
condition|)
block|{
name|id
operator|=
name|getBreadCrumbID
argument_list|(
name|exchange
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
if|if
condition|(
name|showShortExchangeId
condition|)
block|{
comment|// only output last part of id
name|id
operator|=
name|id
operator|.
name|substring
argument_list|(
name|id
operator|.
name|lastIndexOf
argument_list|(
literal|'-'
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
comment|// compute from, to and route
name|String
name|from
init|=
literal|""
decl_stmt|;
name|String
name|to
init|=
literal|""
decl_stmt|;
name|String
name|route
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|showNode
operator|||
name|showRouteId
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|TracedRouteNodes
name|traced
init|=
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|getTracedRouteNodes
argument_list|()
decl_stmt|;
name|RouteNode
name|traceFrom
init|=
name|traced
operator|.
name|getSecondLastNode
argument_list|()
decl_stmt|;
if|if
condition|(
name|traceFrom
operator|!=
literal|null
condition|)
block|{
name|from
operator|=
name|getNodeMessage
argument_list|(
name|traceFrom
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|exchange
operator|.
name|getFromEndpoint
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|from
operator|=
literal|"from("
operator|+
name|exchange
operator|.
name|getFromEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
operator|+
literal|")"
expr_stmt|;
block|}
name|RouteNode
name|traceTo
init|=
name|traced
operator|.
name|getLastNode
argument_list|()
decl_stmt|;
if|if
condition|(
name|traceTo
operator|!=
literal|null
condition|)
block|{
name|to
operator|=
name|getNodeMessage
argument_list|(
name|traceTo
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
comment|// if its an abstract dummy holder then we have to get the 2nd last so we can get the real node that has
comment|// information which route it belongs to
if|if
condition|(
name|traceTo
operator|.
name|isAbstract
argument_list|()
operator|&&
name|traceTo
operator|.
name|getProcessorDefinition
argument_list|()
operator|==
literal|null
condition|)
block|{
name|traceTo
operator|=
name|traced
operator|.
name|getSecondLastNode
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|traceTo
operator|!=
literal|null
condition|)
block|{
name|route
operator|=
name|extractRoute
argument_list|(
name|traceTo
operator|.
name|getProcessorDefinition
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// assemble result with and without the to/from
if|if
condition|(
name|showNode
condition|)
block|{
if|if
condition|(
name|showRouteId
operator|&&
name|route
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
name|id
operator|.
name|trim
argument_list|()
operator|+
literal|">>> ("
operator|+
name|route
operator|+
literal|") "
operator|+
name|from
operator|+
literal|" --> "
operator|+
name|to
operator|.
name|trim
argument_list|()
operator|+
literal|"<<< "
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|id
operator|.
name|trim
argument_list|()
operator|+
literal|">>> "
operator|+
name|from
operator|+
literal|" --> "
operator|+
name|to
operator|.
name|trim
argument_list|()
operator|+
literal|"<<< "
expr_stmt|;
block|}
if|if
condition|(
name|interceptor
operator|.
name|shouldTraceOutExchanges
argument_list|()
operator|&&
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|result
operator|+=
literal|" (OUT) "
expr_stmt|;
block|}
block|}
else|else
block|{
name|result
operator|=
name|id
expr_stmt|;
block|}
if|if
condition|(
name|breadCrumbLength
operator|>
literal|0
condition|)
block|{
comment|// we want to ensure text coming after this is aligned for readability
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%1$-"
operator|+
name|breadCrumbLength
operator|+
literal|"."
operator|+
name|breadCrumbLength
operator|+
literal|"s"
argument_list|,
name|result
operator|.
name|trim
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|result
operator|.
name|trim
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

