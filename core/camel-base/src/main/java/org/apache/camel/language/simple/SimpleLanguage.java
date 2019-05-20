begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|StaticService
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
name|annotations
operator|.
name|Language
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
name|support
operator|.
name|CamelContextHelper
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
name|support
operator|.
name|LRUCache
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
name|support
operator|.
name|LRUCacheFactory
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
name|support
operator|.
name|LanguageSupport
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
name|support
operator|.
name|PredicateToExpressionAdapter
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
name|support
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A<a href="http://camel.apache.org/simple.html">simple language</a>  * which maps simple property style notations to access headers and bodies.  * Examples of supported expressions are:  *<ul>  *<li>exchangeId to access the exchange id</li>  *<li>id to access the inbound message id</li>  *<li>in.body or body to access the inbound body</li>  *<li>in.body.OGNL or body.OGNL to access the inbound body using an OGNL expression</li>  *<li>mandatoryBodyAs(&lt;classname&gt;) to convert the in body to the given type, will throw exception if not possible to convert</li>  *<li>bodyAs(&lt;classname&gt;) to convert the in body to the given type, will return null if not possible to convert</li>  *<li>headerAs(&lt;key&gt;,&lt;classname&gt;) to convert the in header to the given type, will return null if not possible to convert</li>  *<li>out.body to access the inbound body</li>  *<li>in.header.foo or header.foo to access an inbound header called 'foo'</li>  *<li>in.header.foo[bar] or header.foo[bar] to access an inbound header called 'foo' as a Map and lookup the map with 'bar' as key</li>  *<li>in.header.foo.OGNL or header.OGNL to access an inbound header called 'foo' using an OGNL expression</li>  *<li>out.header.foo to access an outbound header called 'foo'</li>  *<li>property.foo to access the exchange property called 'foo'</li>  *<li>property.foo.OGNL to access the exchange property called 'foo' using an OGNL expression</li>  *<li>sys.foo to access the system property called 'foo'</li>  *<li>sysenv.foo to access the system environment called 'foo'</li>  *<li>exception.messsage to access the exception message</li>  *<li>threadName to access the current thread name</li>  *<li>date:&lt;command&gt; evaluates to a Date object  *     Supported commands are:<tt>now</tt> for current timestamp,  *<tt>in.header.xxx</tt> or<tt>header.xxx</tt> to use the Date object in the in header.  *<tt>out.header.xxx</tt> to use the Date object in the out header.  *<tt>property.xxx</tt> to use the Date object in the exchange property.  *<tt>file</tt> for the last modified timestamp of the file (available with a File consumer).  *     Command accepts offsets such as:<tt>now-24h</tt> or<tt>in.header.xxx+1h</tt> or even<tt>now+1h30m-100</tt>.  *</li>  *<li>date:&lt;command&gt;:&lt;pattern&gt; for date formatting using {@link java.text.SimpleDateFormat} patterns</li>  *<li>date-with-timezone:&lt;command&gt;:&lt;timezone&gt;:&lt;pattern&gt; for date formatting using {@link java.text.SimpleDateFormat} timezones and patterns</li>  *<li>bean:&lt;bean expression&gt; to invoke a bean using the  * {@link org.apache.camel.language.bean.BeanLanguage BeanLanguage}</li>  *<li>properties:&lt;[locations]&gt;:&lt;key&gt; for using property placeholders using the  *     {@link org.apache.camel.component.properties.PropertiesComponent}.  *     The locations parameter is optional and you can enter multiple locations separated with comma.  *</li> *</ul>  *<p/>  * The simple language supports OGNL notation when accessing either body or header.  *<p/>  * The simple language now also includes file language out of the box which means the following expression is also  * supported:  *<ul>  *<li><tt>file:name</tt> to access the file name (is relative, see note below))</li>  *<li><tt>file:name.noext</tt> to access the file name with no extension</li>  *<li><tt>file:name.ext</tt> to access the file extension</li>  *<li><tt>file:ext</tt> to access the file extension</li>  *<li><tt>file:onlyname</tt> to access the file name (no paths)</li>  *<li><tt>file:onlyname.noext</tt> to access the file name (no paths) with no extension</li>  *<li><tt>file:parent</tt> to access the parent file name</li>  *<li><tt>file:path</tt> to access the file path name</li>  *<li><tt>file:absolute</tt> is the file regarded as absolute or relative</li>  *<li><tt>file:absolute.path</tt> to access the absolute file path name</li>  *<li><tt>file:length</tt> to access the file length as a Long type</li>  *<li><tt>file:size</tt> to access the file length as a Long type</li>  *<li><tt>file:modified</tt> to access the file last modified as a Date type</li>  *</ul>  * The<b>relative</b> file is the filename with the starting directory clipped, as opposed to<b>path</b> that will  * return the full path including the starting directory.  *<br/>  * The<b>only</b> file is the filename only with all paths clipped.  */
end_comment

