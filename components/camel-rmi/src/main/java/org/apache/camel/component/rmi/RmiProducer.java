begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|rmi
operator|.
name|NotBoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|RemoteException
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
name|component
operator|.
name|bean
operator|.
name|BeanHolder
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
name|BeanProcessor
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|RmiProducer
specifier|public
class|class
name|RmiProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|beanProcessor
specifier|private
name|BeanProcessor
name|beanProcessor
decl_stmt|;
DECL|method|RmiProducer (RmiEndpoint endpoint)
specifier|public
name|RmiProducer
parameter_list|(
name|RmiEndpoint
name|endpoint
parameter_list|)
throws|throws
name|RemoteException
throws|,
name|NotBoundException
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|BeanHolder
name|holder
init|=
operator|new
name|RmiRegistryBean
argument_list|(
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getName
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getRegistry
argument_list|()
argument_list|)
decl_stmt|;
name|beanProcessor
operator|=
operator|new
name|BeanProcessor
argument_list|(
name|holder
argument_list|)
expr_stmt|;
name|String
name|method
init|=
name|endpoint
operator|.
name|getMethod
argument_list|()
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
name|beanProcessor
operator|.
name|setMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
block|}
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
name|beanProcessor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

