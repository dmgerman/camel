begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.exceptionpolicy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|exceptionpolicy
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
name|model
operator|.
name|WhenDefinition
import|;
end_import

begin_comment
comment|/**  * Exception policy key is a compound key for storing:  *<b>exception class</b> +<b>when</b> =><b>exception type</b>.  *<p/>  * This is used by Camel to store the onException types configued that has or has not predicates attached (when).  */
end_comment

begin_class
DECL|class|ExceptionPolicyKey
specifier|public
specifier|final
class|class
name|ExceptionPolicyKey
block|{
DECL|field|exceptionClass
specifier|private
specifier|final
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
name|exceptionClass
decl_stmt|;
DECL|field|when
specifier|private
specifier|final
name|WhenDefinition
name|when
decl_stmt|;
DECL|method|ExceptionPolicyKey (Class<? extends Throwable> exceptionClass, WhenDefinition when)
specifier|public
name|ExceptionPolicyKey
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
name|exceptionClass
parameter_list|,
name|WhenDefinition
name|when
parameter_list|)
block|{
name|this
operator|.
name|exceptionClass
operator|=
name|exceptionClass
expr_stmt|;
name|this
operator|.
name|when
operator|=
name|when
expr_stmt|;
block|}
DECL|method|getExceptionClass ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getExceptionClass
parameter_list|()
block|{
return|return
name|exceptionClass
return|;
block|}
DECL|method|getWhen ()
specifier|public
name|WhenDefinition
name|getWhen
parameter_list|()
block|{
return|return
name|when
return|;
block|}
DECL|method|newInstance (Class<? extends Throwable> exceptionClass)
specifier|public
specifier|static
name|ExceptionPolicyKey
name|newInstance
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
name|exceptionClass
parameter_list|)
block|{
return|return
operator|new
name|ExceptionPolicyKey
argument_list|(
name|exceptionClass
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|newInstance (Class<? extends Throwable> exceptionClass, WhenDefinition when)
specifier|public
specifier|static
name|ExceptionPolicyKey
name|newInstance
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
name|exceptionClass
parameter_list|,
name|WhenDefinition
name|when
parameter_list|)
block|{
return|return
operator|new
name|ExceptionPolicyKey
argument_list|(
name|exceptionClass
argument_list|,
name|when
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ExceptionPolicyKey
name|that
init|=
operator|(
name|ExceptionPolicyKey
operator|)
name|o
decl_stmt|;
if|if
condition|(
operator|!
name|exceptionClass
operator|.
name|equals
argument_list|(
name|that
operator|.
name|exceptionClass
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|when
operator|!=
literal|null
condition|?
operator|!
name|when
operator|.
name|equals
argument_list|(
name|that
operator|.
name|when
argument_list|)
else|:
name|that
operator|.
name|when
operator|!=
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
name|exceptionClass
operator|.
name|hashCode
argument_list|()
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|when
operator|!=
literal|null
condition|?
name|when
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
return|return
name|result
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
literal|"ExceptionPolicyKey["
operator|+
name|exceptionClass
operator|+
operator|(
name|when
operator|!=
literal|null
condition|?
literal|" "
operator|+
name|when
else|:
literal|""
operator|)
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

