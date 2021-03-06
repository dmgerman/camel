begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dozer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dozer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|support
operator|.
name|ResourceHelper
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
comment|/**  * Provides support for mapping a Camel expression to a target field in a   * mapping.  Expressions have the following format:  *<br><br>  * [language]:[expression]  *<br><br>  */
end_comment

begin_class
DECL|class|ExpressionMapper
specifier|public
class|class
name|ExpressionMapper
extends|extends
name|BaseConverter
block|{
DECL|field|currentExchange
specifier|private
name|ThreadLocal
argument_list|<
name|Exchange
argument_list|>
name|currentExchange
init|=
operator|new
name|ThreadLocal
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|convert (Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass)
specifier|public
name|Object
name|convert
parameter_list|(
name|Object
name|existingDestinationFieldValue
parameter_list|,
name|Object
name|sourceFieldValue
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|destinationClass
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|sourceClass
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|currentExchange
operator|.
name|get
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Current exchange has not been set for ExpressionMapper"
argument_list|)
throw|;
block|}
name|Expression
name|exp
decl_stmt|;
comment|// Resolve the language being used for this expression and evaluate
name|Exchange
name|exchange
init|=
name|currentExchange
operator|.
name|get
argument_list|()
decl_stmt|;
name|Language
name|expLang
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|resolveLanguage
argument_list|(
name|getLanguagePart
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|scheme
init|=
name|getSchemePart
argument_list|()
decl_stmt|;
if|if
condition|(
name|scheme
operator|!=
literal|null
operator|&&
operator|(
name|scheme
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"classpath"
argument_list|)
operator|||
name|scheme
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"file"
argument_list|)
operator|||
name|scheme
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"http"
argument_list|)
operator|)
condition|)
block|{
name|String
name|path
init|=
name|getPathPart
argument_list|()
decl_stmt|;
try|try
block|{
name|exp
operator|=
name|expLang
operator|.
name|createExpression
argument_list|(
name|resolveScript
argument_list|(
name|scheme
operator|+
literal|":"
operator|+
name|path
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Expression script specified but not found"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|exp
operator|=
name|expLang
operator|.
name|createExpression
argument_list|(
name|getExpressionPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|destinationClass
argument_list|)
return|;
block|}
finally|finally
block|{
name|done
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Resolves the script.      *      * @param script script or uri for a script to load      * @return the script      * @throws IOException is thrown if error loading the script      */
DECL|method|resolveScript (String script)
specifier|protected
name|String
name|resolveScript
parameter_list|(
name|String
name|script
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|answer
decl_stmt|;
if|if
condition|(
name|ResourceHelper
operator|.
name|hasScheme
argument_list|(
name|script
argument_list|)
condition|)
block|{
name|InputStream
name|is
init|=
name|loadResource
argument_list|(
name|script
argument_list|)
decl_stmt|;
name|answer
operator|=
name|currentExchange
operator|.
name|get
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
name|String
operator|.
name|class
argument_list|,
name|is
argument_list|)
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|script
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Loads the given resource.      *      * @param uri uri of the resource.      * @return the loaded resource      * @throws IOException is thrown if resource is not found or cannot be loaded      */
DECL|method|loadResource (String uri)
specifier|protected
name|InputStream
name|loadResource
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|currentExchange
operator|.
name|get
argument_list|()
operator|.
name|getContext
argument_list|()
argument_list|,
name|uri
argument_list|)
return|;
block|}
comment|/**      * Used as the source field for Dozer mappings.       */
DECL|method|getExpression ()
specifier|public
name|String
name|getExpression
parameter_list|()
block|{
return|return
name|getParameter
argument_list|()
return|;
block|}
comment|/**      * The actual expression, without the language prefix.      */
DECL|method|getExpressionPart ()
specifier|public
name|String
name|getExpressionPart
parameter_list|()
block|{
return|return
name|getParameter
argument_list|()
operator|.
name|substring
argument_list|(
name|getParameter
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
operator|+
literal|1
argument_list|)
return|;
block|}
comment|/**      * The expression language used for this mapping.      */
DECL|method|getLanguagePart ()
specifier|public
name|String
name|getLanguagePart
parameter_list|()
block|{
return|return
name|getParameter
argument_list|()
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|getParameter
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Sets the Camel exchange reference for this mapping.  The exchange       * reference is stored in a thread-local which is cleaned up after the       * mapping has been performed via the done() method.      * @param exchange      */
DECL|method|setCurrentExchange (Exchange exchange)
specifier|public
name|void
name|setCurrentExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|currentExchange
operator|.
name|set
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|/**      * The scheme used for this mapping's resource file (classpath, file, http).      */
DECL|method|getSchemePart ()
specifier|public
name|String
name|getSchemePart
parameter_list|()
block|{
return|return
name|getParameterPart
argument_list|(
literal|":"
argument_list|,
literal|1
argument_list|)
return|;
block|}
comment|/**      * The path used for this mapping's resource file.      */
DECL|method|getPathPart ()
specifier|public
name|String
name|getPathPart
parameter_list|()
block|{
return|return
name|getParameterPart
argument_list|(
literal|":"
argument_list|,
literal|2
argument_list|)
return|;
block|}
comment|/*      * Parse the URI to get at one of the parameters.      * @param separator      * @param idx      * @return      */
DECL|method|getParameterPart (String separator, int idx)
specifier|private
name|String
name|getParameterPart
parameter_list|(
name|String
name|separator
parameter_list|,
name|int
name|idx
parameter_list|)
block|{
name|String
name|part
init|=
literal|null
decl_stmt|;
name|String
index|[]
name|parts
init|=
name|getParameter
argument_list|()
operator|.
name|split
argument_list|(
name|separator
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|>
name|idx
condition|)
block|{
name|part
operator|=
name|parts
index|[
name|idx
index|]
expr_stmt|;
block|}
return|return
name|part
return|;
block|}
block|}
end_class

end_unit

