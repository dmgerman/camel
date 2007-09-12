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
name|CamelContext
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
name|model
operator|.
name|ProcessorType
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
comment|/**  * Represents an expression clause within the DSL  *  * @version $Revision: 1.1 $  */
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
block|{
DECL|field|result
specifier|private
name|T
name|result
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
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
comment|/**      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
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
comment|/**      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
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
comment|/**      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
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
comment|/**      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
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
comment|/**      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
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
comment|/**      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
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
comment|/**      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
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
comment|/**      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
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
comment|/**      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
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
comment|/**      * @param text the expression to be evaluated      * @return the builder to continue processing the DSL      */
DECL|method|xqery (String text)
specifier|public
name|T
name|xqery
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|language
argument_list|(
literal|"xqery"
argument_list|,
name|text
argument_list|)
return|;
block|}
comment|/**      * Evaluates a given language name with the expression text      *      * @param languageName the name of the language      * @param text         the expression in the given language      * @return the builder to continue processing the DSL      */
DECL|method|language (String languageName, String text)
specifier|public
name|T
name|language
parameter_list|(
name|String
name|languageName
parameter_list|,
name|String
name|text
parameter_list|)
block|{
name|Expression
name|expression
init|=
name|createExpression
argument_list|(
literal|"el"
argument_list|,
name|text
argument_list|)
decl_stmt|;
comment|// TODO set the exception!
return|return
name|result
return|;
block|}
DECL|method|createExpression (String languageName, String text)
specifier|protected
name|Expression
name|createExpression
parameter_list|(
name|String
name|languageName
parameter_list|,
name|String
name|text
parameter_list|)
block|{
comment|// TODO can we share this code with other places we assert mandatory language names?
name|Language
name|language
init|=
name|camelContext
operator|.
name|resolveLanguage
argument_list|(
name|languageName
argument_list|)
decl_stmt|;
if|if
condition|(
name|language
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Could not resolve language: "
operator|+
name|languageName
argument_list|)
throw|;
block|}
return|return
name|language
operator|.
name|createExpression
argument_list|(
name|text
argument_list|)
return|;
block|}
block|}
end_class

end_unit

