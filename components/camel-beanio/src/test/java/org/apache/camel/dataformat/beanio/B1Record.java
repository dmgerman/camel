begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.beanio
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|beanio
package|;
end_package

begin_class
DECL|class|B1Record
specifier|public
class|class
name|B1Record
extends|extends
name|Record
block|{
DECL|field|securityName
name|String
name|securityName
decl_stmt|;
DECL|method|B1Record ()
specifier|public
name|B1Record
parameter_list|()
block|{     }
DECL|method|B1Record (String sedol, String source, String securityName)
specifier|public
name|B1Record
parameter_list|(
name|String
name|sedol
parameter_list|,
name|String
name|source
parameter_list|,
name|String
name|securityName
parameter_list|)
block|{
name|super
argument_list|(
name|sedol
argument_list|,
name|source
argument_list|)
expr_stmt|;
name|this
operator|.
name|securityName
operator|=
name|securityName
expr_stmt|;
block|}
DECL|method|getSecurityName ()
specifier|public
name|String
name|getSecurityName
parameter_list|()
block|{
return|return
name|securityName
return|;
block|}
DECL|method|setSecurityName (String securityName)
specifier|public
name|void
name|setSecurityName
parameter_list|(
name|String
name|securityName
parameter_list|)
block|{
name|this
operator|.
name|securityName
operator|=
name|securityName
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
return|return
name|securityName
operator|!=
literal|null
condition|?
name|securityName
operator|.
name|hashCode
argument_list|()
else|:
literal|0
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
name|obj
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|obj
operator|==
name|this
condition|)
block|{
return|return
literal|true
return|;
block|}
else|else
block|{
name|B1Record
name|record
init|=
operator|(
name|B1Record
operator|)
name|obj
decl_stmt|;
return|return
name|super
operator|.
name|equals
argument_list|(
name|record
argument_list|)
operator|&&
name|this
operator|.
name|securityName
operator|.
name|equals
argument_list|(
name|record
operator|.
name|getSecurityName
argument_list|()
argument_list|)
return|;
block|}
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
literal|"SEDOL["
operator|+
name|this
operator|.
name|sedol
operator|+
literal|"], SOURCE["
operator|+
name|this
operator|.
name|source
operator|+
literal|"], NAME["
operator|+
name|this
operator|.
name|securityName
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

