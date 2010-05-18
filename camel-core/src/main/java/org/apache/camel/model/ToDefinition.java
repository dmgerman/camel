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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
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
name|ExchangePattern
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
name|processor
operator|.
name|SendAsyncProcessor
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
name|UnitOfWorkProcessor
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
name|util
operator|.
name|concurrent
operator|.
name|ExecutorServiceHelper
import|;
end_import

begin_comment
comment|/**  * Represents an XML&lt;to/&gt; element  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"to"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|ToDefinition
specifier|public
class|class
name|ToDefinition
extends|extends
name|SendDefinition
argument_list|<
name|ToDefinition
argument_list|>
implements|implements
name|ExecutorServiceAwareDefinition
argument_list|<
name|ToDefinition
argument_list|>
block|{
annotation|@
name|XmlTransient
DECL|field|outputs
specifier|private
specifier|final
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|outputs
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|pattern
specifier|private
name|ExchangePattern
name|pattern
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
annotation|@
name|Deprecated
DECL|field|async
specifier|private
name|Boolean
name|async
init|=
name|Boolean
operator|.
name|FALSE
decl_stmt|;
annotation|@
name|XmlTransient
annotation|@
name|Deprecated
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
annotation|@
name|Deprecated
DECL|field|executorServiceRef
specifier|private
name|String
name|executorServiceRef
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
annotation|@
name|Deprecated
DECL|field|poolSize
specifier|private
name|Integer
name|poolSize
decl_stmt|;
DECL|method|ToDefinition ()
specifier|public
name|ToDefinition
parameter_list|()
block|{     }
DECL|method|ToDefinition (String uri)
specifier|public
name|ToDefinition
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|setUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
DECL|method|ToDefinition (Endpoint endpoint)
specifier|public
name|ToDefinition
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|setEndpoint
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
DECL|method|ToDefinition (String uri, ExchangePattern pattern)
specifier|public
name|ToDefinition
parameter_list|(
name|String
name|uri
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|)
block|{
name|this
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
block|}
DECL|method|ToDefinition (Endpoint endpoint, ExchangePattern pattern)
specifier|public
name|ToDefinition
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|)
block|{
name|this
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getOutputs ()
specifier|public
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|getOutputs
parameter_list|()
block|{
return|return
name|outputs
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
if|if
condition|(
name|async
operator|==
literal|null
operator|||
operator|!
name|async
condition|)
block|{
comment|// when sync then let super create the processor
return|return
name|super
operator|.
name|createProcessor
argument_list|(
name|routeContext
argument_list|)
return|;
block|}
comment|// this code below is only for creating when async is enabled
comment|// ----------------------------------------------------------
comment|// create the child processor which is the async route
name|Processor
name|childProcessor
init|=
name|this
operator|.
name|createChildProcessor
argument_list|(
name|routeContext
argument_list|,
literal|false
argument_list|)
decl_stmt|;
comment|// wrap it in a unit of work so the route that comes next is also done in a unit of work
name|UnitOfWorkProcessor
name|uow
init|=
operator|new
name|UnitOfWorkProcessor
argument_list|(
name|routeContext
argument_list|,
name|childProcessor
argument_list|)
decl_stmt|;
comment|// create async processor
name|Endpoint
name|endpoint
init|=
name|resolveEndpoint
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
comment|// TODO: rework to have configured executor service in SendAsyncProcessor being handled in stop/start scenario
name|SendAsyncProcessor
name|async
init|=
operator|new
name|SendAsyncProcessor
argument_list|(
name|endpoint
argument_list|,
name|getPattern
argument_list|()
argument_list|,
name|uow
argument_list|)
decl_stmt|;
name|executorService
operator|=
name|ExecutorServiceHelper
operator|.
name|getConfiguredExecutorService
argument_list|(
name|routeContext
argument_list|,
literal|"ToAsync"
argument_list|,
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|executorService
operator|!=
literal|null
condition|)
block|{
name|async
operator|.
name|setExecutorService
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|poolSize
operator|!=
literal|null
condition|)
block|{
name|async
operator|.
name|setPoolSize
argument_list|(
name|poolSize
argument_list|)
expr_stmt|;
block|}
return|return
name|async
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|async
operator|!=
literal|null
operator|&&
name|async
condition|)
block|{
return|return
literal|"ToAsync["
operator|+
name|getLabel
argument_list|()
operator|+
literal|"] -> "
operator|+
name|getOutputs
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|"To["
operator|+
name|getLabel
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"to"
return|;
block|}
annotation|@
name|Override
DECL|method|getPattern ()
specifier|public
name|ExchangePattern
name|getPattern
parameter_list|()
block|{
return|return
name|pattern
return|;
block|}
annotation|@
name|Deprecated
DECL|method|isAsync ()
specifier|public
name|Boolean
name|isAsync
parameter_list|()
block|{
return|return
name|async
return|;
block|}
annotation|@
name|Deprecated
DECL|method|setAsync (Boolean async)
specifier|public
name|void
name|setAsync
parameter_list|(
name|Boolean
name|async
parameter_list|)
block|{
name|this
operator|.
name|async
operator|=
name|async
expr_stmt|;
block|}
annotation|@
name|Deprecated
DECL|method|getPoolSize ()
specifier|public
name|Integer
name|getPoolSize
parameter_list|()
block|{
return|return
name|poolSize
return|;
block|}
annotation|@
name|Deprecated
DECL|method|setPoolSize (Integer poolSize)
specifier|public
name|void
name|setPoolSize
parameter_list|(
name|Integer
name|poolSize
parameter_list|)
block|{
name|this
operator|.
name|poolSize
operator|=
name|poolSize
expr_stmt|;
block|}
annotation|@
name|Deprecated
DECL|method|getExecutorService ()
specifier|public
name|ExecutorService
name|getExecutorService
parameter_list|()
block|{
return|return
name|executorService
return|;
block|}
annotation|@
name|Deprecated
DECL|method|setExecutorService (ExecutorService executorService)
specifier|public
name|void
name|setExecutorService
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
block|{
name|this
operator|.
name|executorService
operator|=
name|executorService
expr_stmt|;
block|}
annotation|@
name|Deprecated
DECL|method|getExecutorServiceRef ()
specifier|public
name|String
name|getExecutorServiceRef
parameter_list|()
block|{
return|return
name|executorServiceRef
return|;
block|}
annotation|@
name|Deprecated
DECL|method|setExecutorServiceRef (String executorServiceRef)
specifier|public
name|void
name|setExecutorServiceRef
parameter_list|(
name|String
name|executorServiceRef
parameter_list|)
block|{
name|this
operator|.
name|executorServiceRef
operator|=
name|executorServiceRef
expr_stmt|;
block|}
comment|/**      * Sets the optional {@link ExchangePattern} used to invoke this endpoint      */
DECL|method|setPattern (ExchangePattern pattern)
specifier|public
name|void
name|setPattern
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
block|}
comment|/**      * Sets the optional {@link ExchangePattern} used to invoke this endpoint      */
DECL|method|pattern (ExchangePattern pattern)
specifier|public
name|ToDefinition
name|pattern
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
name|setPattern
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Deprecated
DECL|method|executorService (ExecutorService executorService)
specifier|public
name|ToDefinition
name|executorService
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
block|{
name|setExecutorService
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Deprecated
DECL|method|executorServiceRef (String executorServiceRef)
specifier|public
name|ToDefinition
name|executorServiceRef
parameter_list|(
name|String
name|executorServiceRef
parameter_list|)
block|{
name|setExecutorServiceRef
argument_list|(
name|executorServiceRef
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Setting the core pool size for the underlying {@link java.util.concurrent.ExecutorService}.      *      * @return the builder      */
annotation|@
name|Deprecated
DECL|method|poolSize (int poolSize)
specifier|public
name|ToDefinition
name|poolSize
parameter_list|(
name|int
name|poolSize
parameter_list|)
block|{
name|setPoolSize
argument_list|(
name|poolSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

