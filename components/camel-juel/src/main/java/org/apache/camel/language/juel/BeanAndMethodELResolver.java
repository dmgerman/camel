begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.juel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|juel
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
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Modifier
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|el
operator|.
name|BeanELResolver
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|el
operator|.
name|ELContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|el
operator|.
name|PropertyNotFoundException
import|;
end_import

begin_comment
comment|/**  * An extension of the JUEL {@link BeanELResolver} which also supports the resolving of methods  *  * @version   */
end_comment

begin_class
DECL|class|BeanAndMethodELResolver
specifier|public
class|class
name|BeanAndMethodELResolver
extends|extends
name|BeanELResolver
block|{
DECL|method|BeanAndMethodELResolver ()
specifier|public
name|BeanAndMethodELResolver
parameter_list|()
block|{
name|super
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getValue (ELContext elContext, Object base, Object property)
specifier|public
name|Object
name|getValue
parameter_list|(
name|ELContext
name|elContext
parameter_list|,
name|Object
name|base
parameter_list|,
name|Object
name|property
parameter_list|)
block|{
try|try
block|{
return|return
operator|(
name|property
operator|instanceof
name|Method
operator|)
condition|?
name|property
else|:
name|super
operator|.
name|getValue
argument_list|(
name|elContext
argument_list|,
name|base
argument_list|,
name|property
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|PropertyNotFoundException
name|e
parameter_list|)
block|{
comment|// lets see if its a method call...
name|Method
name|method
init|=
name|findMethod
argument_list|(
name|elContext
argument_list|,
name|base
argument_list|,
name|property
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
name|elContext
operator|.
name|setPropertyResolved
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|method
return|;
block|}
else|else
block|{
throw|throw
name|e
throw|;
block|}
block|}
block|}
DECL|method|findMethod (ELContext elContext, Object base, Object property)
specifier|protected
name|Method
name|findMethod
parameter_list|(
name|ELContext
name|elContext
parameter_list|,
name|Object
name|base
parameter_list|,
name|Object
name|property
parameter_list|)
block|{
if|if
condition|(
name|base
operator|!=
literal|null
condition|)
block|{
name|Method
index|[]
name|methods
init|=
name|base
operator|.
name|getClass
argument_list|()
operator|.
name|getMethods
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Method
argument_list|>
name|matching
init|=
operator|new
name|ArrayList
argument_list|<
name|Method
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Method
name|method
range|:
name|methods
control|)
block|{
if|if
condition|(
name|method
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|property
operator|.
name|toString
argument_list|()
argument_list|)
operator|&&
name|Modifier
operator|.
name|isPublic
argument_list|(
name|method
operator|.
name|getModifiers
argument_list|()
argument_list|)
condition|)
block|{
name|matching
operator|.
name|add
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
block|}
name|int
name|size
init|=
name|matching
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|size
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|size
operator|>
literal|1
condition|)
block|{
comment|// TODO there's currently no way for JUEL to tell us how many parameters there are
comment|// so lets just pick the first one that has a single param by default
for|for
control|(
name|Method
name|method
range|:
name|matching
control|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|paramTypes
init|=
name|method
operator|.
name|getParameterTypes
argument_list|()
decl_stmt|;
if|if
condition|(
name|paramTypes
operator|.
name|length
operator|==
literal|1
condition|)
block|{
return|return
name|method
return|;
block|}
block|}
block|}
comment|// lets default to the first one
return|return
name|matching
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

