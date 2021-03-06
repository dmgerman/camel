begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|CamelContextAware
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
name|spi
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
name|util
operator|.
name|IOHelper
import|;
end_import

begin_comment
comment|/**  * Base language for {@link Language} implementations.  */
end_comment

begin_class
DECL|class|LanguageSupport
specifier|public
specifier|abstract
class|class
name|LanguageSupport
implements|implements
name|Language
implements|,
name|IsSingleton
implements|,
name|CamelContextAware
block|{
DECL|field|RESOURCE
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE
init|=
literal|"resource:"
decl_stmt|;
DECL|field|SIMPLE_FUNCTION_START
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|SIMPLE_FUNCTION_START
init|=
operator|new
name|String
index|[]
block|{
literal|"${"
block|,
literal|"$simple{"
block|}
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
annotation|@
name|Override
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
annotation|@
name|Override
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
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
return|return
literal|true
return|;
block|}
comment|/**      * Loads the resource if the given expression is referring to an external resource by using      * the syntax<tt>resource:scheme:uri<tt>.      * If the expression is not referring to a resource, then its returned as is.      *<p/>      * For example<tt>resource:classpath:mygroovy.groovy</tt> to refer to a groovy script on the classpath.      *      * @param expression the expression      * @return the expression      * @throws ExpressionIllegalSyntaxException is thrown if error loading the resource      */
DECL|method|loadResource (String expression)
specifier|protected
name|String
name|loadResource
parameter_list|(
name|String
name|expression
parameter_list|)
throws|throws
name|ExpressionIllegalSyntaxException
block|{
if|if
condition|(
name|camelContext
operator|!=
literal|null
operator|&&
name|expression
operator|.
name|startsWith
argument_list|(
name|RESOURCE
argument_list|)
condition|)
block|{
name|String
name|uri
init|=
name|expression
operator|.
name|substring
argument_list|(
name|RESOURCE
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
try|try
block|{
name|is
operator|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|camelContext
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|expression
operator|=
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|is
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ExpressionIllegalSyntaxException
argument_list|(
name|expression
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|expression
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
if|if
condition|(
name|expression
operator|!=
literal|null
condition|)
block|{
return|return
name|expression
operator|.
name|contains
argument_list|(
name|SIMPLE_FUNCTION_START
index|[
literal|0
index|]
argument_list|)
operator|||
name|expression
operator|.
name|contains
argument_list|(
name|SIMPLE_FUNCTION_START
index|[
literal|1
index|]
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

