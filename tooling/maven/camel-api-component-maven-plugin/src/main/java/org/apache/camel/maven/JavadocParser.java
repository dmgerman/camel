begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLDecoder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|ChangedCharSetException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|SimpleAttributeSet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|html
operator|.
name|HTML
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|html
operator|.
name|parser
operator|.
name|DTD
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|html
operator|.
name|parser
operator|.
name|Parser
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|html
operator|.
name|parser
operator|.
name|TagElement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringEscapeUtils
import|;
end_import

begin_comment
comment|/**  * Parses Javadoc HTML to get Method Signatures from Method Sumary. Supports Java 6, 7 and 8 Javadoc formats.  */
end_comment

begin_class
DECL|class|JavadocParser
specifier|public
class|class
name|JavadocParser
extends|extends
name|Parser
block|{
DECL|field|NON_BREAKING_SPACE
specifier|private
specifier|static
specifier|final
name|String
name|NON_BREAKING_SPACE
init|=
literal|"\u00A0"
decl_stmt|;
DECL|field|JAVA6_NON_BREAKING_SPACE
specifier|private
specifier|static
specifier|final
name|String
name|JAVA6_NON_BREAKING_SPACE
init|=
literal|"&nbsp"
decl_stmt|;
DECL|field|hrefPattern
specifier|private
specifier|final
name|String
name|hrefPattern
decl_stmt|;
DECL|field|parserState
specifier|private
name|ParserState
name|parserState
decl_stmt|;
DECL|field|methodWithTypes
specifier|private
name|String
name|methodWithTypes
decl_stmt|;
DECL|field|methodTextBuilder
specifier|private
name|StringBuilder
name|methodTextBuilder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
DECL|field|methods
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|methods
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|methodText
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|methodText
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|errorMessage
specifier|private
name|String
name|errorMessage
decl_stmt|;
DECL|method|JavadocParser (DTD dtd, String docPath)
specifier|public
name|JavadocParser
parameter_list|(
name|DTD
name|dtd
parameter_list|,
name|String
name|docPath
parameter_list|)
block|{
name|super
argument_list|(
name|dtd
argument_list|)
expr_stmt|;
name|this
operator|.
name|hrefPattern
operator|=
name|docPath
operator|+
literal|"#"
expr_stmt|;
name|parserState
operator|=
name|ParserState
operator|.
name|INIT
expr_stmt|;
block|}
DECL|method|reset ()
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|parserState
operator|=
name|ParserState
operator|.
name|INIT
expr_stmt|;
name|methodWithTypes
operator|=
literal|null
expr_stmt|;
name|methodTextBuilder
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
name|methods
operator|.
name|clear
argument_list|()
expr_stmt|;
name|methodText
operator|.
name|clear
argument_list|()
expr_stmt|;
name|errorMessage
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|startTag (TagElement tag)
specifier|protected
name|void
name|startTag
parameter_list|(
name|TagElement
name|tag
parameter_list|)
throws|throws
name|ChangedCharSetException
block|{
name|super
operator|.
name|startTag
argument_list|(
name|tag
argument_list|)
expr_stmt|;
specifier|final
name|HTML
operator|.
name|Tag
name|htmlTag
init|=
name|tag
operator|.
name|getHTMLTag
argument_list|()
decl_stmt|;
if|if
condition|(
name|htmlTag
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|HTML
operator|.
name|Tag
operator|.
name|A
operator|.
name|equals
argument_list|(
name|htmlTag
argument_list|)
condition|)
block|{
specifier|final
name|SimpleAttributeSet
name|attributes
init|=
name|getAttributes
argument_list|()
decl_stmt|;
specifier|final
name|Object
name|name
init|=
name|attributes
operator|.
name|getAttribute
argument_list|(
name|HTML
operator|.
name|Attribute
operator|.
name|NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
specifier|final
name|String
name|nameAttr
init|=
operator|(
name|String
operator|)
name|name
decl_stmt|;
if|if
condition|(
name|parserState
operator|==
name|ParserState
operator|.
name|INIT
operator|&&
operator|(
literal|"method_summary"
operator|.
name|equals
argument_list|(
name|nameAttr
argument_list|)
operator|||
literal|"method.summary"
operator|.
name|equals
argument_list|(
name|nameAttr
argument_list|)
operator|)
condition|)
block|{
name|parserState
operator|=
name|ParserState
operator|.
name|METHOD_SUMMARY
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parserState
operator|==
name|ParserState
operator|.
name|METHOD
condition|)
block|{
if|if
condition|(
name|methodWithTypes
operator|==
literal|null
condition|)
block|{
specifier|final
name|String
name|hrefAttr
init|=
operator|(
name|String
operator|)
name|attributes
operator|.
name|getAttribute
argument_list|(
name|HTML
operator|.
name|Attribute
operator|.
name|HREF
argument_list|)
decl_stmt|;
if|if
condition|(
name|hrefAttr
operator|!=
literal|null
operator|&&
name|hrefAttr
operator|.
name|contains
argument_list|(
name|hrefPattern
argument_list|)
condition|)
block|{
comment|// unescape HTML
name|String
name|methodSignature
init|=
name|hrefAttr
operator|.
name|substring
argument_list|(
name|hrefAttr
operator|.
name|indexOf
argument_list|(
literal|'#'
argument_list|)
operator|+
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|int
name|firstHyphen
init|=
name|methodSignature
operator|.
name|indexOf
argument_list|(
literal|'-'
argument_list|)
decl_stmt|;
if|if
condition|(
name|firstHyphen
operator|!=
operator|-
literal|1
condition|)
block|{
specifier|final
name|int
name|lastHyphen
init|=
name|methodSignature
operator|.
name|lastIndexOf
argument_list|(
literal|'-'
argument_list|)
decl_stmt|;
name|methodSignature
operator|=
name|methodSignature
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|firstHyphen
argument_list|)
operator|+
literal|"("
operator|+
name|methodSignature
operator|.
name|substring
argument_list|(
name|firstHyphen
operator|+
literal|1
argument_list|,
name|lastHyphen
argument_list|)
operator|+
literal|")"
expr_stmt|;
name|methodSignature
operator|=
name|methodSignature
operator|.
name|replaceAll
argument_list|(
literal|"-"
argument_list|,
literal|","
argument_list|)
expr_stmt|;
block|}
comment|// support varargs
if|if
condition|(
name|methodSignature
operator|.
name|contains
argument_list|(
literal|"...)"
argument_list|)
condition|)
block|{
name|methodSignature
operator|=
name|methodSignature
operator|.
name|replaceAll
argument_list|(
literal|"\\.\\.\\.\\)"
argument_list|,
literal|"[])"
argument_list|)
expr_stmt|;
block|}
comment|// map Java8 array types
if|if
condition|(
name|methodSignature
operator|.
name|contains
argument_list|(
literal|":A"
argument_list|)
condition|)
block|{
name|methodSignature
operator|=
name|methodSignature
operator|.
name|replaceAll
argument_list|(
literal|":A"
argument_list|,
literal|"[]"
argument_list|)
expr_stmt|;
block|}
name|methodWithTypes
operator|=
name|unescapeHtml
argument_list|(
name|methodSignature
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
specifier|final
name|String
name|title
init|=
operator|(
name|String
operator|)
name|attributes
operator|.
name|getAttribute
argument_list|(
name|HTML
operator|.
name|Attribute
operator|.
name|TITLE
argument_list|)
decl_stmt|;
if|if
condition|(
name|title
operator|!=
literal|null
condition|)
block|{
comment|// append package name to type name text
name|methodTextBuilder
operator|.
name|append
argument_list|(
name|title
operator|.
name|substring
argument_list|(
name|title
operator|.
name|lastIndexOf
argument_list|(
literal|' '
argument_list|)
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|parserState
operator|==
name|ParserState
operator|.
name|METHOD_SUMMARY
operator|&&
name|HTML
operator|.
name|Tag
operator|.
name|CODE
operator|.
name|equals
argument_list|(
name|htmlTag
argument_list|)
condition|)
block|{
name|parserState
operator|=
name|ParserState
operator|.
name|METHOD
expr_stmt|;
block|}
block|}
block|}
DECL|method|unescapeHtml (String htmlString)
specifier|private
specifier|static
name|String
name|unescapeHtml
parameter_list|(
name|String
name|htmlString
parameter_list|)
block|{
name|String
name|result
init|=
name|StringEscapeUtils
operator|.
name|unescapeHtml
argument_list|(
name|htmlString
argument_list|)
operator|.
name|replaceAll
argument_list|(
name|NON_BREAKING_SPACE
argument_list|,
literal|" "
argument_list|)
operator|.
name|replaceAll
argument_list|(
name|JAVA6_NON_BREAKING_SPACE
argument_list|,
literal|" "
argument_list|)
decl_stmt|;
try|try
block|{
name|result
operator|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|result
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|ignored
parameter_list|)
block|{          }
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|handleEmptyTag (TagElement tag)
specifier|protected
name|void
name|handleEmptyTag
parameter_list|(
name|TagElement
name|tag
parameter_list|)
block|{
if|if
condition|(
name|parserState
operator|==
name|ParserState
operator|.
name|METHOD
operator|&&
name|HTML
operator|.
name|Tag
operator|.
name|CODE
operator|.
name|equals
argument_list|(
name|tag
operator|.
name|getHTMLTag
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|methodWithTypes
operator|!=
literal|null
condition|)
block|{
comment|// process collected method data
name|methods
operator|.
name|add
argument_list|(
name|methodWithTypes
argument_list|)
expr_stmt|;
name|this
operator|.
name|methodText
operator|.
name|put
argument_list|(
name|methodWithTypes
argument_list|,
name|getArgSignature
argument_list|()
argument_list|)
expr_stmt|;
comment|// clear the text builder for next method
name|methodTextBuilder
operator|.
name|delete
argument_list|(
literal|0
argument_list|,
name|methodTextBuilder
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|methodWithTypes
operator|=
literal|null
expr_stmt|;
block|}
name|parserState
operator|=
name|ParserState
operator|.
name|METHOD_SUMMARY
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parserState
operator|==
name|ParserState
operator|.
name|METHOD_SUMMARY
operator|&&
operator|!
name|methods
operator|.
name|isEmpty
argument_list|()
operator|&&
name|HTML
operator|.
name|Tag
operator|.
name|TABLE
operator|.
name|equals
argument_list|(
name|tag
operator|.
name|getHTMLTag
argument_list|()
argument_list|)
condition|)
block|{
comment|// end of method summary table
name|parserState
operator|=
name|ParserState
operator|.
name|INIT
expr_stmt|;
block|}
block|}
DECL|method|getArgSignature ()
specifier|private
name|String
name|getArgSignature
parameter_list|()
block|{
specifier|final
name|String
name|typeString
init|=
name|methodWithTypes
operator|.
name|substring
argument_list|(
name|methodWithTypes
operator|.
name|indexOf
argument_list|(
literal|'('
argument_list|)
operator|+
literal|1
argument_list|,
name|methodWithTypes
operator|.
name|indexOf
argument_list|(
literal|')'
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|typeString
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|"()"
return|;
block|}
comment|// unescape HTML method text
name|String
name|plainText
init|=
name|unescapeHtml
argument_list|(
name|methodTextBuilder
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
comment|// support varargs
if|if
condition|(
name|plainText
operator|.
name|contains
argument_list|(
literal|"..."
argument_list|)
condition|)
block|{
name|plainText
operator|=
name|plainText
operator|.
name|replaceAll
argument_list|(
literal|"\\.\\.\\."
argument_list|,
literal|"[]"
argument_list|)
expr_stmt|;
block|}
return|return
name|plainText
operator|.
name|substring
argument_list|(
name|plainText
operator|.
name|indexOf
argument_list|(
literal|'('
argument_list|)
argument_list|,
name|plainText
operator|.
name|indexOf
argument_list|(
literal|')'
argument_list|)
operator|+
literal|1
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|handleText (char[] text)
specifier|protected
name|void
name|handleText
parameter_list|(
name|char
index|[]
name|text
parameter_list|)
block|{
if|if
condition|(
name|parserState
operator|==
name|ParserState
operator|.
name|METHOD
operator|&&
name|methodWithTypes
operator|!=
literal|null
condition|)
block|{
name|methodTextBuilder
operator|.
name|append
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|handleError (int ln, String msg)
specifier|protected
name|void
name|handleError
parameter_list|(
name|int
name|ln
parameter_list|,
name|String
name|msg
parameter_list|)
block|{
if|if
condition|(
name|msg
operator|.
name|startsWith
argument_list|(
literal|"exception "
argument_list|)
condition|)
block|{
name|this
operator|.
name|errorMessage
operator|=
literal|"Exception parsing Javadoc line "
operator|+
name|ln
operator|+
literal|": "
operator|+
name|msg
expr_stmt|;
block|}
block|}
DECL|method|getErrorMessage ()
specifier|public
name|String
name|getErrorMessage
parameter_list|()
block|{
return|return
name|errorMessage
return|;
block|}
DECL|method|getMethods ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getMethods
parameter_list|()
block|{
return|return
name|methods
return|;
block|}
DECL|method|getMethodText ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getMethodText
parameter_list|()
block|{
return|return
name|methodText
return|;
block|}
DECL|enum|ParserState
specifier|private
specifier|static
enum|enum
name|ParserState
block|{
DECL|enumConstant|INIT
DECL|enumConstant|METHOD_SUMMARY
DECL|enumConstant|METHOD
name|INIT
block|,
name|METHOD_SUMMARY
block|,
name|METHOD
block|}
block|}
end_class

end_unit