begin_class
annotation|@
name|Language
argument_list|(
literal|"simple"
argument_list|)
DECL|class|SimpleLanguage
specifier|public
class|class
name|SimpleLanguage
extends|extends
name|LanguageSupport
implements|implements
name|StaticService
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SimpleLanguage
operator|.
name|class
argument_list|)
decl_stmt|;
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
DECL|field|allowEscape
name|boolean
name|allowEscape
init|=
literal|true
decl_stmt|;
comment|// use caches to avoid re-parsing the same expressions over and over again
DECL|field|cacheExpression
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Expression
argument_list|>
name|cacheExpression
decl_stmt|;
DECL|field|cachePredicate
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Predicate
argument_list|>
name|cachePredicate
decl_stmt|;
comment|/**      * Default constructor.      */
DECL|method|SimpleLanguage ()
specifier|public
name|SimpleLanguage
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|init ()
specifier|public
name|void
name|init
parameter_list|()
block|{
comment|// setup cache which requires CamelContext to be set first
if|if
condition|(
name|cacheExpression
operator|==
literal|null
operator|&&
name|cachePredicate
operator|==
literal|null
operator|&&
name|getCamelContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|int
name|maxSize
init|=
name|CamelContextHelper
operator|.
name|getMaximumSimpleCacheSize
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|maxSize
operator|>
literal|0
condition|)
block|{
name|cacheExpression
operator|=
name|LRUCacheFactory
operator|.
name|newLRUCache
argument_list|(
literal|16
argument_list|,
name|maxSize
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|cachePredicate
operator|=
name|LRUCacheFactory
operator|.
name|newLRUCache
argument_list|(
literal|16
argument_list|,
name|maxSize
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Simple language predicate/expression cache size: {}"
argument_list|,
name|maxSize
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Simple language disabled predicate/expression cache"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
if|if
condition|(
name|cachePredicate
operator|instanceof
name|LRUCache
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LRUCache
name|cache
init|=
operator|(
name|LRUCache
operator|)
name|cachePredicate
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Clearing simple language predicate cache[size={}, hits={}, misses={}, evicted={}]"
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|,
name|cache
operator|.
name|getHits
argument_list|()
argument_list|,
name|cache
operator|.
name|getMisses
argument_list|()
argument_list|,
name|cache
operator|.
name|getEvicted
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|cacheExpression
operator|instanceof
name|LRUCache
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LRUCache
name|cache
init|=
operator|(
name|LRUCache
operator|)
name|cacheExpression
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Clearing simple language expression cache[size={}, hits={}, misses={}, evicted={}]"
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|,
name|cache
operator|.
name|getHits
argument_list|()
argument_list|,
name|cache
operator|.
name|getMisses
argument_list|()
argument_list|,
name|cache
operator|.
name|getEvicted
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|expression
argument_list|,
literal|"expression"
argument_list|)
expr_stmt|;
name|Predicate
name|answer
init|=
name|cachePredicate
operator|!=
literal|null
condition|?
name|cachePredicate
operator|.
name|get
argument_list|(
name|expression
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|expression
operator|=
name|loadResource
argument_list|(
name|expression
argument_list|)
expr_stmt|;
name|SimplePredicateParser
name|parser
init|=
operator|new
name|SimplePredicateParser
argument_list|(
name|expression
argument_list|,
name|allowEscape
argument_list|,
name|cacheExpression
argument_list|)
decl_stmt|;
name|answer
operator|=
name|parser
operator|.
name|parsePredicate
argument_list|()
expr_stmt|;
if|if
condition|(
name|cachePredicate
operator|!=
literal|null
operator|&&
name|answer
operator|!=
literal|null
condition|)
block|{
name|cachePredicate
operator|.
name|put
argument_list|(
name|expression
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|expression
argument_list|,
literal|"expression"
argument_list|)
expr_stmt|;
name|Expression
name|answer
init|=
name|cacheExpression
operator|!=
literal|null
condition|?
name|cacheExpression
operator|.
name|get
argument_list|(
name|expression
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|expression
operator|=
name|loadResource
argument_list|(
name|expression
argument_list|)
expr_stmt|;
name|SimpleExpressionParser
name|parser
init|=
operator|new
name|SimpleExpressionParser
argument_list|(
name|expression
argument_list|,
name|allowEscape
argument_list|,
name|cacheExpression
argument_list|)
decl_stmt|;
name|answer
operator|=
name|parser
operator|.
name|parseExpression
argument_list|()
expr_stmt|;
if|if
condition|(
name|cacheExpression
operator|!=
literal|null
operator|&&
name|answer
operator|!=
literal|null
condition|)
block|{
name|cacheExpression
operator|.
name|put
argument_list|(
name|expression
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Creates a new {@link Expression}.      *<p/>      *<b>Important:</b> If you need to use a predicate (function to return true|false) then use      * {@link #predicate(String)} instead.      */
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
name|expression
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Creates a new {@link Expression} (or {@link Predicate}      * if the resultType is a<tt>Boolean</tt>, or<tt>boolean</tt> type).      */
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
return|return
operator|new
name|SimpleLanguage
argument_list|()
operator|.
name|createExpression
argument_list|(
name|expression
argument_list|,
name|resultType
argument_list|)
return|;
block|}
DECL|method|createExpression (String expression, Class<?> resultType)
specifier|public
name|Expression
name|createExpression
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
if|if
condition|(
name|resultType
operator|==
name|Boolean
operator|.
name|class
operator|||
name|resultType
operator|==
name|boolean
operator|.
name|class
condition|)
block|{
comment|// if its a boolean as result then its a predicate
name|Predicate
name|predicate
init|=
name|createPredicate
argument_list|(
name|expression
argument_list|)
decl_stmt|;
return|return
name|PredicateToExpressionAdapter
operator|.
name|toExpression
argument_list|(
name|predicate
argument_list|)
return|;
block|}
else|else
block|{
name|Expression
name|exp
init|=
name|createExpression
argument_list|(
name|expression
argument_list|)
decl_stmt|;
if|if
condition|(
name|resultType
operator|!=
literal|null
condition|)
block|{
name|exp
operator|=
name|ExpressionBuilder
operator|.
name|convertToExpression
argument_list|(
name|exp
argument_list|,
name|resultType
argument_list|)
expr_stmt|;
block|}
return|return
name|exp
return|;
block|}
block|}
comment|/**      * Creates a new {@link Expression}.      *<p/>      *<b>Important:</b> If you need to use a predicate (function to return true|false) then use      * {@link #predicate(String)} instead.      */
DECL|method|expression (String expression)
specifier|public
specifier|static
name|Expression
name|expression
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
comment|/**      * Creates a new {@link Predicate}.      */
DECL|method|predicate (String predicate)
specifier|public
specifier|static
name|Predicate
name|predicate
parameter_list|(
name|String
name|predicate
parameter_list|)
block|{
return|return
name|SIMPLE
operator|.
name|createPredicate
argument_list|(
name|predicate
argument_list|)
return|;
block|}
comment|/**      * Does the expression include a simple function.      *      * @param expression the expression      * @return<tt>true</tt> if one or more simple function is included in the expression      */
DECL|method|hasSimpleFunction (String expression)
specifier|public
specifier|static
name|boolean
name|hasSimpleFunction
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
return|return
name|SimpleTokenizer
operator|.
name|hasFunctionStartToken
argument_list|(
name|expression
argument_list|)
return|;
block|}
block|}
end_class

end_unit
