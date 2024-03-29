begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
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
name|spi
operator|.
name|BeanProxyFactory
import|;
end_import

begin_class
DECL|class|DefaultBeanProxyFactory
specifier|public
specifier|final
class|class
name|DefaultBeanProxyFactory
implements|implements
name|BeanProxyFactory
block|{
DECL|method|DefaultBeanProxyFactory ()
specifier|public
name|DefaultBeanProxyFactory
parameter_list|()
block|{     }
annotation|@
name|SafeVarargs
annotation|@
name|Override
DECL|method|createProxy (Endpoint endpoint, boolean binding, Class<T>... interfaceClasses)
specifier|public
specifier|final
parameter_list|<
name|T
parameter_list|>
name|T
name|createProxy
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|boolean
name|binding
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
modifier|...
name|interfaceClasses
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|ProxyHelper
operator|.
name|createProxy
argument_list|(
name|endpoint
argument_list|,
name|binding
argument_list|,
name|interfaceClasses
argument_list|)
return|;
block|}
block|}
end_class

end_unit

