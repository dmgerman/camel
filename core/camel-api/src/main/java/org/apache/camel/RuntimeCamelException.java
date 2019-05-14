begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_comment
comment|/**  * Base class for all Camel unchecked exceptions.  */
end_comment

begin_class
DECL|class|RuntimeCamelException
specifier|public
class|class
name|RuntimeCamelException
extends|extends
name|RuntimeException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|8046489554418284257L
decl_stmt|;
DECL|method|RuntimeCamelException ()
specifier|public
name|RuntimeCamelException
parameter_list|()
block|{     }
DECL|method|RuntimeCamelException (String message)
specifier|public
name|RuntimeCamelException
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|RuntimeCamelException (String message, Throwable cause)
specifier|public
name|RuntimeCamelException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
DECL|method|RuntimeCamelException (Throwable cause)
specifier|public
name|RuntimeCamelException
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
comment|/**      * Wraps the caused exception in a {@link RuntimeCamelException} if its not      * already such an exception.      *      * @param e the caused exception      * @return the wrapper exception      */
DECL|method|wrapRuntimeCamelException (Throwable e)
specifier|public
specifier|static
name|RuntimeCamelException
name|wrapRuntimeCamelException
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|instanceof
name|RuntimeCamelException
condition|)
block|{
comment|// don't double wrap
return|return
operator|(
name|RuntimeCamelException
operator|)
name|e
return|;
block|}
else|else
block|{
return|return
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
return|;
block|}
block|}
comment|/**      * Wraps the caused exception in a {@link RuntimeCamelException} if its not      * already a runtime exception.      *      * @param e the caused exception      * @return the wrapper exception      */
DECL|method|wrapRuntimeException (Throwable e)
specifier|public
specifier|static
name|RuntimeException
name|wrapRuntimeException
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|instanceof
name|RuntimeException
condition|)
block|{
comment|// don't double wrap
return|return
operator|(
name|RuntimeException
operator|)
name|e
return|;
block|}
else|else
block|{
return|return
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

