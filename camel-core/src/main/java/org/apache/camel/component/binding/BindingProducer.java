begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.binding
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|binding
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
name|Producer
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
name|DefaultProducer
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
name|ServiceHelper
import|;
end_import

begin_comment
comment|/**  * A {@link Producer} which applies a {@link org.apache.camel.spi.Binding} before invoking the underlying {@link Producer} on the {@link Endpoint}  */
end_comment

begin_class
DECL|class|BindingProducer
specifier|public
class|class
name|BindingProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|BindingEndpoint
name|endpoint
decl_stmt|;
DECL|field|bindingProcessor
specifier|private
specifier|final
name|Processor
name|bindingProcessor
decl_stmt|;
DECL|field|delegateProducer
specifier|private
specifier|final
name|Producer
name|delegateProducer
decl_stmt|;
DECL|method|BindingProducer (BindingEndpoint endpoint)
specifier|public
name|BindingProducer
parameter_list|(
name|BindingEndpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|bindingProcessor
operator|=
name|endpoint
operator|.
name|getBinding
argument_list|()
operator|.
name|createProduceProcessor
argument_list|()
expr_stmt|;
name|delegateProducer
operator|=
name|endpoint
operator|.
name|getDelegate
argument_list|()
operator|.
name|createProducer
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|pipelineBindingProcessor
argument_list|(
name|bindingProcessor
argument_list|,
name|exchange
argument_list|,
name|delegateProducer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|bindingProcessor
argument_list|,
name|delegateProducer
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|delegateProducer
argument_list|,
name|bindingProcessor
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

