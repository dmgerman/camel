begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|io
operator|.
name|Externalizable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInput
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutput
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
name|util
operator|.
name|Arrays
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
name|RuntimeCamelException
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
name|converter
operator|.
name|ToStringTypeConverter
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
name|ObjectHelper
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

begin_comment
comment|/**  * Invocation of beans that can handle being serialized.  */
end_comment

begin_class
DECL|class|BeanInvocation
specifier|public
class|class
name|BeanInvocation
implements|implements
name|Externalizable
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|BeanInvocation
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|args
specifier|private
name|Object
index|[]
name|args
decl_stmt|;
DECL|field|methodBean
specifier|private
name|MethodBean
name|methodBean
decl_stmt|;
DECL|field|method
specifier|private
specifier|transient
name|Method
name|method
decl_stmt|;
static|static
block|{
name|ToStringTypeConverter
operator|.
name|registerMissType
argument_list|(
name|BeanInvocation
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|BeanInvocation ()
specifier|public
name|BeanInvocation
parameter_list|()
block|{     }
DECL|method|BeanInvocation (Method method, Object[] args)
specifier|public
name|BeanInvocation
parameter_list|(
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
block|{
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
name|this
operator|.
name|args
operator|=
name|args
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|Object
name|list
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|args
operator|!=
literal|null
condition|)
block|{
name|list
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
return|return
literal|"BeanInvocation "
operator|+
name|method
operator|+
literal|" with "
operator|+
name|list
operator|+
literal|"]"
return|;
block|}
DECL|method|getArgs ()
specifier|public
name|Object
index|[]
name|getArgs
parameter_list|()
block|{
return|return
name|args
return|;
block|}
DECL|method|getMethod ()
specifier|public
name|Method
name|getMethod
parameter_list|()
block|{
return|return
name|method
return|;
block|}
DECL|method|setMethod (Method method)
specifier|public
name|void
name|setMethod
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
block|}
DECL|method|setArgs (Object[] args)
specifier|public
name|void
name|setArgs
parameter_list|(
name|Object
index|[]
name|args
parameter_list|)
block|{
name|this
operator|.
name|args
operator|=
name|args
expr_stmt|;
block|}
comment|/**      * This causes us to invoke the endpoint Pojo using reflection.      *      * @param pojo     the bean on which to perform this invocation      * @param exchange the exchange carrying the method invocation      */
DECL|method|invoke (Object pojo, Exchange exchange)
specifier|public
name|void
name|invoke
parameter_list|(
name|Object
name|pojo
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
name|Method
name|method
init|=
name|getMethod
argument_list|()
decl_stmt|;
name|Object
index|[]
name|args
init|=
name|getArgs
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Invoking method: {} with args: {}"
argument_list|,
name|method
argument_list|,
name|args
argument_list|)
expr_stmt|;
name|Object
name|response
init|=
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|ObjectHelper
operator|.
name|invokeMethodSafe
argument_list|(
name|method
argument_list|,
name|pojo
argument_list|,
name|args
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Got response: {}"
argument_list|,
name|response
argument_list|)
expr_stmt|;
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
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|readExternal (ObjectInput objectInput)
specifier|public
name|void
name|readExternal
parameter_list|(
name|ObjectInput
name|objectInput
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|methodBean
operator|=
name|ObjectHelper
operator|.
name|cast
argument_list|(
name|MethodBean
operator|.
name|class
argument_list|,
name|objectInput
operator|.
name|readObject
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|method
operator|=
name|methodBean
operator|.
name|getMethod
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|args
operator|=
name|ObjectHelper
operator|.
name|cast
argument_list|(
name|Object
index|[]
operator|.
expr|class
argument_list|,
name|objectInput
operator|.
name|readObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|writeExternal (ObjectOutput objectOutput)
specifier|public
name|void
name|writeExternal
parameter_list|(
name|ObjectOutput
name|objectOutput
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|methodBean
operator|==
literal|null
condition|)
block|{
name|methodBean
operator|=
operator|new
name|MethodBean
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
name|objectOutput
operator|.
name|writeObject
argument_list|(
name|methodBean
argument_list|)
expr_stmt|;
name|objectOutput
operator|.
name|writeObject
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

