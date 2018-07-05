begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Future
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
name|spi
operator|.
name|ExchangeFormatter
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
name|UriParams
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
name|camel
operator|.
name|util
operator|.
name|StringHelper
import|;
end_import

begin_comment
comment|/**  * Default {@link ExchangeFormatter} that have fine grained options to configure what to include in the output.  */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|DefaultExchangeFormatter
specifier|public
class|class
name|DefaultExchangeFormatter
implements|implements
name|ExchangeFormatter
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
name|lineSeparator
argument_list|()
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
DECL|enum|OutputStyle
DECL|enumConstant|Default
DECL|enumConstant|Tab
DECL|enumConstant|Fixed
specifier|public
enum|enum
name|OutputStyle
block|{
name|Default
block|,
name|Tab
block|,
name|Fixed
block|}
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"formatting"
argument_list|)
DECL|field|showExchangeId
specifier|private
name|boolean
name|showExchangeId
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"formatting"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|showExchangePattern
specifier|private
name|boolean
name|showExchangePattern
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"formatting"
argument_list|)
DECL|field|showProperties
specifier|private
name|boolean
name|showProperties
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"formatting"
argument_list|)
DECL|field|showHeaders
specifier|private
name|boolean
name|showHeaders
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"formatting"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|skipBodyLineSeparator
specifier|private
name|boolean
name|skipBodyLineSeparator
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"formatting"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|,
name|description
operator|=
literal|"Show the message body."
argument_list|)
DECL|field|showBody
specifier|private
name|boolean
name|showBody
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"formatting"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|showBodyType
specifier|private
name|boolean
name|showBodyType
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"formatting"
argument_list|)
DECL|field|showOut
specifier|private
name|boolean
name|showOut
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"formatting"
argument_list|)
DECL|field|showException
specifier|private
name|boolean
name|showException
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"formatting"
argument_list|)
DECL|field|showCaughtException
specifier|private
name|boolean
name|showCaughtException
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"formatting"
argument_list|)
DECL|field|showStackTrace
specifier|private
name|boolean
name|showStackTrace
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"formatting"
argument_list|)
DECL|field|showAll
specifier|private
name|boolean
name|showAll
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"formatting"
argument_list|)
DECL|field|multiline
specifier|private
name|boolean
name|multiline
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"formatting"
argument_list|)
DECL|field|showFuture
specifier|private
name|boolean
name|showFuture
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"formatting"
argument_list|)
DECL|field|showStreams
specifier|private
name|boolean
name|showStreams
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"formatting"
argument_list|)
DECL|field|showFiles
specifier|private
name|boolean
name|showFiles
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"formatting"
argument_list|,
name|defaultValue
operator|=
literal|"10000"
argument_list|)
DECL|field|maxChars
specifier|private
name|int
name|maxChars
init|=
literal|10000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"formatting"
argument_list|,
name|enums
operator|=
literal|"Default,Tab,Fixed"
argument_list|,
name|defaultValue
operator|=
literal|"Default"
argument_list|)
DECL|field|style
specifier|private
name|OutputStyle
name|style
init|=
name|OutputStyle
operator|.
name|Default
decl_stmt|;
DECL|method|style (String label)
specifier|private
name|String
name|style
parameter_list|(
name|String
name|label
parameter_list|)
block|{
if|if
condition|(
name|style
operator|==
name|OutputStyle
operator|.
name|Default
condition|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|", %s: "
argument_list|,
name|label
argument_list|)
return|;
block|}
if|if
condition|(
name|style
operator|==
name|OutputStyle
operator|.
name|Tab
condition|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"\t%s: "
argument_list|,
name|label
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"\t%-20s"
argument_list|,
name|label
argument_list|)
return|;
block|}
block|}
DECL|method|format (Exchange exchange)
specifier|public
name|String
name|format
parameter_list|(
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
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|showAll
operator|||
name|showExchangeId
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
name|style
argument_list|(
literal|"Id"
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|showAll
operator|||
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
name|style
argument_list|(
literal|"ExchangePattern"
argument_list|)
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
if|if
condition|(
name|showAll
operator|||
name|showProperties
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
name|style
argument_list|(
literal|"Properties"
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|sortMap
argument_list|(
name|filterHeaderAndProperties
argument_list|(
name|exchange
operator|.
name|getProperties
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|showAll
operator|||
name|showHeaders
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
name|style
argument_list|(
literal|"Headers"
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|sortMap
argument_list|(
name|filterHeaderAndProperties
argument_list|(
name|in
operator|.
name|getHeaders
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|showAll
operator|||
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
name|style
argument_list|(
literal|"BodyType"
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|getBodyTypeAsString
argument_list|(
name|in
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|showAll
operator|||
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
name|String
name|body
init|=
name|getBodyAsString
argument_list|(
name|in
argument_list|)
decl_stmt|;
if|if
condition|(
name|skipBodyLineSeparator
condition|)
block|{
name|body
operator|=
name|StringHelper
operator|.
name|replaceAll
argument_list|(
name|body
argument_list|,
name|LS
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|style
argument_list|(
literal|"Body"
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|showAll
operator|||
name|showException
operator|||
name|showCaughtException
condition|)
block|{
comment|// try exception on exchange first
name|Exception
name|exception
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
name|boolean
name|caught
init|=
literal|false
decl_stmt|;
if|if
condition|(
operator|(
name|showAll
operator|||
name|showCaughtException
operator|)
operator|&&
name|exception
operator|==
literal|null
condition|)
block|{
comment|// fallback to caught exception
name|exception
operator|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
name|Exception
operator|.
name|class
argument_list|)
expr_stmt|;
name|caught
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|exception
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
if|if
condition|(
name|caught
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|style
argument_list|(
literal|"CaughtExceptionType"
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|exception
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|style
argument_list|(
literal|"CaughtExceptionMessage"
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|exception
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
name|style
argument_list|(
literal|"ExceptionType"
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|exception
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|style
argument_list|(
literal|"ExceptionMessage"
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|exception
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|showAll
operator|||
name|showStackTrace
condition|)
block|{
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|exception
operator|.
name|printStackTrace
argument_list|(
operator|new
name|PrintWriter
argument_list|(
name|sw
argument_list|)
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|style
argument_list|(
literal|"StackTrace"
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|sw
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|showAll
operator|||
name|showOut
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
if|if
condition|(
name|showAll
operator|||
name|showHeaders
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
name|style
argument_list|(
literal|"OutHeaders"
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|sortMap
argument_list|(
name|filterHeaderAndProperties
argument_list|(
name|out
operator|.
name|getHeaders
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|showAll
operator|||
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
name|style
argument_list|(
literal|"OutBodyType"
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|getBodyTypeAsString
argument_list|(
name|out
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|showAll
operator|||
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
name|String
name|body
init|=
name|getBodyAsString
argument_list|(
name|out
argument_list|)
decl_stmt|;
if|if
condition|(
name|skipBodyLineSeparator
condition|)
block|{
name|body
operator|=
name|StringHelper
operator|.
name|replaceAll
argument_list|(
name|body
argument_list|,
name|LS
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|style
argument_list|(
literal|"OutBody"
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
block|}
else|else
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
name|style
argument_list|(
literal|"Out: null"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
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
if|if
condition|(
name|multiline
condition|)
block|{
name|sb
operator|.
name|insert
argument_list|(
literal|0
argument_list|,
literal|"Exchange["
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
else|else
block|{
comment|// get rid of the leading space comma if needed
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|&&
name|sb
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|==
literal|','
operator|&&
name|sb
operator|.
name|charAt
argument_list|(
literal|1
argument_list|)
operator|==
literal|' '
condition|)
block|{
name|sb
operator|.
name|replace
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|insert
argument_list|(
literal|0
argument_list|,
literal|"Exchange["
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
comment|/**      * Filters the headers or properties before formatting them. No default behavior, but can be overridden.      * @param map      * @return      */
DECL|method|filterHeaderAndProperties (Map<String, Object> map)
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|filterHeaderAndProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
block|{
return|return
name|map
return|;
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
comment|/**      * Show the unique exchange ID.      */
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
comment|/**      * Show the exchange properties.      */
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
comment|/**      * Show the message headers.      */
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
DECL|method|isSkipBodyLineSeparator ()
specifier|public
name|boolean
name|isSkipBodyLineSeparator
parameter_list|()
block|{
return|return
name|skipBodyLineSeparator
return|;
block|}
comment|/**      * Whether to skip line separators when logging the message body.      * This allows to log the message body in one line, setting this option to false will preserve any line separators      * from the body, which then will log the body as is.      */
DECL|method|setSkipBodyLineSeparator (boolean skipBodyLineSeparator)
specifier|public
name|void
name|setSkipBodyLineSeparator
parameter_list|(
name|boolean
name|skipBodyLineSeparator
parameter_list|)
block|{
name|this
operator|.
name|skipBodyLineSeparator
operator|=
name|skipBodyLineSeparator
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
comment|/**      * Show the body Java type.      */
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
comment|/*      * Show the message body.      */
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
DECL|method|isShowOut ()
specifier|public
name|boolean
name|isShowOut
parameter_list|()
block|{
return|return
name|showOut
return|;
block|}
comment|/**      * If the exchange has an out message, show the out message.      */
DECL|method|setShowOut (boolean showOut)
specifier|public
name|void
name|setShowOut
parameter_list|(
name|boolean
name|showOut
parameter_list|)
block|{
name|this
operator|.
name|showOut
operator|=
name|showOut
expr_stmt|;
block|}
DECL|method|isShowAll ()
specifier|public
name|boolean
name|isShowAll
parameter_list|()
block|{
return|return
name|showAll
return|;
block|}
comment|/**      * Quick option for turning all options on. (multiline, maxChars has to be manually set if to be used)      */
DECL|method|setShowAll (boolean showAll)
specifier|public
name|void
name|setShowAll
parameter_list|(
name|boolean
name|showAll
parameter_list|)
block|{
name|this
operator|.
name|showAll
operator|=
name|showAll
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
comment|/**      * If the exchange has an exception, show the exception message (no stacktrace)      */
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
DECL|method|isShowStackTrace ()
specifier|public
name|boolean
name|isShowStackTrace
parameter_list|()
block|{
return|return
name|showStackTrace
return|;
block|}
comment|/**      * Show the stack trace, if an exchange has an exception. Only effective if one of showAll, showException or showCaughtException are enabled.      */
DECL|method|setShowStackTrace (boolean showStackTrace)
specifier|public
name|void
name|setShowStackTrace
parameter_list|(
name|boolean
name|showStackTrace
parameter_list|)
block|{
name|this
operator|.
name|showStackTrace
operator|=
name|showStackTrace
expr_stmt|;
block|}
DECL|method|isShowCaughtException ()
specifier|public
name|boolean
name|isShowCaughtException
parameter_list|()
block|{
return|return
name|showCaughtException
return|;
block|}
comment|/**      * f the exchange has a caught exception, show the exception message (no stack trace).      * A caught exception is stored as a property on the exchange (using the key {@link org.apache.camel.Exchange#EXCEPTION_CAUGHT}      * and for instance a doCatch can catch exceptions.      */
DECL|method|setShowCaughtException (boolean showCaughtException)
specifier|public
name|void
name|setShowCaughtException
parameter_list|(
name|boolean
name|showCaughtException
parameter_list|)
block|{
name|this
operator|.
name|showCaughtException
operator|=
name|showCaughtException
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
comment|/**      * Limits the number of characters logged per line.      */
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
comment|/**      * If enabled then each information is outputted on a newline.      */
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
DECL|method|isShowFuture ()
specifier|public
name|boolean
name|isShowFuture
parameter_list|()
block|{
return|return
name|showFuture
return|;
block|}
comment|/**      * If enabled Camel will on Future objects wait for it to complete to obtain the payload to be logged.      */
DECL|method|setShowFuture (boolean showFuture)
specifier|public
name|void
name|setShowFuture
parameter_list|(
name|boolean
name|showFuture
parameter_list|)
block|{
name|this
operator|.
name|showFuture
operator|=
name|showFuture
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
comment|/**      * Shows the Message Exchange Pattern (or MEP for short).      */
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
DECL|method|isShowStreams ()
specifier|public
name|boolean
name|isShowStreams
parameter_list|()
block|{
return|return
name|showStreams
return|;
block|}
comment|/**      * Whether Camel should show stream bodies or not (eg such as java.io.InputStream).      * Beware if you enable this option then you may not be able later to access the message body      * as the stream have already been read by this logger.      * To remedy this you will have to use Stream Caching.      */
DECL|method|setShowStreams (boolean showStreams)
specifier|public
name|void
name|setShowStreams
parameter_list|(
name|boolean
name|showStreams
parameter_list|)
block|{
name|this
operator|.
name|showStreams
operator|=
name|showStreams
expr_stmt|;
block|}
DECL|method|isShowFiles ()
specifier|public
name|boolean
name|isShowFiles
parameter_list|()
block|{
return|return
name|showFiles
return|;
block|}
comment|/**      * If enabled Camel will output files      */
DECL|method|setShowFiles (boolean showFiles)
specifier|public
name|void
name|setShowFiles
parameter_list|(
name|boolean
name|showFiles
parameter_list|)
block|{
name|this
operator|.
name|showFiles
operator|=
name|showFiles
expr_stmt|;
block|}
DECL|method|getStyle ()
specifier|public
name|OutputStyle
name|getStyle
parameter_list|()
block|{
return|return
name|style
return|;
block|}
comment|/**      * Sets the outputs style to use.      */
DECL|method|setStyle (OutputStyle style)
specifier|public
name|void
name|setStyle
parameter_list|(
name|OutputStyle
name|style
parameter_list|)
block|{
name|this
operator|.
name|style
operator|=
name|style
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|getBodyAsString (Message message)
specifier|protected
name|String
name|getBodyAsString
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|.
name|getBody
argument_list|()
operator|instanceof
name|Future
condition|)
block|{
if|if
condition|(
operator|!
name|isShowFuture
argument_list|()
condition|)
block|{
comment|// just use a to string of the future object
return|return
name|message
operator|.
name|getBody
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
return|return
name|MessageHelper
operator|.
name|extractBodyForLogging
argument_list|(
name|message
argument_list|,
literal|""
argument_list|,
name|isShowStreams
argument_list|()
argument_list|,
name|isShowFiles
argument_list|()
argument_list|,
name|getMaxChars
argument_list|(
name|message
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getMaxChars (Message message)
specifier|private
name|int
name|getMaxChars
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|int
name|maxChars
init|=
name|getMaxChars
argument_list|()
decl_stmt|;
if|if
condition|(
name|message
operator|.
name|getExchange
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|globalOption
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getContext
argument_list|()
operator|.
name|getGlobalOption
argument_list|(
name|Exchange
operator|.
name|LOG_DEBUG_BODY_MAX_CHARS
argument_list|)
decl_stmt|;
if|if
condition|(
name|globalOption
operator|!=
literal|null
condition|)
block|{
name|maxChars
operator|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
name|globalOption
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|maxChars
return|;
block|}
DECL|method|getBodyTypeAsString (Message message)
specifier|protected
name|String
name|getBodyTypeAsString
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|answer
init|=
name|ObjectHelper
operator|.
name|classCanonicalName
argument_list|(
name|message
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
operator|&&
name|answer
operator|.
name|startsWith
argument_list|(
literal|"java.lang."
argument_list|)
condition|)
block|{
return|return
name|answer
operator|.
name|substring
argument_list|(
literal|10
argument_list|)
return|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|sortMap (Map<String, Object> map)
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|sortMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|answer
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|(
name|String
operator|.
name|CASE_INSENSITIVE_ORDER
argument_list|)
decl_stmt|;
name|answer
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

