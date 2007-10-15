begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|model
operator|.
name|language
operator|.
name|ExpressionType
import|;
end_import

begin_comment
comment|/**  * TODO workaround for a compiler bug. Remove ASAP!  * We seem to get compile time errors when creating routes of the form  *  *<code>from("foo").filter().xpath("//foo").to("bar")</code>  * as the type of the return value of xpath(String) is of type {@link Object} rather than  * the expected FilterType if we remove the "extends ProcessorType" from the T declaration  * so we've split the ExpressionClause class into two; one with the extends and one without  * with some cut and paste between them which is not ideal  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|ExpressionClauseSupport
specifier|public
class|class
name|ExpressionClauseSupport
parameter_list|<
name|T
parameter_list|>
extends|extends
name|ExpressionType
block|{
DECL|field|result
specifier|private
name|T
name|result
decl_stmt|;
DECL|field|language
specifier|private
name|String
name|language
decl_stmt|;
DECL|method|ExpressionClauseSupport (T result)
specifier|public
name|ExpressionClauseSupport
parameter_list|(
name|T
name|result
parameter_list|)
block|{
name|this
operator|.
name|result
operator|=
name|result
expr_stmt|;
block|}
comment|// Fluent API
comment|//-------------------------------------------------------------------------
comment|/**      * Evaluates the<a href="http://activemq.apache.org/camel/el.html">EL Language from JSP and JSF</a>      * using the<a href="http://activemq.apache.org/camel/juel.html">JUEL library</a>      *      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|el (String text)
specifier|public
name|T
name|el
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|language
argument_list|(
literal|"el"
argument_list|,
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates a<a href="http://activemq.apache.org/camel/groovy.html">Groovy expression</a>      *      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|groovy (String text)
specifier|public
name|T
name|groovy
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|language
argument_list|(
literal|"groovy"
argument_list|,
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates a<a href="http://activemq.apache.org/camel/java-script.html">JavaScript expression</a>      *      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|javaScript (String text)
specifier|public
name|T
name|javaScript
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|language
argument_list|(
literal|"js"
argument_list|,
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates an<a href="http://activemq.apache.org/camel/ognl.html">OGNL expression</a>      *      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|ognl (String text)
specifier|public
name|T
name|ognl
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|language
argument_list|(
literal|"ognl"
argument_list|,
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates a<a href="http://activemq.apache.org/camel/php.html">PHP expression</a>      *      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|php (String text)
specifier|public
name|T
name|php
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|language
argument_list|(
literal|"php"
argument_list|,
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates a<a href="http://activemq.apache.org/camel/python.html">Python expression</a>      *      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|python (String text)
specifier|public
name|T
name|python
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|language
argument_list|(
literal|"python"
argument_list|,
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates a<a href="http://activemq.apache.org/camel/ruby.html">Ruby expression</a>      *      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|ruby (String text)
specifier|public
name|T
name|ruby
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|language
argument_list|(
literal|"ruby"
argument_list|,
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates an<a href="http://activemq.apache.org/camel/sql.html">SQL expression</a>      *      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|sql (String text)
specifier|public
name|T
name|sql
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|language
argument_list|(
literal|"sql"
argument_list|,
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates a<a href="http://activemq.apache.org/camel/simple.html">Simple expression</a>      *      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|simple (String text)
specifier|public
name|T
name|simple
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|language
argument_list|(
literal|"simple"
argument_list|,
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates an<a href="http://activemq.apache.org/camel/xpath.html">XPath expression</a>      *      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|xpath (String text)
specifier|public
name|T
name|xpath
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|language
argument_list|(
literal|"xpath"
argument_list|,
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates an<a href="http://activemq.apache.org/camel/xquery.html">XQuery expression</a>      *      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|xquery (String text)
specifier|public
name|T
name|xquery
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|language
argument_list|(
literal|"xquery"
argument_list|,
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates a given language name with the expression text      *      * @param language   the name of the language      * @param expression the expression in the given language      * @return the builder to continue processing the DSL      */
DECL|method|language (String language, String expression)
specifier|public
name|T
name|language
parameter_list|(
name|String
name|language
parameter_list|,
name|String
name|expression
parameter_list|)
block|{
name|setLanguage
argument_list|(
name|language
argument_list|)
expr_stmt|;
name|setExpression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getLanguage ()
specifier|public
name|String
name|getLanguage
parameter_list|()
block|{
return|return
name|language
return|;
block|}
DECL|method|setLanguage (String language)
specifier|public
name|void
name|setLanguage
parameter_list|(
name|String
name|language
parameter_list|)
block|{
name|this
operator|.
name|language
operator|=
name|language
expr_stmt|;
block|}
block|}
end_class

end_unit

