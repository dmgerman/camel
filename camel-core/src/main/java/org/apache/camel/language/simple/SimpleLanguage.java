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
name|IsSingleton
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
name|Predicate
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
name|spi
operator|.
name|Language
import|;
end_import

begin_comment
comment|/**  * A<a href="http://camel.apache.org/simple.html">simple language</a>  * which maps simple property style notations to access headers and bodies.  * Examples of supported expressions are:  *<ul>  *<li>exchangeId to access the exchange id</li>  *<li>id to access the inbound message id</li>  *<li>in.body or body to access the inbound body</li>  *<li>in.body.OGNL or body.OGNL to access the inbound body using an OGNL expression</li>  *<li>mandatoryBodyAs(&lt;classname&gt;) to convert the in body to the given type, will throw exception if not possible to convert</li>  *<li>bodyAs(&lt;classname&gt;) to convert the in body to the given type, will return null if not possible to convert</li>  *<li>headerAs(&lt;key&gt;,&lt;classname&gt;) to convert the in header to the given type, will return null if not possible to convert</li>  *<li>out.body to access the inbound body</li>  *<li>in.header.foo or header.foo to access an inbound header called 'foo'</li>  *<li>in.header.foo[bar] or header.foo[bar] to access an inbound header called 'foo' as a Map and lookup the map with 'bar' as key</li>  *<li>in.header.foo.OGNL or header.OGNL to access an inbound header called 'foo' using an OGNL expression</li>  *<li>out.header.foo to access an outbound header called 'foo'</li>  *<li>property.foo to access the exchange property called 'foo'</li>  *<li>property.foo.OGNL to access the exchange property called 'foo' using an OGNL expression</li>  *<li>sys.foo to access the system property called 'foo'</li>  *<li>sysenv.foo to access the system environment called 'foo'</li>  *<li>exception.messsage to access the exception message</li>  *<li>threadName to access the current thread name</li>  *<li>date:&lt;command&gt;:&lt;pattern&gt; for date formatting using the {@link java.text.SimpleDateFormat} patterns.  *     Supported commands are:<tt>now</tt> for current timestamp,  *<tt>in.header.xxx</tt> or<tt>header.xxx</tt> to use the Date object in the in header.  *<tt>out.header.xxx</tt> to use the Date object in the out header.  *</li>  *<li>bean:&lt;bean expression&gt; to invoke a bean using the  * {@link org.apache.camel.language.bean.BeanLanguage BeanLanguage}</li>  *<li>properties:&lt;[locations]&gt;:&lt;key&gt; for using property placeholders using the  *     {@link org.apache.camel.component.properties.PropertiesComponent}.  *     The locations parameter is optional and you can enter multiple locations separated with comma.  *</li> *</ul>  *<p/>  * The simple language supports OGNL notation when accessing either body or header.  *<p/>  * The simple language now also includes file language out of the box which means the following expression is also  * supported:  *<ul>  *<li><tt>file:name</tt> to access the file name (is relative, see note below))</li>  *<li><tt>file:name.noext</tt> to access the file name with no extension</li>  *<li><tt>file:name.ext</tt> to access the file extension</li>  *<li><tt>file:ext</tt> to access the file extension</li>  *<li><tt>file:onlyname</tt> to access the file name (no paths)</li>  *<li><tt>file:onlyname.noext</tt> to access the file name (no paths) with no extension</li>  *<li><tt>file:parent</tt> to access the parent file name</li>  *<li><tt>file:path</tt> to access the file path name</li>  *<li><tt>file:absolute</tt> is the file regarded as absolute or relative</li>  *<li><tt>file:absolute.path</tt> to access the absolute file path name</li>  *<li><tt>file:length</tt> to access the file length as a Long type</li>  *<li><tt>file:size</tt> to access the file length as a Long type</li>  *<li><tt>file:modified</tt> to access the file last modified as a Date type</li>  *<li><tt>date:&lt;command&gt;:&lt;pattern&gt;</tt> for date formatting using the {@link java.text.SimpleDateFormat} patterns.  *     Additional Supported commands are:<tt>file</tt> for the last modified timestamp of the file.  *     All the commands from {@link SimpleLanguage} is also available.  *</li>  *</ul>  * The<b>relative</b> file is the filename with the starting directory clipped, as opposed to<b>path</b> that will  * return the full path including the starting directory.  *<br/>  * The<b>only</b> file is the filename only with all paths clipped.  *  */
end_comment

begin_class
DECL|class|SimpleLanguage
specifier|public
class|class
name|SimpleLanguage
implements|implements
name|Language
implements|,
name|IsSingleton
block|{
comment|// singleton for expressions without a result type
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
DECL|field|resultType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
decl_stmt|;
DECL|method|getResultType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getResultType
parameter_list|()
block|{
return|return
name|resultType
return|;
block|}
DECL|method|setResultType (Class<?> resultType)
specifier|public
name|void
name|setResultType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|)
block|{
name|this
operator|.
name|resultType
operator|=
name|resultType
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
comment|// we cannot be singleton as we have state
return|return
literal|false
return|;
block|}
DECL|method|createPredicate (String expression)
specifier|public
name|Predicate
name|createPredicate
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
comment|// support old simple language syntax
name|Predicate
name|answer
init|=
name|SimpleBackwardsCompatibleParser
operator|.
name|parsePredicate
argument_list|(
name|expression
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
comment|// use the new parser
name|SimplePredicateParser
name|parser
init|=
operator|new
name|SimplePredicateParser
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|answer
operator|=
name|parser
operator|.
name|parsePredicate
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|createExpression (String expression)
specifier|public
name|Expression
name|createExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
comment|// support old simple language syntax
name|Expression
name|answer
init|=
name|SimpleBackwardsCompatibleParser
operator|.
name|parseExpression
argument_list|(
name|expression
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
comment|// use the new parser
name|SimpleExpressionParser
name|parser
init|=
operator|new
name|SimpleExpressionParser
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|answer
operator|=
name|parser
operator|.
name|parseExpression
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|resultType
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|ExpressionBuilder
operator|.
name|convertToExpression
argument_list|(
name|answer
argument_list|,
name|resultType
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
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
DECL|method|simple (String expression, Class<?> resultType)
specifier|public
specifier|static
name|Expression
name|simple
parameter_list|(
name|String
name|expression
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|)
block|{
name|SimpleLanguage
name|answer
init|=
operator|new
name|SimpleLanguage
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setResultType
argument_list|(
name|resultType
argument_list|)
expr_stmt|;
return|return
name|answer
operator|.
name|createExpression
argument_list|(
name|expression
argument_list|)
return|;
block|}
DECL|method|changeFunctionStartToken (String... startToken)
specifier|public
specifier|static
name|void
name|changeFunctionStartToken
parameter_list|(
name|String
modifier|...
name|startToken
parameter_list|)
block|{
name|SimpleTokenizer
operator|.
name|changeFunctionStartToken
argument_list|(
name|startToken
argument_list|)
expr_stmt|;
block|}
DECL|method|changeFunctionEndToken (String... endToken)
specifier|public
specifier|static
name|void
name|changeFunctionEndToken
parameter_list|(
name|String
modifier|...
name|endToken
parameter_list|)
block|{
name|SimpleTokenizer
operator|.
name|changeFunctionEndToken
argument_list|(
name|endToken
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

