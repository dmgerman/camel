begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.hessian
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|hessian
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_comment
comment|/** A simple object used used by {@link HessianDataFormatMarshallingTest}. */
end_comment

begin_class
DECL|class|AnotherObject
class|class
name|AnotherObject
implements|implements
name|Serializable
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
DECL|field|bool
specifier|private
name|boolean
name|bool
decl_stmt|;
DECL|field|intNumber
specifier|private
name|int
name|intNumber
decl_stmt|;
DECL|method|isBool ()
specifier|public
name|boolean
name|isBool
parameter_list|()
block|{
return|return
name|bool
return|;
block|}
DECL|method|setBool (final boolean bool)
specifier|public
name|void
name|setBool
parameter_list|(
specifier|final
name|boolean
name|bool
parameter_list|)
block|{
name|this
operator|.
name|bool
operator|=
name|bool
expr_stmt|;
block|}
DECL|method|getIntNumber ()
specifier|public
name|int
name|getIntNumber
parameter_list|()
block|{
return|return
name|intNumber
return|;
block|}
DECL|method|setIntNumber (final int intNumber)
specifier|public
name|void
name|setIntNumber
parameter_list|(
specifier|final
name|int
name|intNumber
parameter_list|)
block|{
name|this
operator|.
name|intNumber
operator|=
name|intNumber
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
specifier|final
name|int
name|prime
init|=
literal|31
decl_stmt|;
name|int
name|result
init|=
literal|1
decl_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
operator|(
name|bool
condition|?
literal|1231
else|:
literal|1237
operator|)
expr_stmt|;
name|result
operator|=
name|prime
operator|*
name|result
operator|+
name|intNumber
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|obj
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|getClass
argument_list|()
operator|!=
name|obj
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|AnotherObject
name|other
init|=
operator|(
name|AnotherObject
operator|)
name|obj
decl_stmt|;
if|if
condition|(
name|bool
operator|!=
name|other
operator|.
name|bool
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|intNumber
operator|!=
name|other
operator|.
name|intNumber
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
block|}
end_class

end_unit

