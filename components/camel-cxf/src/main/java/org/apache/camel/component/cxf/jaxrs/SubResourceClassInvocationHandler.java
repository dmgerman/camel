begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.jaxrs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|jaxrs
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
name|Constructor
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|utils
operator|.
name|ResourceUtils
import|;
end_import

begin_comment
comment|// This class only return the sub class instance
end_comment

begin_class
DECL|class|SubResourceClassInvocationHandler
specifier|public
class|class
name|SubResourceClassInvocationHandler
implements|implements
name|InvocationHandler
block|{
annotation|@
name|Override
DECL|method|invoke (Object proxy, Method method, Object[] parameters)
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
name|parameters
parameter_list|)
throws|throws
name|Throwable
block|{
name|Object
name|result
init|=
literal|null
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|returnType
init|=
name|method
operator|.
name|getReturnType
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|returnType
operator|.
name|isAssignableFrom
argument_list|(
name|Void
operator|.
name|class
argument_list|)
condition|)
block|{
comment|// create a instance to return
if|if
condition|(
name|returnType
operator|.
name|isInterface
argument_list|()
condition|)
block|{
comment|// create a new proxy for it
name|result
operator|=
name|Proxy
operator|.
name|newProxyInstance
argument_list|(
name|returnType
operator|.
name|getClassLoader
argument_list|()
argument_list|,
operator|new
name|Class
index|[]
block|{
name|returnType
block|}
argument_list|,
operator|new
name|SubResourceClassInvocationHandler
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// get the constructor and create a new instance
name|Constructor
argument_list|<
name|?
argument_list|>
name|c
init|=
name|ResourceUtils
operator|.
name|findResourceConstructor
argument_list|(
name|returnType
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|result
operator|=
name|c
operator|.
name|newInstance
argument_list|(
operator|new
name|Object
index|[]
block|{}
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

