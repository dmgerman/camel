begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
name|handlers
operator|.
name|PathHandler
import|;
end_import

begin_comment
comment|/**  * Extended PathHandler to monitor add/remove handlers.  */
end_comment

begin_class
DECL|class|CamelPathHandler
specifier|public
class|class
name|CamelPathHandler
extends|extends
name|PathHandler
block|{
DECL|field|handlers
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|HttpHandler
argument_list|>
name|handlers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|HttpHandler
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|handlerString
specifier|private
name|String
name|handlerString
decl_stmt|;
DECL|method|CamelPathHandler (HttpHandler defaultHandler)
specifier|public
name|CamelPathHandler
parameter_list|(
name|HttpHandler
name|defaultHandler
parameter_list|)
block|{
name|super
argument_list|(
name|defaultHandler
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addPrefixPath (final String path, final HttpHandler handler)
specifier|public
specifier|synchronized
name|PathHandler
name|addPrefixPath
parameter_list|(
specifier|final
name|String
name|path
parameter_list|,
specifier|final
name|HttpHandler
name|handler
parameter_list|)
block|{
name|super
operator|.
name|addPrefixPath
argument_list|(
name|path
argument_list|,
name|handler
argument_list|)
expr_stmt|;
name|handlers
operator|.
name|put
argument_list|(
name|path
argument_list|,
name|handler
argument_list|)
expr_stmt|;
name|handlerString
operator|=
literal|null
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|addExactPath (final String path, final HttpHandler handler)
specifier|public
specifier|synchronized
name|PathHandler
name|addExactPath
parameter_list|(
specifier|final
name|String
name|path
parameter_list|,
specifier|final
name|HttpHandler
name|handler
parameter_list|)
block|{
name|super
operator|.
name|addExactPath
argument_list|(
name|path
argument_list|,
name|handler
argument_list|)
expr_stmt|;
name|handlers
operator|.
name|put
argument_list|(
name|path
argument_list|,
name|handler
argument_list|)
expr_stmt|;
name|handlerString
operator|=
literal|null
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|removePrefixPath (final String path)
specifier|public
specifier|synchronized
name|PathHandler
name|removePrefixPath
parameter_list|(
specifier|final
name|String
name|path
parameter_list|)
block|{
name|super
operator|.
name|removePrefixPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|handlers
operator|.
name|remove
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|handlerString
operator|=
literal|null
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|removeExactPath (final String path)
specifier|public
specifier|synchronized
name|PathHandler
name|removeExactPath
parameter_list|(
specifier|final
name|String
name|path
parameter_list|)
block|{
name|super
operator|.
name|removeExactPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|handlers
operator|.
name|remove
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|handlerString
operator|=
literal|null
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getHandler (String path)
specifier|public
name|HttpHandler
name|getHandler
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
name|handlers
operator|.
name|get
argument_list|(
name|path
argument_list|)
return|;
block|}
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|handlers
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
if|if
condition|(
name|handlerString
operator|==
literal|null
condition|)
block|{
name|handlerString
operator|=
literal|"CamelPathHandler["
operator|+
name|handlers
operator|+
literal|"]"
expr_stmt|;
block|}
return|return
name|handlerString
return|;
block|}
block|}
end_class

end_unit

