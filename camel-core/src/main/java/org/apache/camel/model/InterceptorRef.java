begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|Processor
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
name|impl
operator|.
name|RouteContext
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
name|processor
operator|.
name|DelegateProcessor
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"interceptor"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|InterceptorRef
specifier|public
class|class
name|InterceptorRef
extends|extends
name|InterceptorType
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|ref
specifier|private
name|String
name|ref
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|interceptor
specifier|private
name|DelegateProcessor
name|interceptor
decl_stmt|;
DECL|method|InterceptorRef ()
specifier|public
name|InterceptorRef
parameter_list|()
block|{     }
DECL|method|InterceptorRef (String ref)
specifier|public
name|InterceptorRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|setRef
argument_list|(
name|ref
argument_list|)
expr_stmt|;
block|}
DECL|method|InterceptorRef (DelegateProcessor interceptor)
specifier|public
name|InterceptorRef
parameter_list|(
name|DelegateProcessor
name|interceptor
parameter_list|)
block|{
name|this
operator|.
name|interceptor
operator|=
name|interceptor
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Interceptor["
operator|+
name|getLabel
argument_list|()
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
name|DelegateProcessor
name|processor
init|=
name|createInterceptor
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
name|Processor
name|child
init|=
name|createOutputsProcessor
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
name|processor
operator|.
name|setProcessor
argument_list|(
name|child
argument_list|)
expr_stmt|;
return|return
name|processor
return|;
block|}
DECL|method|createInterceptor (RouteContext routeContext)
specifier|public
name|DelegateProcessor
name|createInterceptor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
if|if
condition|(
name|interceptor
operator|==
literal|null
condition|)
block|{
name|interceptor
operator|=
name|routeContext
operator|.
name|lookup
argument_list|(
name|getRef
argument_list|()
argument_list|,
name|DelegateProcessor
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|interceptor
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No DelegateProcessor bean available for reference: "
operator|+
name|getRef
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|interceptor
return|;
block|}
DECL|method|getRef ()
specifier|public
name|String
name|getRef
parameter_list|()
block|{
return|return
name|ref
return|;
block|}
DECL|method|setRef (String ref)
specifier|public
name|void
name|setRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|this
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
block|}
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
return|return
literal|"ref:  "
operator|+
name|ref
return|;
block|}
elseif|else
if|if
condition|(
name|interceptor
operator|!=
literal|null
condition|)
block|{
return|return
name|interceptor
operator|.
name|toString
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|""
return|;
block|}
block|}
block|}
end_class

end_unit

