begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|AsyncCallback
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
name|spi
operator|.
name|UnitOfWork
import|;
end_import

begin_comment
comment|/**  * This processor tracks the current {@link RouteContext} while processing the {@link Exchange}.  * This ensures that the {@link Exchange} have details under which route its being currently processed.  */
end_comment

begin_class
annotation|@
name|Deprecated
DECL|class|RouteContextProcessor
specifier|public
class|class
name|RouteContextProcessor
extends|extends
name|DelegateAsyncProcessor
block|{
DECL|field|routeContext
specifier|private
specifier|final
name|RouteContext
name|routeContext
decl_stmt|;
DECL|method|RouteContextProcessor (RouteContext routeContext, Processor processor)
specifier|public
name|RouteContextProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|routeContext
operator|=
name|routeContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|processNext (final Exchange exchange, final AsyncCallback callback)
specifier|protected
name|boolean
name|processNext
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
comment|// push the current route context
specifier|final
name|UnitOfWork
name|unitOfWork
init|=
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
decl_stmt|;
if|if
condition|(
name|unitOfWork
operator|!=
literal|null
condition|)
block|{
name|unitOfWork
operator|.
name|pushRouteContext
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
name|boolean
name|sync
init|=
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
try|try
block|{
comment|// pop the route context we just used
if|if
condition|(
name|unitOfWork
operator|!=
literal|null
condition|)
block|{
name|unitOfWork
operator|.
name|popRouteContext
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|callback
operator|.
name|done
argument_list|(
name|doneSync
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|sync
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
return|return
literal|"RouteContextProcessor["
operator|+
name|processor
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

