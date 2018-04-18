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
name|HttpServerExchange
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
name|PathTemplateHandler
import|;
end_import

begin_comment
comment|/**  * Extended PathTemplateHandler to monitor add/remove handlers. Also this enables hot swapping a default handler.  */
end_comment

begin_class
DECL|class|CamelPathTemplateHandler
specifier|public
class|class
name|CamelPathTemplateHandler
implements|implements
name|HttpHandler
block|{
DECL|field|handlers
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|CamelMethodHandler
argument_list|>
name|handlers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|defaultHandlerWrapper
specifier|private
name|Wrapper
name|defaultHandlerWrapper
init|=
operator|new
name|Wrapper
argument_list|()
decl_stmt|;
DECL|field|delegate
specifier|private
name|PathTemplateHandler
name|delegate
decl_stmt|;
DECL|field|handlerString
specifier|private
name|String
name|handlerString
decl_stmt|;
DECL|method|CamelPathTemplateHandler (CamelMethodHandler defaultHandler)
specifier|public
name|CamelPathTemplateHandler
parameter_list|(
name|CamelMethodHandler
name|defaultHandler
parameter_list|)
block|{
name|defaultHandlerWrapper
operator|.
name|set
argument_list|(
name|defaultHandler
argument_list|)
expr_stmt|;
name|delegate
operator|=
operator|new
name|PathTemplateHandler
argument_list|(
name|defaultHandlerWrapper
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|delegate
operator|.
name|handleRequest
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|add (final String uriTemplate, final CamelMethodHandler handler)
specifier|public
specifier|synchronized
name|CamelPathTemplateHandler
name|add
parameter_list|(
specifier|final
name|String
name|uriTemplate
parameter_list|,
specifier|final
name|CamelMethodHandler
name|handler
parameter_list|)
block|{
name|delegate
operator|.
name|add
argument_list|(
name|uriTemplate
argument_list|,
name|handler
argument_list|)
expr_stmt|;
name|handlers
operator|.
name|put
argument_list|(
name|uriTemplate
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
DECL|method|remove (final String uriTemplate)
specifier|public
specifier|synchronized
name|CamelPathTemplateHandler
name|remove
parameter_list|(
specifier|final
name|String
name|uriTemplate
parameter_list|)
block|{
name|delegate
operator|.
name|remove
argument_list|(
name|uriTemplate
argument_list|)
expr_stmt|;
name|handlers
operator|.
name|remove
argument_list|(
name|uriTemplate
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
DECL|method|get (final String uriTemplate)
specifier|public
name|CamelMethodHandler
name|get
parameter_list|(
specifier|final
name|String
name|uriTemplate
parameter_list|)
block|{
return|return
name|handlers
operator|.
name|get
argument_list|(
name|uriTemplate
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
DECL|method|getDefault ()
specifier|public
name|CamelMethodHandler
name|getDefault
parameter_list|()
block|{
return|return
name|this
operator|.
name|defaultHandlerWrapper
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|setDefault (final CamelMethodHandler defaultHandler)
specifier|public
specifier|synchronized
name|void
name|setDefault
parameter_list|(
specifier|final
name|CamelMethodHandler
name|defaultHandler
parameter_list|)
block|{
name|this
operator|.
name|defaultHandlerWrapper
operator|.
name|set
argument_list|(
name|defaultHandler
argument_list|)
expr_stmt|;
name|handlerString
operator|=
literal|null
expr_stmt|;
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
literal|"CamelPathTemplateHandler[default="
operator|+
name|defaultHandlerWrapper
operator|.
name|get
argument_list|()
operator|+
literal|", "
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
DECL|class|Wrapper
class|class
name|Wrapper
implements|implements
name|HttpHandler
block|{
DECL|field|handler
specifier|private
name|CamelMethodHandler
name|handler
decl_stmt|;
DECL|method|set (CamelMethodHandler handler)
specifier|public
name|void
name|set
parameter_list|(
name|CamelMethodHandler
name|handler
parameter_list|)
block|{
name|this
operator|.
name|handler
operator|=
name|handler
expr_stmt|;
block|}
DECL|method|get ()
specifier|public
name|CamelMethodHandler
name|get
parameter_list|()
block|{
return|return
name|this
operator|.
name|handler
return|;
block|}
annotation|@
name|Override
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
name|handler
operator|.
name|handleRequest
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

