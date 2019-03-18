begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|event
operator|.
name|Event
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
name|support
operator|.
name|DefaultProducer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|CdiEventProducer
specifier|final
class|class
name|CdiEventProducer
parameter_list|<
name|T
parameter_list|>
extends|extends
name|DefaultProducer
block|{
DECL|field|logger
specifier|private
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CdiEventProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|event
specifier|private
specifier|final
name|Event
argument_list|<
name|T
argument_list|>
name|event
decl_stmt|;
DECL|method|CdiEventProducer (CdiEventEndpoint<T> endpoint, Event<T> event)
name|CdiEventProducer
parameter_list|(
name|CdiEventEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
parameter_list|,
name|Event
argument_list|<
name|T
argument_list|>
name|event
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|event
operator|=
name|event
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
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Firing CDI event [{}] with {}"
argument_list|,
name|event
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// TODO: leverage Camel type converter mechanism based on the endpoint type
comment|// The EventMetadata injection point will be that of the event which is not very useful
comment|// for the end user. Using BeanManager.fire would be a way to hide that internal though
comment|// it will be necessary to check whether the exchange event type is assignable to the
comment|// endpoint event type.
name|event
operator|.
name|fire
argument_list|(
operator|(
name|T
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

