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
name|ExpressionNode
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
name|ProcessorType
import|;
end_import

begin_comment
comment|/**  * Represents an expression clause within the DSL which when the expression is complete  * the clause continues to another part of the DSL  *  * @version $Revision: 1.1 $  */
end_comment

begin_class
DECL|class|ExpressionClause
specifier|public
class|class
name|ExpressionClause
parameter_list|<
name|T
extends|extends
name|ProcessorType
parameter_list|>
extends|extends
name|ExpressionClauseSupport
argument_list|<
name|T
argument_list|>
block|{
DECL|method|createAndSetExpression (T result)
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|ExpressionNode
parameter_list|>
name|ExpressionClause
argument_list|<
name|T
argument_list|>
name|createAndSetExpression
parameter_list|(
name|T
name|result
parameter_list|)
block|{
name|ExpressionClause
argument_list|<
name|T
argument_list|>
name|clause
init|=
operator|new
name|ExpressionClause
argument_list|<
name|T
argument_list|>
argument_list|(
name|result
argument_list|)
decl_stmt|;
name|result
operator|.
name|setExpression
argument_list|(
name|clause
argument_list|)
expr_stmt|;
return|return
name|clause
return|;
block|}
DECL|method|ExpressionClause (T result)
specifier|public
name|ExpressionClause
parameter_list|(
name|T
name|result
parameter_list|)
block|{
name|super
argument_list|(
name|result
argument_list|)
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
return|return
name|super
operator|.
name|language
argument_list|(
name|language
argument_list|,
name|expression
argument_list|)
return|;
block|}
block|}
end_class

end_unit

