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
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|ParameterBindingException
specifier|public
class|class
name|ParameterBindingException
extends|extends
name|RuntimeCamelException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|method
specifier|private
specifier|final
name|Method
name|method
decl_stmt|;
DECL|field|index
specifier|private
specifier|final
name|int
name|index
decl_stmt|;
DECL|field|parameterType
specifier|private
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|parameterType
decl_stmt|;
DECL|field|parameterValue
specifier|private
specifier|final
name|Object
name|parameterValue
decl_stmt|;
DECL|method|ParameterBindingException (Throwable cause, Method method, int index, Class<?> parameterType, Object parameterValue)
specifier|public
name|ParameterBindingException
parameter_list|(
name|Throwable
name|cause
parameter_list|,
name|Method
name|method
parameter_list|,
name|int
name|index
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|parameterType
parameter_list|,
name|Object
name|parameterValue
parameter_list|)
block|{
name|super
argument_list|(
name|createMessage
argument_list|(
name|method
argument_list|,
name|index
argument_list|,
name|parameterType
argument_list|,
name|parameterValue
argument_list|)
argument_list|,
name|cause
argument_list|)
expr_stmt|;
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
name|this
operator|.
name|index
operator|=
name|index
expr_stmt|;
name|this
operator|.
name|parameterType
operator|=
name|parameterType
expr_stmt|;
name|this
operator|.
name|parameterValue
operator|=
name|parameterValue
expr_stmt|;
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
DECL|method|getIndex ()
specifier|public
name|int
name|getIndex
parameter_list|()
block|{
return|return
name|index
return|;
block|}
DECL|method|getParameterType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getParameterType
parameter_list|()
block|{
return|return
name|parameterType
return|;
block|}
DECL|method|getParameterValue ()
specifier|public
name|Object
name|getParameterValue
parameter_list|()
block|{
return|return
name|parameterValue
return|;
block|}
DECL|method|createMessage (Method method, int index, Class<?> parameterType, Object parameterValue)
specifier|private
specifier|static
name|String
name|createMessage
parameter_list|(
name|Method
name|method
parameter_list|,
name|int
name|index
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|parameterType
parameter_list|,
name|Object
name|parameterValue
parameter_list|)
block|{
return|return
literal|"Error during parameter binding on method: "
operator|+
name|method
operator|+
literal|" at parameter #"
operator|+
name|index
operator|+
literal|" with type: "
operator|+
name|parameterType
operator|+
literal|" with value type: "
operator|+
name|ObjectHelper
operator|.
name|type
argument_list|(
name|parameterValue
argument_list|)
operator|+
literal|" and value: "
operator|+
name|parameterValue
return|;
block|}
block|}
end_class

end_unit

