begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.log
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|log
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
name|StreamCache
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Log formatter to format the logging output.  */
end_comment

begin_class
DECL|class|LogFormatter
specifier|public
class|class
name|LogFormatter
implements|implements
name|ExchangeFormatter
block|{
DECL|field|showExchangeId
specifier|private
name|boolean
name|showExchangeId
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
decl_stmt|;
DECL|field|showBodyType
specifier|private
name|boolean
name|showBodyType
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
DECL|field|showOut
specifier|private
name|boolean
name|showOut
decl_stmt|;
DECL|field|showException
specifier|private
name|boolean
name|showException
decl_stmt|;
DECL|field|showStackTrace
specifier|private
name|boolean
name|showStackTrace
decl_stmt|;
DECL|field|showAll
specifier|private
name|boolean
name|showAll
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
decl_stmt|;
DECL|method|format (Exchange exchange)
specifier|public
name|Object
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
argument_list|(
literal|""
argument_list|)
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
literal|'\n'
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|", Id:"
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
literal|'\n'
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
literal|'\n'
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
literal|'\n'
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
literal|'\n'
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
name|getBodyAsString
argument_list|(
name|in
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
operator|&&
operator|(
name|showAll
operator|||
name|showException
operator|)
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
literal|'\n'
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|", ExceptionType:"
argument_list|)
operator|.
name|append
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
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
literal|", ExceptionMessage:"
argument_list|)
operator|.
name|append
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
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
name|exchange
operator|.
name|getException
argument_list|()
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
literal|", StackTrace:"
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
literal|'\n'
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
literal|'\n'
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
literal|'\n'
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
name|getBodyAsString
argument_list|(
name|out
argument_list|)
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
literal|'\n'
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|", Out: null"
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
literal|"\n"
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
literal|"\n"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// get rid of the leading space comma if needed
return|return
literal|"Exchange["
operator|+
operator|(
name|multiline
condition|?
name|answer
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
operator|.
name|toString
argument_list|()
else|:
name|answer
operator|.
name|toString
argument_list|()
operator|.
name|substring
argument_list|(
literal|2
argument_list|)
operator|+
literal|"]"
operator|)
return|;
block|}
comment|// get rid of the leading space comma if needed
return|return
literal|"Exchange["
operator|+
operator|(
name|multiline
condition|?
name|sb
operator|.
name|append
argument_list|(
literal|']'
argument_list|)
operator|.
name|toString
argument_list|()
else|:
name|sb
operator|.
name|toString
argument_list|()
operator|.
name|substring
argument_list|(
literal|2
argument_list|)
operator|+
literal|"]"
operator|)
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
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|getBodyAsString (Message message)
specifier|protected
name|Object
name|getBodyAsString
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|StreamCache
name|newBody
init|=
name|message
operator|.
name|getBody
argument_list|(
name|StreamCache
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|newBody
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setBody
argument_list|(
name|newBody
argument_list|)
expr_stmt|;
block|}
name|Object
name|answer
init|=
name|message
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|message
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|newBody
operator|!=
literal|null
condition|)
block|{
comment|// Reset the StreamCache
name|newBody
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|getBodyTypeAsString (Message message)
specifier|protected
name|Object
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
block|}
end_class

end_unit

