begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pojo
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pojo
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
name|NoSuchEndpointException
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
name|Component
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
name|Provider
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationTargetException
import|;
end_import

begin_comment
comment|/**  * Represents a pojo endpoint that uses reflection  * to send messages around.  *  * @version $Revision: 519973 $  */
end_comment

begin_class
DECL|class|PojoEndpoint
specifier|public
class|class
name|PojoEndpoint
extends|extends
name|DefaultEndpoint
argument_list|<
name|PojoExchange
argument_list|>
block|{
DECL|field|pojo
specifier|private
name|Object
name|pojo
decl_stmt|;
DECL|method|PojoEndpoint (String uri, Component component, Object pojo)
specifier|public
name|PojoEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Component
name|component
parameter_list|,
name|Object
name|pojo
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|pojo
operator|=
name|pojo
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
argument_list|<
name|PojoExchange
argument_list|>
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Object
name|pojo
init|=
name|getPojo
argument_list|()
decl_stmt|;
if|if
condition|(
name|pojo
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoPojoAvailableException
argument_list|(
name|this
argument_list|)
throw|;
block|}
return|return
operator|new
name|DefaultProducer
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
block|{
name|PojoExchange
name|pojoExchange
init|=
name|toExchangeType
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|invoke
argument_list|(
name|pojo
argument_list|,
name|pojoExchange
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|copyFrom
argument_list|(
name|pojoExchange
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
argument_list|<
name|PojoExchange
argument_list|>
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"You cannot consume from pojo endpoints."
argument_list|)
throw|;
block|}
comment|/**      * This causes us to invoke the endpoint Pojo using reflection.      *      * @param pojo      */
DECL|method|invoke (Object pojo, PojoExchange exchange)
specifier|public
specifier|static
name|void
name|invoke
parameter_list|(
name|Object
name|pojo
parameter_list|,
name|PojoExchange
name|exchange
parameter_list|)
block|{
name|PojoInvocation
name|invocation
init|=
name|exchange
operator|.
name|getInvocation
argument_list|()
decl_stmt|;
try|try
block|{
name|Object
name|response
init|=
name|invocation
operator|.
name|getMethod
argument_list|()
operator|.
name|invoke
argument_list|(
name|pojo
argument_list|,
name|invocation
operator|.
name|getArgs
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvocationTargetException
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|createExchange ()
specifier|public
name|PojoExchange
name|createExchange
parameter_list|()
block|{
return|return
operator|new
name|PojoExchange
argument_list|(
name|getContext
argument_list|()
argument_list|)
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
DECL|method|getPojo ()
specifier|public
name|Object
name|getPojo
parameter_list|()
block|{
return|return
name|pojo
return|;
block|}
DECL|method|setPojo (Object pojo)
specifier|public
name|void
name|setPojo
parameter_list|(
name|Object
name|pojo
parameter_list|)
block|{
name|this
operator|.
name|pojo
operator|=
name|pojo
expr_stmt|;
block|}
block|}
end_class

end_unit

