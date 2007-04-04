begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Service
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
name|ServiceSupport
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|SendProcessor
specifier|public
class|class
name|SendProcessor
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
extends|extends
name|ServiceSupport
implements|implements
name|Processor
argument_list|<
name|E
argument_list|>
implements|,
name|Service
block|{
DECL|field|destination
specifier|private
name|Endpoint
argument_list|<
name|E
argument_list|>
name|destination
decl_stmt|;
DECL|field|producer
specifier|private
name|Producer
argument_list|<
name|E
argument_list|>
name|producer
decl_stmt|;
DECL|method|SendProcessor (Endpoint<E> destination)
specifier|public
name|SendProcessor
parameter_list|(
name|Endpoint
argument_list|<
name|E
argument_list|>
name|destination
parameter_list|)
block|{
name|this
operator|.
name|destination
operator|=
name|destination
expr_stmt|;
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|producer
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|producer
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|producer
operator|=
name|destination
operator|.
name|createProducer
argument_list|()
expr_stmt|;
block|}
DECL|method|process (E exchange)
specifier|public
name|void
name|process
parameter_list|(
name|E
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|producer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"No producer, this processor has not been started!"
argument_list|)
throw|;
block|}
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|getDestination ()
specifier|public
name|Endpoint
argument_list|<
name|E
argument_list|>
name|getDestination
parameter_list|()
block|{
return|return
name|destination
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
literal|"sendTo("
operator|+
name|destination
operator|+
literal|")"
return|;
block|}
block|}
end_class

end_unit

