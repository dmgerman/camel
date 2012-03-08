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
name|Route
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
name|InflightRepository
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

begin_comment
comment|/**  * This processor tracks the current {@link RouteContext} while processing the {@link Exchange}.  * This ensures that the {@link Exchange} have details under which route its being currently processed.  */
end_comment

begin_class
DECL|class|RouteInflightRepositoryProcessor
specifier|public
class|class
name|RouteInflightRepositoryProcessor
extends|extends
name|DelegateAsyncProcessor
block|{
DECL|field|inflightRepository
specifier|private
specifier|final
name|InflightRepository
name|inflightRepository
decl_stmt|;
DECL|field|route
specifier|private
name|Route
name|route
decl_stmt|;
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|method|RouteInflightRepositoryProcessor (InflightRepository inflightRepository, Processor processor)
specifier|public
name|RouteInflightRepositoryProcessor
parameter_list|(
name|InflightRepository
name|inflightRepository
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
name|inflightRepository
operator|=
name|inflightRepository
expr_stmt|;
block|}
DECL|method|setRoute (Route route)
specifier|public
name|void
name|setRoute
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
name|this
operator|.
name|route
operator|=
name|route
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|route
operator|.
name|getId
argument_list|()
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
name|inflightRepository
operator|.
name|add
argument_list|(
name|exchange
argument_list|,
name|id
argument_list|)
expr_stmt|;
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
name|inflightRepository
operator|.
name|remove
argument_list|(
name|exchange
argument_list|,
name|id
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
name|super
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

