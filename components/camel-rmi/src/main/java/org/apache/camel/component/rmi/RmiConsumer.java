begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rmi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rmi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationHandler
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

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|Proxy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|Remote
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|registry
operator|.
name|Registry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|server
operator|.
name|UnicastRemoteObject
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
name|component
operator|.
name|bean
operator|.
name|BeanExchange
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
name|component
operator|.
name|bean
operator|.
name|BeanInvocation
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

begin_comment
comment|/**  * A {@link Consumer} which uses RMI's {@see UnicastRemoteObject} to consume method invocations.  *  * @version $Revision: 533758 $  */
end_comment

begin_class
DECL|class|RmiConsumer
specifier|public
class|class
name|RmiConsumer
extends|extends
name|DefaultConsumer
argument_list|<
name|BeanExchange
argument_list|>
implements|implements
name|InvocationHandler
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|RmiEndpoint
name|endpoint
decl_stmt|;
DECL|field|stub
specifier|private
name|Remote
name|stub
decl_stmt|;
DECL|field|proxy
specifier|private
name|Remote
name|proxy
decl_stmt|;
DECL|method|RmiConsumer (RmiEndpoint endpoint, Processor processor)
specifier|public
name|RmiConsumer
parameter_list|(
name|RmiEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
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
name|Class
index|[]
name|interfaces
init|=
operator|new
name|Class
index|[
name|endpoint
operator|.
name|getRemoteInterfaces
argument_list|()
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
name|endpoint
operator|.
name|getRemoteInterfaces
argument_list|()
operator|.
name|toArray
argument_list|(
name|interfaces
argument_list|)
expr_stmt|;
name|proxy
operator|=
operator|(
name|Remote
operator|)
name|Proxy
operator|.
name|newProxyInstance
argument_list|(
name|endpoint
operator|.
name|getClassLoader
argument_list|()
argument_list|,
name|interfaces
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|stub
operator|=
name|UnicastRemoteObject
operator|.
name|exportObject
argument_list|(
name|proxy
argument_list|,
name|endpoint
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|Registry
name|registry
init|=
name|endpoint
operator|.
name|getRegistry
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|endpoint
operator|.
name|getName
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
name|name
argument_list|,
name|stub
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// Registration might fail.. clean up..
try|try
block|{
name|UnicastRemoteObject
operator|.
name|unexportObject
argument_list|(
name|stub
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e1
parameter_list|)
block|{ 			}
name|stub
operator|=
literal|null
expr_stmt|;
throw|throw
name|e
throw|;
block|}
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
try|try
block|{
name|Registry
name|registry
init|=
name|endpoint
operator|.
name|getRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|unbind
argument_list|(
name|endpoint
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// do our best to unregister
block|}
name|UnicastRemoteObject
operator|.
name|unexportObject
argument_list|(
name|proxy
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|invoke (Object proxy, Method method, Object[] args)
specifier|public
name|Object
name|invoke
parameter_list|(
name|Object
name|proxy
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|Throwable
block|{
if|if
condition|(
operator|!
name|isStarted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The endpoint is not active: "
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
throw|;
block|}
name|BeanInvocation
name|invocation
init|=
operator|new
name|BeanInvocation
argument_list|(
name|proxy
argument_list|,
name|method
argument_list|,
name|args
argument_list|)
decl_stmt|;
name|BeanExchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setInvocation
argument_list|(
name|invocation
argument_list|)
expr_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|Throwable
name|fault
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
if|if
condition|(
name|fault
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|InvocationTargetException
argument_list|(
name|fault
argument_list|)
throw|;
block|}
return|return
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
return|;
block|}
DECL|method|getProxy ()
specifier|public
name|Remote
name|getProxy
parameter_list|()
block|{
return|return
name|proxy
return|;
block|}
DECL|method|getStub ()
specifier|public
name|Remote
name|getStub
parameter_list|()
block|{
return|return
name|stub
return|;
block|}
block|}
end_class

end_unit

