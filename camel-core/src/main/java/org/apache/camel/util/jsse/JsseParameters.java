begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.jsse
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|jsse
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

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
name|java
operator|.
name|net
operator|.
name|URL
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
name|List
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
name|RuntimeCamelException
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
name|ClassResolver
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
comment|/**  * Base class that provides optional integration with core Camel capabilities.  */
end_comment

begin_class
DECL|class|JsseParameters
specifier|public
class|class
name|JsseParameters
implements|implements
name|CamelContextAware
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
name|JsseParameters
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
comment|/**      * @see #setCamelContext(CamelContext)      */
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
comment|/**      * Sets the optional {@link CamelContext} used for integration with core capabilities      * such as Camel Property Placeholders and {@link ClassResolver}.      *      * @param context the context to use      */
DECL|method|setCamelContext (CamelContext context)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
comment|/**      * Parses the value using the Camel Property Placeholder capabilities if      * a context is provided.  Otherwise returns {@code value} as is.      *      * @param value the string to replace property tokens in      * @return the value      *       * @throws RuntimeCamelException if property placeholders were used and there was an error resolving them      *      * @see #setCamelContext(CamelContext)      */
DECL|method|parsePropertyValue (String value)
specifier|protected
name|String
name|parsePropertyValue
parameter_list|(
name|String
name|value
parameter_list|)
throws|throws
name|RuntimeCamelException
block|{
if|if
condition|(
name|this
operator|.
name|getCamelContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|this
operator|.
name|getCamelContext
argument_list|()
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|value
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Error parsing property value: "
operator|+
name|value
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
return|return
name|value
return|;
block|}
block|}
comment|/**      * Parses the values using the Camel Property Placeholder capabilities if      * a context is provided.  Otherwise returns {@code values} as is.      *      * @param values the list of strings to replace property tokens in      * @return the list of strings      *       * @throws RuntimeCamelException if property placeholders were used and there was an error resolving them      *      * @see #parsePropertyValue(String)      */
DECL|method|parsePropertyValues (List<String> values)
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|parsePropertyValues
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|values
parameter_list|)
throws|throws
name|RuntimeCamelException
block|{
if|if
condition|(
name|this
operator|.
name|getCamelContext
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|values
return|;
block|}
else|else
block|{
name|List
argument_list|<
name|String
argument_list|>
name|parsedValues
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|values
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|value
range|:
name|values
control|)
block|{
name|parsedValues
operator|.
name|add
argument_list|(
name|this
operator|.
name|parsePropertyValue
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|parsedValues
return|;
block|}
block|}
comment|/**      * Attempts to loads a resource using a number of different approaches.      * The loading of the resource, is attempted by treating the resource as a file path,      * a class path resource, a URL, and using the Camel Context's {@link ClassResolver}       * if a context is available in that order. An exception is thrown if the resource      * cannot be resolved to readable input stream using any of the above methods.      *       * @param resource the resource location      * @return the input stream for the resource      *      * @throws IOException if the resource cannot be resolved using any of the above methods      *       * @see #setCamelContext(CamelContext)      */
DECL|method|resolveResource (String resource)
specifier|protected
name|InputStream
name|resolveResource
parameter_list|(
name|String
name|resource
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
try|try
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Trying to open resource as a file."
argument_list|)
expr_stmt|;
name|is
operator|=
operator|new
name|FileInputStream
argument_list|(
name|resource
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Loaded resource as file {}"
argument_list|,
name|resource
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Could not open resource as a file."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|is
operator|==
literal|null
operator|&&
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Trying to open resource as a class path resource with the TCCL {}."
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|is
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|resource
argument_list|)
expr_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Could not open resource as a class path resource using the TCCL {}."
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Loaded resource from TCCL ClassLoader {}"
argument_list|,
name|resource
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Trying to open resource as a class path resource using the classloader {}."
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|is
operator|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|resource
argument_list|)
expr_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Could not open resource as a class path resource using the classloader {}."
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Loaded resource from JsseParameter ClassLoader {}"
argument_list|,
name|resource
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Trying to open resource as a URL."
argument_list|)
expr_stmt|;
name|is
operator|=
operator|new
name|URL
argument_list|(
name|resource
argument_list|)
operator|.
name|openStream
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Loaded resource as URL {}"
argument_list|,
name|resource
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Could not open resource as a URL"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|is
operator|==
literal|null
operator|&&
name|this
operator|.
name|context
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Trying to open resource using the CamelContext ClassResolver {}"
argument_list|,
name|context
operator|.
name|getClassResolver
argument_list|()
argument_list|)
expr_stmt|;
name|is
operator|=
name|context
operator|.
name|getClassResolver
argument_list|()
operator|.
name|loadResourceAsStream
argument_list|(
name|resource
argument_list|)
expr_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Could not to open resource using the CamelContext ClassResolver {}."
argument_list|,
name|context
operator|.
name|getClassResolver
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Loaded resource using the CamelContext ClassResolver {}"
argument_list|,
name|resource
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Could not open "
operator|+
name|resource
operator|+
literal|" as a file, class path resource, or URL."
argument_list|)
throw|;
block|}
return|return
name|is
return|;
block|}
block|}
end_class

end_unit

