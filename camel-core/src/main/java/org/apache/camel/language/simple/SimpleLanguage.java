begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.simple
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|simple
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
name|Expression
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
name|ExpressionIllegalSyntaxException
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
name|builder
operator|.
name|ExpressionBuilder
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
name|OgnlHelper
import|;
end_import

begin_comment
comment|/**  * A<a href="http://camel.apache.org/simple.html">simple language</a>  * which maps simple property style notations to access headers and bodies.  * Examples of supported expressions are:  *<ul>  *<li>exchangeId to access the exchange id</li>  *<li>id to access the inbound message id</li>  *<li>in.body or body to access the inbound body</li>  *<li>in.body.OGNL or body.OGNL to access the inbound body using an OGNL expression</li>  *<li>bodyAs(&lt;classname&gt;) to convert the in body to the given type</li>  *<li>out.body to access the inbound body</li>  *<li>in.header.foo or header.foo to access an inbound header called 'foo'</li>  *<li>in.header.foo[bar] or header.foo[bar] to access an inbound header called 'foo' as a Map and lookup the map with 'bar' as key</li>  *<li>in.header.foo.OGNL or header.OGNL to access an inbound header called 'foo' using an OGNL expression</li>  *<li>out.header.foo to access an outbound header called 'foo'</li>  *<li>property.foo to access the exchange property called 'foo'</li>  *<li>sys.foo to access the system property called 'foo'</li>  *<li>sysenv.foo to access the system environment called 'foo'</li>  *<li>exception.messsage to access the exception message</li>  *<li>threadName to access the current thread name</li>  *<li>date:&lt;command&gt;:&lt;pattern&gt; for date formatting using the {@link java.text.SimpleDateFormat} patterns.  *     Supported commands are:<tt>now</tt> for current timestamp,  *<tt>in.header.xxx</tt> or<tt>header.xxx</tt> to use the Date object in the in header.  *<tt>out.header.xxx</tt> to use the Date object in the out header.  *</li>  *<li>bean:&lt;bean expression&gt; to invoke a bean using the  * {@link org.apache.camel.language.bean.BeanLanguage BeanLanguage}</li>  *<li>properties:&lt;[locations]&gt;:&lt;key&gt; for using property placeholders using the  *     {@link org.apache.camel.component.properties.PropertiesComponent}.  *     The locations parameter is optional and you can enter multiple locations separated with comma.  *</li> *</ul>  *<p/>  * The simple language supports OGNL notation when accessing either body or header.  *<p/>  * The simple language now also includes file language out of the box which means the following expression is also  * supported:  *<ul>  *<li><tt>file:name</tt> to access the file name (is relative, see note below))</li>  *<li><tt>file:name.noext</tt> to access the file name with no extension</li>  *<li><tt>file:ext</tt> to access the file extension</li>  *<li><tt>file:onlyname</tt> to access the file name (no paths)</li>  *<li><tt>file:onlyname.noext</tt> to access the file name (no paths) with no extension</li>  *<li><tt>file:parent</tt> to access the parent file name</li>  *<li><tt>file:path</tt> to access the file path name</li>  *<li><tt>file:absolute</tt> is the file regarded as absolute or relative</li>  *<li><tt>file:absolute.path</tt> to access the absolute file path name</li>  *<li><tt>file:length</tt> to access the file length as a Long type</li>  *<li><tt>file:modified</tt> to access the file last modified as a Date type</li>  *<li><tt>date:&lt;command&gt;:&lt;pattern&gt;</tt> for date formatting using the {@link java.text.SimpleDateFormat} patterns.  *     Additional Supported commands are:<tt>file</tt> for the last modified timestamp of the file.  *     All the commands from {@link SimpleLanguage} is also available.  *</li>  *</ul>  * The<b>relative</b> file is the filename with the starting directory clipped, as opposed to<b>path</b> that will  * return the full path including the starting directory.  *<br/>  * The<b>only</b> file is the filename only with all paths clipped.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|SimpleLanguage
specifier|public
class|class
name|SimpleLanguage
extends|extends
name|SimpleLanguageSupport
block|{
DECL|field|SIMPLE
specifier|private
specifier|static
specifier|final
name|SimpleLanguage
name|SIMPLE
init|=
operator|new
name|SimpleLanguage
argument_list|()
decl_stmt|;
DECL|method|simple (String expression)
specifier|public
specifier|static
name|Expression
name|simple
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
return|return
name|SIMPLE
operator|.
name|createExpression
argument_list|(
name|expression
argument_list|)
return|;
block|}
DECL|method|createSimpleExpression (String expression, boolean strict)
specifier|protected
name|Expression
name|createSimpleExpression
parameter_list|(
name|String
name|expression
parameter_list|,
name|boolean
name|strict
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEqualToAny
argument_list|(
name|expression
argument_list|,
literal|"body"
argument_list|,
literal|"in.body"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|bodyExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|expression
argument_list|,
literal|"out.body"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|outBodyExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|expression
argument_list|,
literal|"id"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|messageIdExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|expression
argument_list|,
literal|"exchangeId"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|exchangeIdExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|expression
argument_list|,
literal|"exception.message"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|exchangeExceptionMessageExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|expression
argument_list|,
literal|"threadName"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|threadNameExpression
argument_list|()
return|;
block|}
comment|// bodyAs
name|String
name|remainder
init|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"bodyAs"
argument_list|,
name|expression
argument_list|)
decl_stmt|;
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
name|String
name|type
init|=
name|ObjectHelper
operator|.
name|between
argument_list|(
name|remainder
argument_list|,
literal|"("
argument_list|,
literal|")"
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ExpressionIllegalSyntaxException
argument_list|(
literal|"Valid syntax: ${bodyAs(type)} was: "
operator|+
name|expression
argument_list|)
throw|;
block|}
return|return
name|ExpressionBuilder
operator|.
name|bodyExpression
argument_list|(
name|type
argument_list|)
return|;
block|}
comment|// body OGNL
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"body"
argument_list|,
name|expression
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|==
literal|null
condition|)
block|{
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"in.body"
argument_list|,
name|expression
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
name|boolean
name|invalid
init|=
name|OgnlHelper
operator|.
name|isInvalidValidOgnlExpression
argument_list|(
name|remainder
argument_list|)
decl_stmt|;
if|if
condition|(
name|invalid
condition|)
block|{
throw|throw
operator|new
name|ExpressionIllegalSyntaxException
argument_list|(
literal|"Valid syntax: ${body.OGNL} was: "
operator|+
name|expression
argument_list|)
throw|;
block|}
return|return
name|ExpressionBuilder
operator|.
name|bodyOgnlExpression
argument_list|(
name|remainder
argument_list|)
return|;
block|}
comment|// in header expression
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"in.headers"
argument_list|,
name|expression
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|==
literal|null
condition|)
block|{
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"in.header"
argument_list|,
name|expression
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|remainder
operator|==
literal|null
condition|)
block|{
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"headers"
argument_list|,
name|expression
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|remainder
operator|==
literal|null
condition|)
block|{
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"header"
argument_list|,
name|expression
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
comment|// remove leading dot
name|remainder
operator|=
name|remainder
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// validate syntax
name|boolean
name|invalid
init|=
name|OgnlHelper
operator|.
name|isInvalidValidOgnlExpression
argument_list|(
name|remainder
argument_list|)
decl_stmt|;
if|if
condition|(
name|invalid
condition|)
block|{
throw|throw
operator|new
name|ExpressionIllegalSyntaxException
argument_list|(
literal|"Valid syntax: ${header.name[key]} was: "
operator|+
name|expression
argument_list|)
throw|;
block|}
if|if
condition|(
name|OgnlHelper
operator|.
name|isValidOgnlExpression
argument_list|(
name|remainder
argument_list|)
condition|)
block|{
comment|// ognl based header
return|return
name|ExpressionBuilder
operator|.
name|headersOgnlExpression
argument_list|(
name|remainder
argument_list|)
return|;
block|}
else|else
block|{
comment|// regular header
return|return
name|ExpressionBuilder
operator|.
name|headerExpression
argument_list|(
name|remainder
argument_list|)
return|;
block|}
block|}
comment|// out header expression
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"out.header."
argument_list|,
name|expression
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|==
literal|null
condition|)
block|{
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"out.headers."
argument_list|,
name|expression
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|outHeaderExpression
argument_list|(
name|remainder
argument_list|)
return|;
block|}
comment|// property
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"property."
argument_list|,
name|expression
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|propertyExpression
argument_list|(
name|remainder
argument_list|)
return|;
block|}
comment|// system property
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"sys."
argument_list|,
name|expression
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|systemPropertyExpression
argument_list|(
name|remainder
argument_list|)
return|;
block|}
comment|// system property
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"sysenv."
argument_list|,
name|expression
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|systemEnvironmentExpression
argument_list|(
name|remainder
argument_list|)
return|;
block|}
comment|// file: prefix
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"file:"
argument_list|,
name|expression
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
name|Expression
name|fileExpression
init|=
name|createSimpleFileExpression
argument_list|(
name|remainder
argument_list|)
decl_stmt|;
if|if
condition|(
name|expression
operator|!=
literal|null
condition|)
block|{
return|return
name|fileExpression
return|;
block|}
block|}
comment|// date: prefix
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"date:"
argument_list|,
name|expression
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|parts
init|=
name|remainder
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|<
literal|2
condition|)
block|{
throw|throw
operator|new
name|ExpressionIllegalSyntaxException
argument_list|(
literal|"Valid syntax: ${date:command:pattern} was: "
operator|+
name|expression
argument_list|)
throw|;
block|}
name|String
name|command
init|=
name|ObjectHelper
operator|.
name|before
argument_list|(
name|remainder
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
name|String
name|pattern
init|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|remainder
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
return|return
name|ExpressionBuilder
operator|.
name|dateExpression
argument_list|(
name|command
argument_list|,
name|pattern
argument_list|)
return|;
block|}
comment|// bean: prefix
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"bean:"
argument_list|,
name|expression
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|beanExpression
argument_list|(
name|remainder
argument_list|)
return|;
block|}
comment|// properties: prefix
name|remainder
operator|=
name|ifStartsWithReturnRemainder
argument_list|(
literal|"properties:"
argument_list|,
name|expression
argument_list|)
expr_stmt|;
if|if
condition|(
name|remainder
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|parts
init|=
name|remainder
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|>
literal|2
condition|)
block|{
throw|throw
operator|new
name|ExpressionIllegalSyntaxException
argument_list|(
literal|"Valid syntax: ${properties:[locations]:key} was: "
operator|+
name|expression
argument_list|)
throw|;
block|}
name|String
name|locations
init|=
literal|null
decl_stmt|;
name|String
name|key
init|=
name|remainder
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|==
literal|2
condition|)
block|{
name|locations
operator|=
name|ObjectHelper
operator|.
name|before
argument_list|(
name|remainder
argument_list|,
literal|":"
argument_list|)
expr_stmt|;
name|key
operator|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|remainder
argument_list|,
literal|":"
argument_list|)
expr_stmt|;
block|}
return|return
name|ExpressionBuilder
operator|.
name|propertiesComponentExpression
argument_list|(
name|key
argument_list|,
name|locations
argument_list|)
return|;
block|}
if|if
condition|(
name|strict
condition|)
block|{
throw|throw
operator|new
name|ExpressionIllegalSyntaxException
argument_list|(
name|expression
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|ExpressionBuilder
operator|.
name|constantExpression
argument_list|(
name|expression
argument_list|)
return|;
block|}
block|}
DECL|method|createSimpleFileExpression (String remainder)
specifier|public
name|Expression
name|createSimpleFileExpression
parameter_list|(
name|String
name|remainder
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"name"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|fileNameExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"name.noext"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|fileNameNoExtensionExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"onlyname"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|fileOnlyNameExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"onlyname.noext"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|fileOnlyNameNoExtensionExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"ext"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|fileExtensionExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"parent"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|fileParentExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"path"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|filePathExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"absolute"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|fileAbsoluteExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"absolute.path"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|fileAbsolutePathExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"length"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|fileSizeExpression
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|remainder
argument_list|,
literal|"modified"
argument_list|)
condition|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|fileLastModifiedExpression
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
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
block|}
end_class

end_unit

