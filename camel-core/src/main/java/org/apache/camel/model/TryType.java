begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Endpoint
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
name|TryProcessor
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
name|CatchProcessor
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
name|XmlElement
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
name|XmlElementRef
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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

begin_comment
comment|/**  * @version $Revision: 1.1 $  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"try"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|TryType
specifier|public
class|class
name|TryType
extends|extends
name|ProcessorType
block|{
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|interceptors
specifier|private
name|List
argument_list|<
name|InterceptorRef
argument_list|>
name|interceptors
init|=
operator|new
name|ArrayList
argument_list|<
name|InterceptorRef
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElementRef
DECL|field|catchClauses
specifier|private
name|List
argument_list|<
name|CatchType
argument_list|>
name|catchClauses
init|=
operator|new
name|ArrayList
argument_list|<
name|CatchType
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|finallyClause
specifier|private
name|FinallyType
name|finallyClause
decl_stmt|;
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Try[ "
operator|+
name|getCatchClauses
argument_list|()
operator|+
literal|" "
operator|+
name|getFinallyClause
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
name|Processor
name|tryProcessor
init|=
name|routeContext
operator|.
name|createProcessor
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|Processor
name|finallyProcessor
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|finallyClause
operator|!=
literal|null
condition|)
block|{
name|finallyProcessor
operator|=
name|finallyClause
operator|.
name|createProcessor
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|CatchProcessor
argument_list|>
name|catchProcessors
init|=
operator|new
name|ArrayList
argument_list|<
name|CatchProcessor
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|CatchType
name|catchClause
range|:
name|catchClauses
control|)
block|{
name|catchProcessors
operator|.
name|add
argument_list|(
name|catchClause
operator|.
name|createProcessor
argument_list|(
name|routeContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|TryProcessor
argument_list|(
name|tryProcessor
argument_list|,
name|catchProcessors
argument_list|,
name|finallyProcessor
argument_list|)
return|;
block|}
comment|// Fluent API
comment|//-------------------------------------------------------------------------
DECL|method|when (Class exceptionType)
specifier|public
name|TryType
name|when
parameter_list|(
name|Class
name|exceptionType
parameter_list|)
block|{
name|getCatchClauses
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|CatchType
argument_list|(
name|exceptionType
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|otherwise ()
specifier|public
name|FinallyType
name|otherwise
parameter_list|()
block|{
name|FinallyType
name|answer
init|=
operator|new
name|FinallyType
argument_list|()
decl_stmt|;
name|setFinallyClause
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|to (Endpoint endpoint)
specifier|public
name|TryType
name|to
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|super
operator|.
name|to
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|to (Collection<Endpoint> endpoints)
specifier|public
name|TryType
name|to
parameter_list|(
name|Collection
argument_list|<
name|Endpoint
argument_list|>
name|endpoints
parameter_list|)
block|{
name|super
operator|.
name|to
argument_list|(
name|endpoints
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|to (Endpoint... endpoints)
specifier|public
name|TryType
name|to
parameter_list|(
name|Endpoint
modifier|...
name|endpoints
parameter_list|)
block|{
name|super
operator|.
name|to
argument_list|(
name|endpoints
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|to (String uri)
specifier|public
name|TryType
name|to
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|super
operator|.
name|to
argument_list|(
name|uri
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|to (String... uris)
specifier|public
name|TryType
name|to
parameter_list|(
name|String
modifier|...
name|uris
parameter_list|)
block|{
name|super
operator|.
name|to
argument_list|(
name|uris
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getCatchClauses ()
specifier|public
name|List
argument_list|<
name|CatchType
argument_list|>
name|getCatchClauses
parameter_list|()
block|{
return|return
name|catchClauses
return|;
block|}
DECL|method|setCatchClauses (List<CatchType> catchClauses)
specifier|public
name|void
name|setCatchClauses
parameter_list|(
name|List
argument_list|<
name|CatchType
argument_list|>
name|catchClauses
parameter_list|)
block|{
name|this
operator|.
name|catchClauses
operator|=
name|catchClauses
expr_stmt|;
block|}
DECL|method|getOutputs ()
specifier|public
name|List
argument_list|<
name|ProcessorType
argument_list|>
name|getOutputs
parameter_list|()
block|{
if|if
condition|(
name|finallyClause
operator|!=
literal|null
condition|)
block|{
return|return
name|finallyClause
operator|.
name|getOutputs
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|catchClauses
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|EMPTY_LIST
return|;
block|}
else|else
block|{
name|CatchType
name|when
init|=
name|catchClauses
operator|.
name|get
argument_list|(
name|catchClauses
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
return|return
name|when
operator|.
name|getOutputs
argument_list|()
return|;
block|}
block|}
DECL|method|getFinallyClause ()
specifier|public
name|FinallyType
name|getFinallyClause
parameter_list|()
block|{
return|return
name|finallyClause
return|;
block|}
DECL|method|setFinallyClause (FinallyType finallyClause)
specifier|public
name|void
name|setFinallyClause
parameter_list|(
name|FinallyType
name|finallyClause
parameter_list|)
block|{
name|this
operator|.
name|finallyClause
operator|=
name|finallyClause
expr_stmt|;
block|}
DECL|method|getInterceptors ()
specifier|public
name|List
argument_list|<
name|InterceptorRef
argument_list|>
name|getInterceptors
parameter_list|()
block|{
return|return
name|interceptors
return|;
block|}
DECL|method|setInterceptors (List<InterceptorRef> interceptors)
specifier|public
name|void
name|setInterceptors
parameter_list|(
name|List
argument_list|<
name|InterceptorRef
argument_list|>
name|interceptors
parameter_list|)
block|{
name|this
operator|.
name|interceptors
operator|=
name|interceptors
expr_stmt|;
block|}
block|}
end_class

end_unit

