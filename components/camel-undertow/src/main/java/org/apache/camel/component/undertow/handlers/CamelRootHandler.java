begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.undertow.handlers
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|undertow
operator|.
name|handlers
package|;
end_package

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|server
operator|.
name|HttpHandler
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|server
operator|.
name|HttpServerExchange
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|util
operator|.
name|PathTemplate
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|util
operator|.
name|URLUtils
import|;
end_import

begin_comment
comment|/**  * Custom root handler to enable hot swapping individual handlers assigned for each path template and/or HTTP method.  */
end_comment

begin_class
DECL|class|CamelRootHandler
specifier|public
class|class
name|CamelRootHandler
implements|implements
name|HttpHandler
block|{
DECL|field|pathHandler
specifier|private
name|CamelPathHandler
name|pathHandler
decl_stmt|;
DECL|method|CamelRootHandler (HttpHandler defaultHandler)
specifier|public
name|CamelRootHandler
parameter_list|(
name|HttpHandler
name|defaultHandler
parameter_list|)
block|{
name|pathHandler
operator|=
operator|new
name|CamelPathHandler
argument_list|(
name|defaultHandler
argument_list|)
expr_stmt|;
block|}
DECL|method|handleRequest (HttpServerExchange exchange)
specifier|public
name|void
name|handleRequest
parameter_list|(
name|HttpServerExchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|pathHandler
operator|.
name|handleRequest
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|add (String path, String methods, boolean prefixMatch, HttpHandler handler)
specifier|public
specifier|synchronized
name|HttpHandler
name|add
parameter_list|(
name|String
name|path
parameter_list|,
name|String
name|methods
parameter_list|,
name|boolean
name|prefixMatch
parameter_list|,
name|HttpHandler
name|handler
parameter_list|)
block|{
name|String
name|basePath
init|=
name|getBasePath
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|HttpHandler
name|basePathHandler
init|=
name|pathHandler
operator|.
name|getHandler
argument_list|(
name|basePath
argument_list|)
decl_stmt|;
name|CamelMethodHandler
name|targetHandler
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|contains
argument_list|(
literal|"{"
argument_list|)
condition|)
block|{
comment|// Adding a handler for the template path
name|String
name|relativePath
init|=
name|path
operator|.
name|substring
argument_list|(
name|basePath
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|basePathHandler
operator|instanceof
name|CamelPathTemplateHandler
condition|)
block|{
name|CamelPathTemplateHandler
name|templateHandler
init|=
operator|(
name|CamelPathTemplateHandler
operator|)
name|basePathHandler
decl_stmt|;
name|targetHandler
operator|=
name|templateHandler
operator|.
name|get
argument_list|(
name|relativePath
argument_list|)
expr_stmt|;
if|if
condition|(
name|targetHandler
operator|==
literal|null
condition|)
block|{
name|targetHandler
operator|=
operator|new
name|CamelMethodHandler
argument_list|()
expr_stmt|;
name|templateHandler
operator|.
name|add
argument_list|(
name|relativePath
argument_list|,
name|targetHandler
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|CamelPathTemplateHandler
name|templateHandler
decl_stmt|;
if|if
condition|(
name|basePathHandler
operator|instanceof
name|CamelMethodHandler
condition|)
block|{
comment|// A static path handler is already set for the base path. Use it as a default handler
name|templateHandler
operator|=
operator|new
name|CamelPathTemplateHandler
argument_list|(
operator|(
name|CamelMethodHandler
operator|)
name|basePathHandler
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|basePathHandler
operator|==
literal|null
condition|)
block|{
name|templateHandler
operator|=
operator|new
name|CamelPathTemplateHandler
argument_list|(
operator|new
name|CamelMethodHandler
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Unsupported handler '%s' was found"
argument_list|,
name|basePathHandler
argument_list|)
argument_list|)
throw|;
block|}
name|targetHandler
operator|=
operator|new
name|CamelMethodHandler
argument_list|()
expr_stmt|;
name|templateHandler
operator|.
name|add
argument_list|(
name|relativePath
argument_list|,
name|targetHandler
argument_list|)
expr_stmt|;
name|pathHandler
operator|.
name|addPrefixPath
argument_list|(
name|basePath
argument_list|,
name|templateHandler
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// Adding a handler for the static path
if|if
condition|(
name|basePathHandler
operator|instanceof
name|CamelPathTemplateHandler
condition|)
block|{
name|CamelPathTemplateHandler
name|templateHandler
init|=
operator|(
name|CamelPathTemplateHandler
operator|)
name|basePathHandler
decl_stmt|;
if|if
condition|(
operator|!
name|prefixMatch
condition|)
block|{
name|targetHandler
operator|=
name|templateHandler
operator|.
name|getDefault
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Duplicate handlers on a path '%s'"
argument_list|,
name|path
argument_list|)
argument_list|)
throw|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|basePathHandler
operator|instanceof
name|CamelMethodHandler
condition|)
block|{
name|targetHandler
operator|=
operator|(
name|CamelMethodHandler
operator|)
name|basePathHandler
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|basePathHandler
operator|==
literal|null
condition|)
block|{
name|targetHandler
operator|=
operator|new
name|CamelMethodHandler
argument_list|()
expr_stmt|;
if|if
condition|(
name|prefixMatch
condition|)
block|{
name|pathHandler
operator|.
name|addPrefixPath
argument_list|(
name|basePath
argument_list|,
name|targetHandler
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|pathHandler
operator|.
name|addExactPath
argument_list|(
name|basePath
argument_list|,
name|targetHandler
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Unsupported handler '%s' was found"
argument_list|,
name|basePathHandler
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
return|return
name|targetHandler
operator|.
name|add
argument_list|(
name|methods
argument_list|,
name|handler
argument_list|)
return|;
block|}
DECL|method|remove (String path, String methods, boolean prefixMatch)
specifier|public
specifier|synchronized
name|void
name|remove
parameter_list|(
name|String
name|path
parameter_list|,
name|String
name|methods
parameter_list|,
name|boolean
name|prefixMatch
parameter_list|)
block|{
name|String
name|basePath
init|=
name|getBasePath
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|HttpHandler
name|basePathHandler
init|=
name|pathHandler
operator|.
name|getHandler
argument_list|(
name|basePath
argument_list|)
decl_stmt|;
if|if
condition|(
name|basePathHandler
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|path
operator|.
name|contains
argument_list|(
literal|"{"
argument_list|)
condition|)
block|{
comment|// Removing a handler for the template path
name|String
name|relativePath
init|=
name|path
operator|.
name|substring
argument_list|(
name|basePath
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|CamelPathTemplateHandler
name|templateHandler
init|=
operator|(
name|CamelPathTemplateHandler
operator|)
name|basePathHandler
decl_stmt|;
name|CamelMethodHandler
name|targetHandler
init|=
name|templateHandler
operator|.
name|get
argument_list|(
name|relativePath
argument_list|)
decl_stmt|;
if|if
condition|(
name|targetHandler
operator|.
name|remove
argument_list|(
name|methods
argument_list|)
condition|)
block|{
name|templateHandler
operator|.
name|remove
argument_list|(
name|relativePath
argument_list|)
expr_stmt|;
if|if
condition|(
name|templateHandler
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|pathHandler
operator|.
name|removePrefixPath
argument_list|(
name|basePath
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
comment|// Removing a handler for the static path
if|if
condition|(
name|basePathHandler
operator|instanceof
name|CamelPathTemplateHandler
condition|)
block|{
name|String
name|relativePath
init|=
name|path
operator|.
name|substring
argument_list|(
name|basePath
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|CamelPathTemplateHandler
name|templateHandler
init|=
operator|(
name|CamelPathTemplateHandler
operator|)
name|basePathHandler
decl_stmt|;
name|CamelMethodHandler
name|targetHandler
init|=
name|templateHandler
operator|.
name|getDefault
argument_list|()
decl_stmt|;
if|if
condition|(
name|targetHandler
operator|.
name|remove
argument_list|(
name|methods
argument_list|)
condition|)
block|{
name|templateHandler
operator|.
name|remove
argument_list|(
name|relativePath
argument_list|)
expr_stmt|;
if|if
condition|(
name|templateHandler
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|pathHandler
operator|.
name|removePrefixPath
argument_list|(
name|basePath
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|CamelMethodHandler
name|targetHandler
init|=
operator|(
name|CamelMethodHandler
operator|)
name|basePathHandler
decl_stmt|;
if|if
condition|(
name|targetHandler
operator|.
name|remove
argument_list|(
name|methods
argument_list|)
condition|)
block|{
if|if
condition|(
name|prefixMatch
condition|)
block|{
name|pathHandler
operator|.
name|removePrefixPath
argument_list|(
name|basePath
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|pathHandler
operator|.
name|removeExactPath
argument_list|(
name|basePath
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
DECL|method|isEmpty ()
specifier|public
specifier|synchronized
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|pathHandler
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|pathHandler
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getBasePath (String path)
specifier|private
name|String
name|getBasePath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
if|if
condition|(
name|path
operator|.
name|contains
argument_list|(
literal|"{"
argument_list|)
condition|)
block|{
name|path
operator|=
name|PathTemplate
operator|.
name|create
argument_list|(
name|path
argument_list|)
operator|.
name|getBase
argument_list|()
expr_stmt|;
block|}
return|return
name|URLUtils
operator|.
name|normalizeSlashes
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_class

end_unit

