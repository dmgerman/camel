begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jbi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jbi
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
name|Consumer
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
name|DefaultConsumer
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
name|DefaultEndpoint
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

begin_comment
comment|/**  * Represents an {@link Endpoint} for interacting with JBI  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|JbiEndpoint
specifier|public
class|class
name|JbiEndpoint
extends|extends
name|DefaultEndpoint
argument_list|<
name|Exchange
argument_list|>
block|{
DECL|field|toJbiProcessor
specifier|private
name|Processor
argument_list|<
name|Exchange
argument_list|>
name|toJbiProcessor
decl_stmt|;
DECL|field|jbiComponent
specifier|private
specifier|final
name|CamelJbiComponent
name|jbiComponent
decl_stmt|;
DECL|method|JbiEndpoint (CamelJbiComponent jbiComponent, String uri)
specifier|public
name|JbiEndpoint
parameter_list|(
name|CamelJbiComponent
name|jbiComponent
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|jbiComponent
argument_list|)
expr_stmt|;
name|this
operator|.
name|jbiComponent
operator|=
name|jbiComponent
expr_stmt|;
name|toJbiProcessor
operator|=
operator|new
name|ToJbiProcessor
argument_list|(
name|jbiComponent
operator|.
name|getBinding
argument_list|()
argument_list|,
name|jbiComponent
operator|.
name|getComponentContext
argument_list|()
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
argument_list|<
name|Exchange
argument_list|>
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|startService
argument_list|(
operator|new
name|DefaultProducer
argument_list|<
name|Exchange
argument_list|>
argument_list|(
name|this
argument_list|)
block|{
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
name|toJbiProcessor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|createConsumer (final Processor<Exchange> processor)
specifier|public
name|Consumer
argument_list|<
name|Exchange
argument_list|>
name|createConsumer
parameter_list|(
specifier|final
name|Processor
argument_list|<
name|Exchange
argument_list|>
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|startService
argument_list|(
operator|new
name|DefaultConsumer
argument_list|<
name|Exchange
argument_list|>
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
block|{
name|CamelJbiEndpoint
name|jbiEndpoint
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|jbiEndpoint
operator|=
name|jbiComponent
operator|.
name|activateJbiEndpoint
argument_list|(
name|JbiEndpoint
operator|.
name|this
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|/*                 if (jbiEndpoint != null) {                     jbiEndpoint.deactivate();                 } */
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|createExchange ()
specifier|public
name|JbiExchange
name|createExchange
parameter_list|()
block|{
return|return
operator|new
name|JbiExchange
argument_list|(
name|getContext
argument_list|()
argument_list|,
name|getBinding
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getBinding ()
specifier|public
name|JbiBinding
name|getBinding
parameter_list|()
block|{
return|return
name|jbiComponent
operator|.
name|getBinding
argument_list|()
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

